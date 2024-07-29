package com.pdp.ecommerce.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdp.ecommerce.entity.Role;
import com.pdp.ecommerce.entity.User;
import com.pdp.ecommerce.exception.TokenExpiredException;
import com.pdp.ecommerce.service.MailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private final MailService mailService;

    public String generateToken(User user) {
        return "Bearer " + Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60*30))
                .claim("roles", user.getRoles())
                .signWith(getPrivateKey())
                .compact();
    }

    public String generateRefreshToken(User user) {
        return "Bearer " + Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60*24*7))
                .claim("roles", user.getRoles())
                .signWith(getPrivateKey())
                .compact();
    }

    public String generateConfirmationToken(User user) {
        Random random = new Random();
        int code = random.nextInt(100, 1000);
        System.out.println(code);  //for test purposes
        mailService.sendConfirmationCode(code, user.getUsername());
        System.out.println(code);
        return "Confirmation " + Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60*10))
                .claim("code", code)
                .claim("user", user)
                .signWith(getPrivateKey())
                .compact();
    }

    private Key getPrivateKey() {
        byte[] bytes = Decoders.BASE64.decode("a2345678a2345678a2345678a2345678a2345678a2345678a2345678a2345678");
        return Keys.hmacShaKeyFor(bytes);
    }

    public boolean isValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("Token is expired");
        }
    }

    public String getEmail(String token) {
        Jws<Claims> claimsJws = getClaims(token);
        return claimsJws.getBody().getSubject();
    }

    public List<Role> getRoles(String token) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Jws<Claims> claimsJws = getClaims(token);
        String json = mapper.writeValueAsString(claimsJws.getBody().get("roles"));
        List<Role> roles = mapper.readValue(json, new TypeReference<List<Role>>() {});
        return roles;
    }

    public User getUser(String token) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Jws<Claims> claimsJws = getClaims(token);
        String json = mapper.writeValueAsString(claimsJws.getBody().get("user"));
        User user = mapper.readValue(json, new TypeReference<User>() {});
        return user;
    }

    private Jws<Claims> getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getPrivateKey())
                .build()
                .parseClaimsJws(token);
    }

    public boolean checkVerificationCode(String code, String token) {
        String codeFromToken = getVerificationCodeFromToken(token);
        return  code.equals(codeFromToken);
    }

    private String getVerificationCodeFromToken(String token) {
        try {
            Jws<Claims> claims = getClaims(token);
            return String.valueOf(claims.getBody().get("code", Integer.class));
        } catch (RuntimeException e) {
            throw new RuntimeException("Invalid verification code");
        }
    }
}
