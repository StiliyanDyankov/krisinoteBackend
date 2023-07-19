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
public class ResponseErrorData<T> {

    @Nullable
    private String message;

    @Nullable
    private T errors;

    @Nullable
    private ErrorTypes type;
}
