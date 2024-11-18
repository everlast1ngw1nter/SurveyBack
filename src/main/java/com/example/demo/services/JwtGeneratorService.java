package com.example.demo.services;

import com.example.demo.DbService;
import com.example.demo.dto.InfoInToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtGeneratorService {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final DbService dbService;

    @Autowired
    public JwtGeneratorService(DbService dbService){
        this.dbService = dbService;
    }

    public static String generateToken(String email) {
        var expirationTime = 1000 * 60 * 60; // 1 час

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public InfoInToken isValidToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                return new InfoInToken(false, "");
            }

            var email = claims.getBody().getSubject();
            var user = dbService.getUser(email);
            if (user == null) {
                return new InfoInToken(false, "");
            }
            return new InfoInToken(true, email);
        } catch (Exception ex) {
            return new InfoInToken(false, "");
        }
    }
}