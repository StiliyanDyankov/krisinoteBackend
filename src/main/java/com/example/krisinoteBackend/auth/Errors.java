package com.example.krisinoteBackend.auth;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Errors<T> {

    @Nullable
    private T errors;

    @Nullable
    private String message;
}

