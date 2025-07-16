package com.prabinsoft.expense.controller;

import com.prabinsoft.expense.dto.profile.ProfileRequest;
import com.prabinsoft.expense.dto.profile.ProfileResponse;
import com.prabinsoft.expense.exception.GlobalApiResponse;
import com.prabinsoft.expense.service.profile.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class ProfileController extends BaseController {
    private final ProfileService profileService;

    @PostMapping("/create")
    public ResponseEntity<GlobalApiResponse> create(@Valid @RequestBody ProfileRequest profileRequest) {
        ProfileResponse profile = profileService.createProfile(profileRequest);
        return ResponseEntity.ok(successResponse("save", profile));
    }
}
