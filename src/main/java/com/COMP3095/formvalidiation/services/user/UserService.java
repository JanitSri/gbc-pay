/********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 2
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: November 08, 2020
 * Description: User service that provides access to the user repository.
 *********************************************************************************/

package com.COMP3095.formvalidiation.services.user;

import com.COMP3095.formvalidiation.models.User;
import com.COMP3095.formvalidiation.repositories.UserRepository;
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

    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmailIgnoreCase(email);

        return user.orElse(null);
    }

    public boolean userExists(String email) {
        return findByEmail(email) != null;
    }

    public void saveUser(User newUser) {
        userRepository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }

        return new UserDetailsImp(user);
    }
}
