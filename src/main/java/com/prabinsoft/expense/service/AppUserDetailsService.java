package com.prabinsoft.expense.service;

import com.prabinsoft.expense.entity.Profile;
import com.prabinsoft.expense.repo.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Profile profile = profileRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("Profile not found with email: " + email));

        return User.builder()
                .username(profile.getEmail())
                .password(profile.getPassword())
                .authorities(Collections.emptyList())
                .build();

    }
}
