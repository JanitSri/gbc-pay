package com.example.formvalidiation.bootstrap;

import com.example.formvalidiation.models.Role;
import com.example.formvalidiation.models.User;
import com.example.formvalidiation.repositories.RoleRepository;
import com.example.formvalidiation.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DBInit implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DBInit(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) throws Exception {
        User admin1 = new User("admin", "admin", "admin address",
                 passwordEncoder.encode("P@ssword1"), "admin@isp.net",true);

        Role role_user = new Role("USER");
        Role role_admin = new Role("ADMIN");

        admin1.getRoles().add(role_admin);
        role_admin.getUsers().add(admin1);

        roleRepository.save(role_admin);
        userRepository.save(admin1);

        roleRepository.save(role_user);

        System.out.println("Number of users: " + userRepository.count());
        System.out.println("Number of roles: " + roleRepository.count());
    }
}
