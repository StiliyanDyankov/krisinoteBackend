package com.example.krisinoteBackend.auth;

import com.example.krisinoteBackend.config.JwtService;
import com.example.krisinoteBackend.mail.MailingService;
import com.example.krisinoteBackend.user.Role;
import com.example.krisinoteBackend.user.User;
import com.example.krisinoteBackend.user.UserDAOImpl;
import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserDAOImpl repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MailingService mailingService;

    // a service func which returns body of req
    // takes in the whole req as arg

    public ResponseEntity<AuthenticationResponse> registerAndSendCode(RegisterRequest request) { // OK!
        // creates a new user, implementing the builder pattern
        // here we set all the fields of the user

        System.out.println("first request");
        System.out.println(request);

        var user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        System.out.println("first user");
        System.out.println(user);

        // we save the already created by the builder user
        // we use the repository method save
        // !!! we should have a check whether the user is already registered

//        repository.save(user);

        // we generate new token with the jwtservice generate token method
        // we pass in the user obj as the argument

        Integer verificationCode = 10000 + new Random().nextInt(90000);


//        verificationCode.toString();

        if(repository.getUserByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(409).body(
                AuthenticationResponse.builder()
                    .error(
                        ResponseErrorData.builder().errors(
                            EmailErrors.builder()
                                .alreadyExists(true)
                                .noEmailServer(false)
                                .invalidEmailForm(false)
                                .build()
                        )
                        .type(ErrorTypes.EMAIL_ERROR)
                        .build()
                )
                .build()
            );
        }

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("verificationCode", verificationCode);
        claims.put("email", user.getEmail());
        claims.put("password", user.getPassword());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());

        var jwtToken = jwtService.generateToken(claims, user);

