package com.example.springboot_new.ai.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    
    private static final String SECRET_KEY = "mySecretKeyForJWTTokenGenerationAndValidation123456789";
    private static final long EXPIRATION_TIME = 86400000; // 24小时
    
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    
    public String generateToken(Integer accountId, String account) {
        return Jwts.builder()
                .setSubject(account)
                .claim("accountId", accountId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public String extractAccount(String token) {
        return extractClaims(token).getSubject();
    }
    
    public Integer extractAccountId(String token) {
        return extractClaims(token).get("accountId", Integer.class);
    }
    
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
    
    public boolean validateToken(String token, String account) {
        return extractAccount(token).equals(account) && !isTokenExpired(token);
    }
}