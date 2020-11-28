package com.COMP3095.gbc_pay.services.user;

import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.models.User;
import com.COMP3095.gbc_pay.repositories.ProfileRepository;
import com.COMP3095.gbc_pay.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public UserService(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    public Profile findByEmail(String email) {
        Optional<Profile> userProfile = profileRepository.findByEmailIgnoreCase(email);

        return userProfile.orElse(null);
    }

    public User findByUser(User user){
        Optional<User> foundUser = userRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndDateOfBirth(
                user.getFirstName(), user.getLastName(), user.getDateOfBirth());

        return foundUser.orElse(null);
    }

    public boolean userProfileExists(String email) {
        return findByEmail(email) != null;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void saveProfile(Profile profile) {
        profileRepository.save(profile);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Profile profile = findByEmail(email);

        if (profile == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }

        return new UserDetailsImp(profile);
    }
}
