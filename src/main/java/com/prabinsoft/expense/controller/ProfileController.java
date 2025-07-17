package com.prabinsoft.expense.controller;

import com.prabinsoft.expense.constants.ModuleNameConstants;
import com.prabinsoft.expense.dto.AuthRequest;
import com.prabinsoft.expense.dto.profile.ProfileRequest;
import com.prabinsoft.expense.dto.profile.ProfileResponse;
import com.prabinsoft.expense.enums.Message;
import com.prabinsoft.expense.exception.GlobalApiResponse;
import com.prabinsoft.expense.service.profile.ProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/")
@Tag(name = ModuleNameConstants.PROFILE)
@RequiredArgsConstructor
public class ProfileController extends BaseController {
    private final ProfileService profileService;

    @PostConstruct
    public void init() {
        this.module = ModuleNameConstants.PROFILE;
    }

    @PostMapping("/create")
    public ResponseEntity<GlobalApiResponse> create(@Valid @RequestBody ProfileRequest profileRequest) {
        ProfileResponse savedProfile = profileService.createProfile(profileRequest);
        return ResponseEntity.ok(successResponse(customMessageSource.getMessage(Message.SAVE.getCode(), module), savedProfile));
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile(String token) {
        boolean isActivated = profileService.activateProfile(token);
        if (isActivated) {
            return ResponseEntity.ok("Profile activated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation token not found");
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthRequest authRequest) {
        try {
            if (!profileService.isAccountActive(authRequest.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        Map.of("message", "Account is not active. Please activate your account first"));
            }
            Map<String, Object> response = profileService.authenticateAndGenerateToken(authRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", e.getMessage()
            ));
        }
    }
}
