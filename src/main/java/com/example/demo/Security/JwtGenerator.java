package com.example.demo.Security;

import com.example.demo.DbService;
import com.example.demo.dto.InfoInToken;
import com.example.demo.models.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class JwtGenerator {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final DbService dbService;

    @Autowired
    public JwtGenerator(DbService dbService){
        this.dbService = dbService;
    }

    public static String generateToken(String email, String password) {
        var expirationTime = 1000 * 60 * 60; // 1 час

        Map<String, Object> claims = new HashMap<>();
        claims.put("password", password);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .addClaims(claims)
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
            var password = (String) claims.getBody().get("password");
            var dbPassword = user.getPassword();
            return new InfoInToken(Objects.equals(password, dbPassword), email);
        } catch (MalformedJwtException ex) {
            // Неправильный формат токена.
            return new InfoInToken(false, "");
        } catch (ExpiredJwtException ex) {
            // Токен истек.
            return new InfoInToken(false, "");
        } catch (UnsupportedJwtException ex) {
            // Неподдерживаемый тип токена.
            return new InfoInToken(false, "");
        } catch (IllegalArgumentException ex) {
            // Неверный токен.
            return new InfoInToken(false, "");
        }
    }
}