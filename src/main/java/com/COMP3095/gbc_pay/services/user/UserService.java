package com.COMP3095.gbc_pay.services.user;

import com.COMP3095.gbc_pay.models.Credit;
import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.models.Role;
import com.COMP3095.gbc_pay.models.User;
import com.COMP3095.gbc_pay.repositories.CreditRepository;
import com.COMP3095.gbc_pay.repositories.ProfileRepository;
import com.COMP3095.gbc_pay.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final RoleService roleService;
    private final CreditRepository creditRepository;

    public UserService(UserRepository userRepository, ProfileRepository profileRepository, RoleService roleService,
                       CreditRepository creditRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.roleService = roleService;
        this.creditRepository = creditRepository;
    }

    public Profile findByEmail(String email) {
        Optional<Profile> userProfile = profileRepository.findByEmailIgnoreCase(email);

        return userProfile.orElse(null);
    }

    public Profile findByProfileById(Long id) {
        Optional<Profile> userProfile = profileRepository.findById(id);

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

    public void deleteProfile(Profile profile){
        User foundUser = userRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndDateOfBirth(
                profile.getUser().getFirstName(), profile.getUser().getLastName(), profile.getUser().getDateOfBirth())
                .get();
        
        for(Role r : profile.getRoles()){
            Role role = roleService.getRole(r.getRoleName());
            role.getProfiles().removeIf(profile1 -> profile1.getId().equals(profile.getId()));
        }

        foundUser.getProfiles().removeIf(profile1 -> profile1.getId().equals(profile.getId()));

        saveUser(foundUser);
        profileRepository.deleteById(profile.getId());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Profile profile = findByEmail(email);

        if (profile == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }

        return new UserDetailsImp(profile);
    }

    public void updateProfile(Profile profile, User user){
        profileRepository.save(profile);

    }

    public List<Profile> getAllProfiles(){
        return (List<Profile>) profileRepository.findAll();
    }

    public void saveCard(Credit card){
        creditRepository.save(card);
    }

    public void deleteCard(Credit card){
        creditRepository.deleteById(card.getId());
    }
}






