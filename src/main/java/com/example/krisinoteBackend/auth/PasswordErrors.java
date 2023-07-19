package com.example.krisinoteBackend.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordErrors {

    private Boolean noPasswordServer;
    private Boolean noLength;
    private Boolean noUppercase;
    private Boolean noLowercase;
    private Boolean noNumber;
    private Boolean noSymbol;

}
