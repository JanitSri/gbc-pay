package com.example.formvalidiation.services.user;

import com.example.formvalidiation.models.User;
import com.example.formvalidiation.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email);

        if(user == null){
            throw new UsernameNotFoundException("User not found: "+ email);
        }

        UserDetailsImp userDetailsImp = new UserDetailsImp(user);

        return userDetailsImp;
    }
}
