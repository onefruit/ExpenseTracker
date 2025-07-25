package com.prabinsoft.expense.dto.profile;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProfileRequest {
    private Integer id;
    private String fullName;
    private String password;
    private String email;
    private String profileImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
