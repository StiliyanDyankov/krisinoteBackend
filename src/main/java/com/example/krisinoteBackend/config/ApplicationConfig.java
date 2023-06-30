package com.example.krisinoteBackend.config;

import com.example.krisinoteBackend.user.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserDAO userDAO;

    // the userDetailsService is the service, which actually implements
    // the retrieval of user information from some store
    // it should return a UserDetails Obj
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userDAO.getUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // here we configure the auth provider and set it as a bean to be
    // managed by spring
    // the authProvider is an abstraction over the implementation
    // details involved with the process of fetching a userDetails
    // object from somewhere

    // one of the many possible provided by spring providers is the
    // daoAuthProvider, to which we set the actual implementation itself
    // and also a password encoder

    // there is more to be said here - the userDetailsService has a
    // simple interface - it takes in a single argument - String username
    // and must return a UserDetails object
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
