package com.COMP3095.formvalidiation.services.user;

import com.COMP3095.formvalidiation.models.Profile;
import com.COMP3095.formvalidiation.models.User;
import com.COMP3095.formvalidiation.repositories.ProfileRepository;
import com.COMP3095.formvalidiation.repositories.UserRepository;
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

    public boolean userProfileExists(String email) {
        return findByEmail(email) != null;
    }

    public void saveUser(User newUser) {
        userRepository.save(newUser);
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
