package com.vivekdev.TaskApp.security;

import io.jsonwebtoken.Claims;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

// This class is responsible to generate, validate the JWT Token
@Service
@Slf4j
public class JWTUtils {

    public static final long EXPIRATION_TIME=30L*24*60*60*1000 ;  // 30 Days in Millisec
    private SecretKey key;

    @Value("${secreteJwtString}")
    private String secreteJwtString;

    // This Method(i.e init()) should run Automatically as soon as this class is called
    @PostConstruct
    public void init(){
        // We are converting the secretKey into byte Array

        byte[] keyBytes=secreteJwtString.getBytes(StandardCharsets.UTF_8);  // We are extracting the Byte Data from JwtStringkey using Standard Algorithm
        this.key=new SecretKeySpec(keyBytes,"HmacSHA256");
    }

    public String generateToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }


    // Refer to the Coding Shuttle, Much easier implementation there
    public String getUsernameFromToken(String token){
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims,T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username=getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractClaims(token,Claims::getExpiration).before(new Date());
    }
}
