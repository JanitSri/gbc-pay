package com.COMP3095.gbc_pay.services.user;

import com.COMP3095.gbc_pay.models.Role;
import com.COMP3095.gbc_pay.repositories.RoleRepository;
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
}
