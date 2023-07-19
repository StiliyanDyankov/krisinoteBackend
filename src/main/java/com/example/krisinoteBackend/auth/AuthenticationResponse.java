package com.example.krisinoteBackend.auth;

import jakarta.annotation.Nullable;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    @Nullable
    private ResponseSuccessData data;

    @Nullable
    private ResponseErrorData error;

}

