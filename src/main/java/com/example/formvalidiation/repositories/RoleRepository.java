package com.example.formvalidiation.repositories;

import com.example.formvalidiation.models.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByRoleNameIgnoreCase(String name);
}
