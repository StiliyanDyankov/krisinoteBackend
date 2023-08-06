package com.example.krisinoteBackend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${app.jwt-secret-key.SECRET_KEY}")
    private String SECRET_KEY;
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // here we have two methods with the same name but diff args
    // aka method overloading
    // this takes in an user object as arg
    // spring security insists on it being standardized to UserDetails
    // that is, UserDetails type is really w/e we define it
    // in our case it's of User type
    public String generateToken(UserDetails userDetails) {
        // here, what we're doing, albeit it being confusing
        // is that we're just calling the second generateToken method
        // passing in two args - the userDetails (user obj) & empty hashmap
        return generateToken(new HashMap<>(), userDetails);
    }

    // here we're taking a map as first arg - mapping strings to objs
    // it's called extractClaims
    // and an userDetails obj
    // I'm confused as to why we're taking the extractClaims arg
    // investigating... the extractClaims is passed to setClaims()
    // when we're using the JWT builder
    public String generateToken(
            Map<String, Object> extractClaims,
            UserDetails userDetails
    ) {
        // the mystery surrounding the map (extractClaims) is uncovered
        // the extractClaims map really just holds the payload (set of claims)
        // of the jwt
        // the methods setSubject, setIssuedAt etc. are really
        // just specialized versions of setClaims, wherein you set
        // commonly used/ standard claims - they're convenience methods
//        userDetails.getPassword()
        // here we're yet again utilizing the builder pattern
        extractClaims.put("Role", userDetails.getAuthorities());
        return Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                // up till here things are fairly standard - we just set some claims
                // here we get the encription key and specify the algorithm
                // to be used when generating the jwt token
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                // I researched what compact() exactly does
                // it's pretty straight forward -
                // the method encodes the token into a URL-safe string
                // this is done, so it could be passed into the url of a http req/res
                // that is, it could be used as a path variable or path parameter in an url
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // what does this method do? can't we just pass in the key
    // as a plain string? Let's investigate.
    private Key getSignInKey() {
        // here we translate the base64 encoded SECRET_KEY
        // and decode it getting back a byte array
        // not sure why that's done
        byte[] keyBytes = Decoders. BASE64.decode(SECRET_KEY);
        // this method returns a SecretKey object
        // apparently in java we have a special obj which represents
        // the key to be used for encryption/decryption
        // what I know is that this method takes in as arg a byte array
        // after a bit more research, it turned out that it has something
        // to do with the SECRET_KEY data integrity - not to be concerned
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
