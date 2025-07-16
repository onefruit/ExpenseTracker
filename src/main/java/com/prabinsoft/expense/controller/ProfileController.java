package com.prabinsoft.expense.controller;

import com.prabinsoft.expense.constants.ModuleNameConstants;
import com.prabinsoft.expense.dto.profile.ProfileRequest;
import com.prabinsoft.expense.dto.profile.ProfileResponse;
import com.prabinsoft.expense.enums.Message;
import com.prabinsoft.expense.exception.GlobalApiResponse;
import com.prabinsoft.expense.service.profile.ProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
@Tag(name = ModuleNameConstants.PROFILE)
public class ProfileController extends BaseController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
        this.module = ModuleNameConstants.PROFILE;
    }

    @PostMapping("/create")
    public ResponseEntity<GlobalApiResponse> create(@Valid @RequestBody ProfileRequest profileRequest) {
        ProfileResponse savedProfile = profileService.createProfile(profileRequest);
        return ResponseEntity.ok(successResponse(customMessageSource.getMessage(Message.SAVE.getCode(), module), savedProfile));
    }
}
