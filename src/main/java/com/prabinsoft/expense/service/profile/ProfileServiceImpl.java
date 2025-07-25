package com.prabinsoft.expense.service.profile;

import com.prabinsoft.expense.dto.AuthRequest;
import com.prabinsoft.expense.dto.profile.ProfileRequest;
import com.prabinsoft.expense.dto.profile.ProfileResponse;
import com.prabinsoft.expense.entity.Profile;
import com.prabinsoft.expense.jwt.JwtHelper;
import com.prabinsoft.expense.repo.ProfileRepository;
import com.prabinsoft.expense.service.AppUserDetailsService;
import com.prabinsoft.expense.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    private final ModelMapper modelMapper;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtHelper jwtHelper;

    private final AppUserDetailsService appUserDetailsService;

    @Override
    public ProfileResponse createProfile(ProfileRequest profileRequest) {
        Profile profile;
        if (profileRequest.getId() != null) {
            profile = profileRepository.findById(profileRequest.getId()).orElse(new Profile());
            modelMapper.map(profileRequest, profile);
        } else {
            profile = modelMapper.map(profileRequest, Profile.class);
        }
        profile.setActivationToken(UUID.randomUUID().toString());
        profile.setPassword(passwordEncoder.encode(profileRequest.getPassword()));
        try {
            profile = profileRepository.save(profile);
            String activationLink = "http://localhost:8848/api/activate?token=" + profile.getActivationToken();
            String subject = "Activate your Expense Tracker Account";
            String body = "Click on the following link to activate your account: " + activationLink;
            emailService.sendMail(profile.getEmail(), subject, body);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Failed to save profile entity", e);
        }
        return modelMapper.map(profile, ProfileResponse.class);
    }

    @Override
    public boolean activateProfile(@RequestParam String activationToken) {
        return profileRepository.findByActivationToken(activationToken).map(e -> {
            e.setIsActive(true);
            profileRepository.save(e);
            return true;
        }).orElse(false);
    }

    @Override
    public boolean isAccountActive(String email) {
        return profileRepository.findByEmail(email)
                .map(Profile::getIsActive).orElse(false);
    }

    @Override
    public Profile getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return profileRepository.findByEmail(authentication.getName()).orElseThrow(() ->
                new UsernameNotFoundException("Profile not found with email: " + authentication.getName()));
    }

    @Override
    public ProfileResponse getPublicProfile(String email) {
        Profile currentUser = null;
        if (email == null) {
            currentUser = getCurrentProfile();
        } else {
            currentUser = profileRepository.findByEmail(email).orElseThrow(() ->
                    new UsernameNotFoundException("Profile not found with email: " + email));
        }

        return modelMapper.map(currentUser, ProfileResponse.class);
    }

    @Override
    public Map<String, Object> authenticateAndGenerateToken(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
                    authRequest.getPassword()));
            UserDetails userDetails = appUserDetailsService.loadUserByUsername(authRequest.getEmail());
            String token = jwtHelper.generateToken(userDetails);
            log.error(token);
            return Map.of(
                    "token", token,
                    "user", getPublicProfile(authRequest.getEmail())
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Invalid email or password");
        }
    }
}
