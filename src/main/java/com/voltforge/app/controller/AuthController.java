package com.voltforge.app.controller;

import com.voltforge.app.model.AppRole;
import com.voltforge.app.model.RoleModel;
import com.voltforge.app.model.UserModel;
import com.voltforge.app.repository.RoleRespository;
import com.voltforge.app.repository.UserRepository;
import com.voltforge.app.security.jwt.JwtUtils;
import com.voltforge.app.security.request.LoginRequestDTO;
import com.voltforge.app.security.request.SignupRequestDTO;
import com.voltforge.app.security.response.MessageResponseDTO;
import com.voltforge.app.security.response.UserInfoResponseDTO;
import com.voltforge.app.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRespository roleRespository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequestDTO){
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getLoginUsername(), loginRequestDTO.getLoginPassword()));
        } catch (AuthenticationException e) {
            Map<String, Object> response = new HashMap<>();

            response.put("message", "Invalid username or password");
            response.put("status", "false");

            return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtTokenFromCookie(userDetails);

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponseDTO userInfoResponseDTO = new UserInfoResponseDTO(userDetails.getUsername(), roles);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(userInfoResponseDTO);

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerNewUser(@Valid @RequestBody SignupRequestDTO signupRequestDTO) {
        if (userRepository.existsByUserName(signupRequestDTO.getUserName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponseDTO("Error: Username is already taken!"));
        }

        if (userRepository.existsByUserEmail(signupRequestDTO.getUserEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponseDTO("Error: Email is already in use!"));
        }

        UserModel newUser =  new UserModel(signupRequestDTO.getUserName(),
                signupRequestDTO.getUserFirstName(),
                signupRequestDTO.getUserMiddleName(),
                signupRequestDTO.getUserLastName(),
                passwordEncoder.encode(signupRequestDTO.getPassword()),
                signupRequestDTO.getUserMobileNumber(),
                signupRequestDTO.getUserEmail());

        Set<String> strRoles = signupRequestDTO.getUserRoles();
        Set<RoleModel> roleModels = new HashSet<>();

        if (strRoles == null && strRoles.isEmpty()) {
            RoleModel userRole = roleRespository
                    .findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role Not Found"));
            roleModels.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        RoleModel adminRole = roleRespository
                                .findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role Not Found"));
                        roleModels.add(adminRole);
                        break;
                    case "seller":
                        RoleModel sellerRole = roleRespository
                                .findByRoleName(AppRole.ROLE_SELLER)
                                .orElseThrow(() -> new RuntimeException("Error: Role Not Found"));
                        roleModels.add(sellerRole);
                        break;
                    default:
                        RoleModel userRole = roleRespository
                                .findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role Not Found"));
                        roleModels.add(userRole);
                }
            });
        }

        newUser.setUserRoles(roleModels);
        userRepository.save(newUser);

        return ResponseEntity.ok(new MessageResponseDTO("User registered successfully!"));
    }

    @GetMapping("/username")
    public String getCurrentUsername(Authentication authentication) {
        if (authentication != null) {
            return authentication.getName();
        } else {
            return "";
        }
    }

    @GetMapping("/user")
    public ResponseEntity<UserInfoResponseDTO> getCurrentUserDetails(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponseDTO userInfoResponseDTO = new UserInfoResponseDTO(userDetails.getUsername(), roles);

        return ResponseEntity.ok().body(userInfoResponseDTO);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signout() {
        ResponseCookie jwtCookie = jwtUtils.generateCleanJwtTokenFromCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new MessageResponseDTO("Successfully logged out!"));
    }
}
