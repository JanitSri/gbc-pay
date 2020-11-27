package com.COMP3095.gbc_pay.repositories;

import com.COMP3095.gbc_pay.models.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByRoleNameIgnoreCase(String name);
}
