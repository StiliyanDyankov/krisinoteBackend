package com.example.krisinoteBackend.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    // covered v

    // it's important to note that we actually predefine the shapes of
    // the requests we are expecting from the client
    // by defining them as classes and passing them in as generics
    // to the response entity class

    // the @RequestBody extracts the fields from the body corresponding
    // to the properties of the RegisterRequest class
    // this extraction is automatic, which is pretty cool
    // that's how it populates the request obj, served as arg.
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
            // map the request body contents to arg
    ) {
        // return okay with body, returned by register method
        return ResponseEntity.ok(service.register(request));
    }

    // the same process happens here
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request
    ) {
        // return okay with body, returned by register method
        return ResponseEntity.ok(service.authenticate(request));
    }
}
