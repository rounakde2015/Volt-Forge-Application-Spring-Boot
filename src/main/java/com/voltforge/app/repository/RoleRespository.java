package com.voltforge.app.repository;

import com.voltforge.app.model.AppRole;
import com.voltforge.app.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRespository extends JpaRepository<RoleModel, Long> {
    Optional<RoleModel> findByRoleName(AppRole appRole);
}
