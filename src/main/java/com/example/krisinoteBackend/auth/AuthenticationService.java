package com.example.krisinoteBackend.auth;

import com.example.krisinoteBackend.config.JwtService;
import com.example.krisinoteBackend.user.Role;
import com.example.krisinoteBackend.user.User;
import com.example.krisinoteBackend.user.UserDAOImpl;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserDAOImpl repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // a service func which returns body of req
    // takes in the whole req as arg
    public AuthenticationResponse register(RegisterRequest request) {
        // creates a new user, implementing the builder pattern
        // here we set all the fields of the user
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        // we save the already created by the builder user
        // we use the repository method save
        // !!! we should have a check whether the user is already registered
        repository.save(user);

        // we generate new token with the jwtservice generatetoken method
        // we pass in the user obj as the argument
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
    // takes in the request body as argument
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
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

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // we throw if the authentication is unsuccessful
        // we get the userDetails obj
        var user = repository.getUserByEmail(request.getEmail())
                .orElseThrow();

        // we generate a jwt token with the UserDetails obj
        var jwtToken = jwtService.generateToken(user);

        // we build authentication response, which essentially
        // just populates the body with a json with body token: "..."
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
