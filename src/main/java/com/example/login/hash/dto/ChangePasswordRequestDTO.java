package com.example.login.hash.dto;

import lombok.Data;

@Data
public class ChangePasswordRequestDTO {
    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;
}
