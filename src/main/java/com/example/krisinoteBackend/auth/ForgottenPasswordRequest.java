package com.example.krisinoteBackend.auth;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForgottenPasswordRequest {
    @Nullable
    private String email;
    @Nullable
    private String newPassword;
}
