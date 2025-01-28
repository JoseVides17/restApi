package com.josevides.restApi.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

    private String secretKey = "89366ba5d25ea3e9a704d525316f9792cb157b68ee291e3224612997cdaa717fd61aead03cdb49414c98aa010b6da43db7068720cd1e89e5ba41abf48bb5961278f585aa8d64853a419c881be9520fb1e5d6d77beda4cd634f152dd6f09889260aff47f1a6119556fa085f2daff22796a256e8195a6d5149d0878b668cb7c7cb";
    private final String superRole = "ADMIN";

    public JWTUtil(){

    }

    public String generateToken(String role) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .and()
                .signWith(getKey())
                .compact();

    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractRole(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean verifyRole(String jwt, String roleRequest) {
        String role = extractRole(jwt);
        return role != null && (role.equals(roleRequest) || role.equals(superRole));
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

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false; // Token is invalid if any exception occurs during parsing
        }
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
