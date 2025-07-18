package com.prabinsoft.expense.service.profile;

import com.prabinsoft.expense.dto.AuthRequest;
import com.prabinsoft.expense.dto.profile.ProfileRequest;
import com.prabinsoft.expense.dto.profile.ProfileResponse;
import com.prabinsoft.expense.entity.Profile;

import java.util.Map;

public interface ProfileService {
    ProfileResponse createProfile(ProfileRequest profileRequest);

    boolean activateProfile(String activationToken);

    boolean isAccountActive(String email);

    Profile getCurrentProfile();

    ProfileResponse getPublicProfile(String email);

    Map<String, Object> authenticateAndGenerateToken(AuthRequest authRequest);
}
