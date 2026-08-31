package com.voltforge.app.security.web;


import com.voltforge.app.model.AppRole;
import com.voltforge.app.model.Role;
import com.voltforge.app.model.User;
import com.voltforge.app.repository.RoleRespository;
import com.voltforge.app.repository.UserRepository;
import com.voltforge.app.security.jwt.AuthEntryPointJwt;
import com.voltforge.app.security.jwt.AuthTokenFilter;
import com.voltforge.app.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);

        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        //.requestMatchers("/api/v1/admin/**").permitAll()
                        //.requestMatchers("/api/v1/public/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .anyRequest().authenticated());

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers(headers -> headers.frameOptions(
                frameOptionsConfig ->  frameOptionsConfig.sameOrigin()));

        return http.build();
    }

    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers("/v2/api-docs/",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /*@Bean
    public CommandLineRunner initDbData(RoleRespository roleRespository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return (args) -> {
            Role userRole = roleRespository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> {
                        Role newUserRole = new Role(AppRole.ROLE_USER);
                        return roleRespository.save(newUserRole);
                    });

            Role adminRole = roleRespository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
                        return roleRespository.save(newAdminRole);
                    });

            Role sellerRole = roleRespository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(() -> {
                        Role newSellerRole = new Role(AppRole.ROLE_SELLER);
                        return roleRespository.save(newSellerRole);
                    });

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> adminRoles = Set.of(adminRole);
            Set<Role> sellerRoles = Set.of(sellerRole);

            if (!userRepository.existsByUserName("user1")) {
                User user1 = new User("user1",
                        "Rakesh",
                        "Kumar",
                        "Das",
                        passwordEncoder.encode("password1234"),
                        "9876543210",
                        "rakeshkumar@gmail.com");
                userRepository.save(user1);
            }

            if (!userRepository.existsByUserName("seller1")) {
                User seller1 = new User("seller1",
                        "Sanjay",
                        "",
                        "Sharma",
                        passwordEncoder.encode("password4321"),
                        "9876543211",
                        "sanajaysharma@gmail.com");
                userRepository.save(seller1);
            }

            if (!userRepository.existsByUserName("admin")) {
                User admin = new User("admin",
                        "Bhajan",
                        "Lal",
                        "Singh",
                        passwordEncoder.encode("password6789"),
                        "9876554321",
                        "bhajanlalsingh@gmail.com");
                userRepository.save(admin);
            }

            userRepository.findByUserName("user1").ifPresent(user -> {
                user.setUserRoles(userRoles);
                userRepository.save(user);
            });

            userRepository.findByUserName("seller1").ifPresent(seller -> {
                seller.setUserRoles(sellerRoles);
                userRepository.save(seller);
            });

            userRepository.findByUserName("admin").ifPresent(admin -> {
                admin.setUserRoles(adminRoles);
                userRepository.save(admin);
            });
        };
    }*/

}
