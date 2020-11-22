/**********************************************************************************
* Project: GBC PAY - The Raptors
* Assignment: Assignment 2
* Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
* Student Number: 101229102, 101186743, 101028504
* Date: November 08, 2020
* Description: Initially add data to the database, i.e. admin.
*********************************************************************************/

package com.COMP3095.formvalidiation.bootstrap;

import com.COMP3095.formvalidiation.models.Role;
import com.COMP3095.formvalidiation.models.User;
import com.COMP3095.formvalidiation.repositories.RoleRepository;
import com.COMP3095.formvalidiation.repositories.UserRepository;
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

        admin1.getRoles().add(role_user);
        role_user.getUsers().add(admin1);

        roleRepository.save(role_user);
        userRepository.save(admin1);
    }
}
