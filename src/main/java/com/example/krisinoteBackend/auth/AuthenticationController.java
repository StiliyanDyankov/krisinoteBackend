package com.example.krisinoteBackend.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
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
    public ResponseEntity<AuthenticationResponse> registerAndSendCode(
            @RequestBody RegisterRequest request
            // map the request body contents to arg
    ) {
        // return okay with body, returned by register method
        return service.registerAndSendCode(request);
    }
    @PostMapping("/verification/register")
    public ResponseEntity<AuthenticationResponse> verifyAndRegister(
            @RequestBody VerificationRequest request, @RequestHeader("Authorization") String token
    ) {
        return service.verifyAndRegister(request, token);
    }
    @GetMapping("/verification/newCode")
    public ResponseEntity<AuthenticationResponse> resendCode(
            @RequestHeader("Authorization") String token
    ) {
        return service.resendCode(token);
    }

    // the same process happens here
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        // return okay with body, returned by register method
        return service.authenticate(request);
    }
    @PostMapping("/forgottenPassword")
    public ResponseEntity<AuthenticationResponse> getEmailAndSendCode(
            @RequestBody RegisterRequest request
            // map the request body contents to arg
    ) {
        // return okay with body, returned by register method
        return service.getEmailAndSendCode(request);
    }
    @PostMapping("/verification/forgottenPassword")
    public ResponseEntity<AuthenticationResponse> verifyAndPassToChange(
            @RequestBody VerificationRequest request, @RequestHeader("Authorization") String token
    ) {
        return service.verifyAndPassToChange(request, token);
    }
    @PostMapping("/newPassword")
    public ResponseEntity<AuthenticationResponse> setNewPassword(
            @RequestBody ForgottenPasswordRequest request, @RequestHeader("Authorization") String token
    ) {
        return service.setNewPassword(request, token);
    }
}
