package com.COMP3095.gbc_pay.bootstrap;


import com.COMP3095.gbc_pay.models.*;
import com.COMP3095.gbc_pay.repositories.RoleRepository;
import com.COMP3095.gbc_pay.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class DBInit implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DBInit(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        // DEFAULT USER FOR TESTING
        User user = new User("john", "smith", LocalDate.parse("1990-01-20"));

        Profile profile2 = new Profile("test@hotmail.com", false, passwordEncoder.encode("test123"));
        Address address2 = new Address("123 Pine Blvd", "Mississauga", "Canada", true, true);

        profile2.setAddress(address2);
        address2.setProfile(profile2);

        Credit credit2 = new Credit(CardType.VISA, LocalDate.parse("2022-01-20"), "John Smith",
                "9876 9876 9876 9876", true);

        Set<Credit> profile2_credit = new HashSet<Credit>(){{
            add(credit2);
        }};
        profile2.setCredits(profile2_credit);
        credit2.setProfile(profile2);

        VerificationToken verificationToken2 = new VerificationToken(UUID.randomUUID().toString(), LocalDate.now());

        profile2.setVerificationToken(verificationToken2);
        verificationToken2.setProfile(profile2);

        Message message1 = new Message("Login Error",
                "I having trouble logging into my account.",
                false);

        Message message2 = new Message("Error has been fixed",
                "You should be able to login into your account now.",
                false);

        List<Message> messages_second = new ArrayList<>() {{
            add(message1);
            add(message2);
        }};

        profile2.setMessages(messages_second);

        Role role1 = new Role("USER");

        profile2.getRoles().add(role1);
        role1.getProfiles().add(profile2);

        profile2.setUser(user);

        Set<Profile> profiles = new HashSet<Profile>() {{
            add(profile2);
        }};

        user.setProfiles(profiles);

        userRepository.save(user);


        // DEFAULT ADMIN USER
        User admin = new User("Admin", "Admin", LocalDate.parse("1975-01-01"));

        Profile adminDefaultProfile = new Profile("admin@isp.net", true, passwordEncoder.encode("P@ssword1"));
        Address adminAddress = new Address("Default Admin Street", "Default Admin City", "Default Admin Country", true, true);

        adminDefaultProfile.setAddress(adminAddress);
        adminAddress.setProfile(adminDefaultProfile);

        Role adminRole = new Role("ADMIN");

        adminDefaultProfile.getRoles().add(adminRole);
        adminRole.getProfiles().add(adminDefaultProfile);

        adminDefaultProfile.setUser(admin);
        admin.getProfiles().add(adminDefaultProfile);
        userRepository.save(admin);
    }
}
