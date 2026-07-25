package com.voltforge.app.repository;

import com.voltforge.app.model.AppRole;
import com.voltforge.app.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRespository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
