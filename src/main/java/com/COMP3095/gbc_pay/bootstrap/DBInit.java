package com.COMP3095.gbc_pay.bootstrap;


import com.COMP3095.gbc_pay.models.*;
import com.COMP3095.gbc_pay.repositories.UserRepository;
import com.COMP3095.gbc_pay.services.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class DBInit implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;


    public DBInit(UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        // DEFAULT USER FOR TESTING
        User admin = new User("Admin", "Admin", LocalDate.parse("1975-01-01"));
        User user = new User("john", "smith", LocalDate.parse("1990-01-20"));
        Role role_user = new Role("USER");
        Role role_admin = new Role("ADMIN");


        // ADMIN
        Profile adminDefaultProfile = new Profile("admin@isp.net", true, passwordEncoder.encode("P@ssword1"));
        Address adminAddress = new Address("Default Admin Street", "Default Admin City", "Default Admin Country", true, true);

        adminDefaultProfile.getRoles().add(role_admin);
        role_admin.getProfiles().add(adminDefaultProfile);

        adminDefaultProfile.setAddress(adminAddress);
        adminAddress.setProfile(adminDefaultProfile);

        adminDefaultProfile.setUser(admin);
        admin.getProfiles().add(adminDefaultProfile);


        userRepository.save(admin);


        // Profile 1 - SAME USERS
        Profile profile1 = new Profile("test1@hotmail.com", false, passwordEncoder.encode("test123"));
        Address address1 = new Address("123 Main Street", "Toronto", "Canada", false, true);

        profile1.setAddress(address1);
        address1.setProfile(profile1);

        Credit credit1 = new Credit(CardType.MASTER_CARD, "03 2022", "John Smith",
                "1234 1234 1234 1234", true);

        Set<Credit> profile1_credit = new HashSet<Credit>(){{
            add(credit1);
        }};
        profile1.setCredits(profile1_credit);
        credit1.setProfile(profile1);

        VerificationToken verificationToken1 = new VerificationToken(UUID.randomUUID().toString(), LocalDate.now());

        profile1.setVerificationToken(verificationToken1);
        verificationToken1.setProfile(profile1);

        Message message1 = new Message("Login Error",
                "I having trouble logging into my account.",
                false);

        List<Message> messages_first = new ArrayList<>() {{
            add(message1);
        }};

        profile1.setMessages(messages_first);

        Set<Role> roles1 = new HashSet<Role>() {{
            add(role_user);
        }};

        profile1.setRoles(roles1);
        role_user.getProfiles().add(profile1);

        profile1.setUser(user);

        Set<Profile> profiles = new HashSet<>(){{
            add(profile1);
        }};

        user.setProfiles(profiles);

        userRepository.save(user);


        // Profile 2 - SAME USERS
        Profile profile2 = new Profile("test2@hotmail.com", false, passwordEncoder.encode("test123"));
        Address address2 = new Address("123 Pine Blvd", "Mississauga", "Canada", true, false);

        profile2.setAddress(address2);
        address2.setProfile(profile2);

        Credit credit2 = new Credit(CardType.VISA, "04 2025", "John Smith",
                "9876 9876 9876 9876", true);

        Set<Credit> profile2_credit = new HashSet<Credit>(){{
            add(credit2);
        }};
        profile2.setCredits(profile2_credit);
        credit2.setProfile(profile2);

        VerificationToken verificationToken2 = new VerificationToken(UUID.randomUUID().toString(), LocalDate.now());

        profile2.setVerificationToken(verificationToken2);
        verificationToken2.setProfile(profile2);

        Message message2 = new Message("Error has been fixed",
                "You should be able to login into your account now.",
                false);

        List<Message> messages_second = new ArrayList<>() {{
            add(message1);
            add(message2);
        }};

        profile2.setMessages(messages_second);


        Set<Role> roles2 = new HashSet<Role>() {{
            add(role_user);
            //add(role_admin);
        }};

        profile2.setRoles(roles2);
        role_user.getProfiles().add(profile2);
        //role_admin.getProfiles().add(profile2);

        profile2.setUser(user);


        Set<Profile> profiles2 = new HashSet<>(){{
            add(profile2);
        }};


        User existingUser = userService.findByUser(user);

        existingUser.setProfiles(profiles2);

        userRepository.save(existingUser);
    }
}
