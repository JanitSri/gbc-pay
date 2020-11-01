package com.example.formvalidiation.services;

import com.example.formvalidiation.models.User;
import com.example.formvalidiation.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email){
        Optional<User> user = userRepository.findByEmailIgnoreCase(email);

        return user.orElse(null);

    }

    public boolean userExists(String email){
        return findByEmail(email) != null;
    }

    public User saveUser(User newUser){
        return userRepository.save(newUser);
    }
}
