package com.redaeilco.ecommerce.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    private final String SECRET_KEY = "efacd432b90a4b6229ed8e0a862eb51e0d117c416e955f2b4d36918411df1f456b26d4d0ef2c1ef86f0c789334b003ceae38113b2d67af0aa801627bb61fbb70";
    private final int EXPIRATION_TIME = 1000 * 60 * 30; // 30 min

    public String generateToken(String username, String role, int userId) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userId", userId);

        return Jwts.builder()
            .claims(claims)
            .subject(username)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis () + EXPIRATION_TIME))
            .signWith(getKey())
            .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        token = formatToken(token);
        String username = extractClaim(token, Claims::getSubject);
        return username;
    }

    public int extractUserId(String token) {
        token = formatToken(token);
        return extractClaim(token, claims -> claims.get("userId", Integer.class));
    }

    public String extractRole(String token) {
        token = formatToken(token);
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        token = formatToken(token);
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        token = formatToken(token);
        return extractClaim(token, Claims::getExpiration);
    }

    private String formatToken(String token) {
        return token.startsWith("Bearer ") ? token.substring(7) : token;
    }
    
}