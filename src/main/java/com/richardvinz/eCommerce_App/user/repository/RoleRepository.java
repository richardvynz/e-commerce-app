package com.richardvinz.eCommerce_App.user.repository;

import com.richardvinz.eCommerce_App.user.enums.AppRole;
import com.richardvinz.eCommerce_App.user.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
