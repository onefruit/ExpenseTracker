package com.prabinsoft.expense.service.profile;

import com.prabinsoft.expense.dto.profile.ProfileRequest;
import com.prabinsoft.expense.dto.profile.ProfileResponse;
import com.prabinsoft.expense.entity.ProfileEntity;
import com.prabinsoft.expense.repo.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    private final ModelMapper modelMapper;

    @Override
    public ProfileResponse createProfile(ProfileRequest profileRequest) {
        ProfileEntity profileEntity;
        if (profileRequest.getId() != null) {
            profileEntity = profileRepository.findById(profileRequest.getId()).orElse(new ProfileEntity());
            modelMapper.map(profileRequest, profileEntity);
        } else {
            profileEntity = modelMapper.map(profileRequest, ProfileEntity.class);
        }
        profileEntity.setActivationToken(UUID.randomUUID().toString());
        try {
            profileEntity = profileRepository.save(profileEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Failed to save profile entity", e);
        }
        return modelMapper.map(profileEntity, ProfileResponse.class);
    }
}
