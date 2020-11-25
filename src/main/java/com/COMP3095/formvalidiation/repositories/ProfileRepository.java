package com.COMP3095.formvalidiation.repositories;

import com.COMP3095.formvalidiation.models.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long> {
    Optional<Profile> findByEmailIgnoreCase(String email);
}
