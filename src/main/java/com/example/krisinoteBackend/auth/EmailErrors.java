package com.example.krisinoteBackend.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailErrors implements IEmailErrors {

    private Boolean alreadyExists = false;
    private Boolean noEmailServer = false;
    private Boolean invalidEmailForm = false;
}

