/********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 2
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: November 08, 2020
 * Description: Role service that provides access to the role repository.
 *********************************************************************************/

package com.example.formvalidiation.services.user;

import com.example.formvalidiation.models.Role;
import com.example.formvalidiation.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRole(String roleName) {
        Optional<Role> role = roleRepository.findByRoleNameIgnoreCase(roleName);
        return role.orElse(null);
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }
}