//        try {
//            mailingService.sendHtmlEmail(user.getEmail(), verificationCode.toString());
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }

        return ResponseEntity.status(200).body(AuthenticationResponse.builder()
                        .data(
                                ResponseSuccessData.builder().message("Success").token(jwtToken).build()
                        )
                .build());
    }

    public ResponseEntity<AuthenticationResponse> verifyAndRegister(VerificationRequest request, String token) { // OK!
        Claims allClaims = jwtService.extractAllClaims(token.substring(7));
        String trueVerificationCode = allClaims.get("verificationCode").toString();
        String userVerificationCode = request.getVerificationCode();


        System.out.println(trueVerificationCode);
        System.out.println(userVerificationCode);

        if(!Objects.equals(trueVerificationCode, userVerificationCode)) {
            return ResponseEntity.status(409).body(
                    AuthenticationResponse.builder()
                            .error(
                                    ResponseErrorData.builder().errors(
                                                    Errors.builder()
                                                            .message("Invalid pin")
                                                            .build()
                                            )
                                            .build()
                            )
                            .build()
            );
        }

        var user = User.builder()
                .firstName((String) allClaims.get("firstName"))
                .lastName((String) allClaims.get("lastName"))
                .email(allClaims.getSubject())
                .password((String) allClaims.get("password"))
                .role(Role.USER)
                .build();

        System.out.println("second user before save");
        System.out.println(user);

        repository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return ResponseEntity.status(200).body(AuthenticationResponse.builder()
                .data(
                        ResponseSuccessData.builder().message("Success").token(jwtToken).build()
                )
                .build());

    }

    // takes in the request body as argument

    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        // not sure what exactly happens here tbh, let's investigate
        // we're just using auth manager, which is injected from somewhere
        // prob some utility obj provided by spring

        // nevermind, the authentication manager is something to be configured
        // it's configured in the application config as a bean

        // things are straight-forward
        // the authenticationManager is the interface through which auth is
        // done - it has only a single method - authenticate
        // this method takes in an authentication object and returns
        // such an obj in the case of successfull auth process

        // essentially what happens here is that
        // 1. we pass in the credentials we've retreved from the req body
        // 2. wrap them in UserPassAuthToken
        // 3. pass the obj to the manager's authenticate() method
        // 4. the object is then passed to an authProvider
        // 5. the auth provider calls the UserDetails Service itself
        // passing in the username
        // 6. The service retrieves the UserDetails obj from some store
        // 7. somewhere along the chain the passwords are compared
        // 8. if they match the auth manager returns an Authentication obj
        if(!repository.getUserByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(409).body(
                    AuthenticationResponse.builder()
                            .error(
                                    ResponseErrorData.builder().errors(
                                                    EmailErrors.builder()
                                                            .alreadyExists(false)
                                                            .noEmailServer(true)
                                                            .invalidEmailForm(false)
                                                            .build()
                                            )
                                            .type(ErrorTypes.EMAIL_ERROR)
                                            .build()
                            )
                            .build()
            );
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

        } catch(Exception e) {
            return ResponseEntity.status(403).body(
                    AuthenticationResponse.builder()
                            .error(
                                    ResponseErrorData.builder().errors(
                                                    PasswordErrors.builder()
                                                            .noLength(false)
                                                            .noLowercase(false)
                                                            .noNumber(false)
                                                            .noPasswordServer(true)
                                                            .noSymbol(false)
                                                            .noUppercase(false)
                                                            .build()
                                            )
                                            .type(ErrorTypes.PASSWORD_ERROR)
                                            .build()
                            )
                            .build()
            );
        }

        // we throw if the authentication is unsuccessful
        // we get the userDetails obj
        var user = repository.getUserByEmail(request.getEmail())
                .orElseThrow();

        // we generate a jwt token with the UserDetails obj
        var jwtToken = jwtService.generateToken(user);

        // we build authentication response, which essentially
        // just populates the body with a json with body token: "..."
        return ResponseEntity.status(200).body(AuthenticationResponse.builder()
                .data(
                        ResponseSuccessData.builder().message("Success").token(jwtToken).build()
                )
                .build());
    }


    public ResponseEntity<AuthenticationResponse> resendCode(String token) {
        Claims allClaims = jwtService.extractAllClaims(token.substring(7));

        Integer verificationCode = 10000 + new Random().nextInt(90000);

        var user = User.builder()
                .email(allClaims.getSubject())
                .role(Role.USER)
                .build();

        HashMap<String, Object> claims = new HashMap<>();
//        claims.put("verificationCode", verificationCode);
//        claims.put("email", user.getEmail());
//        claims.put("password", user.getPassword());
//        claims.put("firstName", user.getFirstName());
//        claims.put("lastName", user.getLastName());

        for (Map.Entry<String, Object> entry : allClaims.entrySet()) {
            if(entry.getKey().equals("verificationCode")){
                claims.put(entry.getKey(), verificationCode);
            } else {
                claims.put(entry.getKey(), entry.getValue());
            }
        }

        var jwtToken = jwtService.generateToken(claims, user);

        try {
            mailingService.sendHtmlEmail(user.getEmail(), verificationCode.toString());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(200).body(AuthenticationResponse.builder()
                .data(
                        ResponseSuccessData.builder().message("Success").token(jwtToken).build()
                )
                .build());
    }


    public ResponseEntity<AuthenticationResponse> getEmailAndSendCode(RegisterRequest request) {

        if(!repository.getUserByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(409).body(
                    AuthenticationResponse.builder()
                            .error(
                                    ResponseErrorData.builder().errors(
                                                    EmailErrors.builder()
                                                            .alreadyExists(false)
                                                            .noEmailServer(true)
                                                            .invalidEmailForm(false)
                                                            .build()
                                            )
                                            .type(ErrorTypes.EMAIL_ERROR)
                                            .build()
                            )
                            .build()
            );
        }
        // we save the already created by the builder user
        // we use the repository method save
        // !!! we should have a check whether the user is already registered

        User user = repository.getUserByEmail(request.getEmail()).orElseThrow();
//        repository.save(user);

        // we generate new token with the jwtservice generate token method
        // we pass in the user obj as the argument

        Integer verificationCode = 10000 + new Random().nextInt(90000);

//        verificationCode.toString();

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("verificationCode", verificationCode);

        var jwtToken = jwtService.generateToken(claims, user);

        try {
            mailingService.sendHtmlEmail(user.getEmail(), verificationCode.toString());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(200).body(AuthenticationResponse.builder()
                .data(
                        ResponseSuccessData.builder().message("Success").token(jwtToken).build()
                )
                .build());
    }


    public ResponseEntity<AuthenticationResponse> verifyAndPassToChange(VerificationRequest request, String token) {
        Claims allClaims = jwtService.extractAllClaims(token.substring(7));
        String trueVerificationCode = allClaims.get("verificationCode").toString();
        String userVerificationCode = request.getVerificationCode();

        if(!Objects.equals(trueVerificationCode, userVerificationCode)) {
            return ResponseEntity.status(409).body(
                    AuthenticationResponse.builder()
                            .error(
                                    ResponseErrorData.builder().errors(
                                                    Errors.builder()
                                                            .message("Invalid pin")
                                                            .build()
                                            )
                                            .build()
                            )
                            .build()
            );
        }

        var user = User.builder()
                .email(allClaims.getSubject())
                .role(Role.USER)
                .build();

        var jwtToken = jwtService.generateToken(user);

        return ResponseEntity.status(200).body(AuthenticationResponse.builder()
                .data(
                        ResponseSuccessData.builder().message("Success").token(jwtToken).build()
                )
                .build());
    }


    public ResponseEntity<AuthenticationResponse> setNewPassword(ForgottenPasswordRequest request, String token) {
        Claims allClaims = jwtService.extractAllClaims(token.substring(7));

        String newPassEncoded = passwordEncoder.encode(request.getNewPassword());

        User currentUser = repository.getUserByEmail(allClaims.getSubject()).orElseThrow();

        currentUser.setPassword(newPassEncoded);

        repository.update(currentUser, currentUser.getId());

        var user = User.builder()
                .email(currentUser.getEmail())
                .role(Role.USER)
                .build();

        var jwtToken = jwtService.generateToken(user);

        return ResponseEntity.status(200).body(AuthenticationResponse.builder()
                .data(
                        ResponseSuccessData.builder().message("Success").token(jwtToken).build()
                )
                .build());
    }
}
