package com.prabinsoft.expense.repo;

import com.prabinsoft.expense.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {

    Optional<Profile> findByEmail(String email);
    Optional<Profile> findByActivationToken(String activationToken);

}
