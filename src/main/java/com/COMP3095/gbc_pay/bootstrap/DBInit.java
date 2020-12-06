/* *********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 3
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: December 05, 2020
 * Description: Initially add data to the database, i.e. admin.
 ******************************************************************************** */

package com.COMP3095.gbc_pay.bootstrap;


import com.COMP3095.gbc_pay.models.*;
import com.COMP3095.gbc_pay.repositories.RoleRepository;
import com.COMP3095.gbc_pay.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DBInit implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    public DBInit(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        User admin = new User("Admin", "Admin", LocalDate.parse("1975-01-01"));
        Role role_user = new Role("USER");
        Role role_admin = new Role("ADMIN");

        // DEFAULT ADMIN
        Profile adminDefaultProfile = new Profile("admin@isp.net", true, passwordEncoder.encode("P@ssword1"));
        Address adminAddress = new Address("Default Admin Street", "Default Admin City", "Default Admin Country", true, true);

        adminDefaultProfile.getRoles().add(role_admin);
        role_admin.getProfiles().add(adminDefaultProfile);

        adminDefaultProfile.setAddress(adminAddress);
        adminAddress.setProfile(adminDefaultProfile);

        adminDefaultProfile.setUser(admin);
        admin.getProfiles().add(adminDefaultProfile);

        userRepository.save(admin);

        roleRepository.save(role_user);

    }
}
