package com.risi.demo.gateway.service;

import com.risi.demo.gateway.domain.TechUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${token.expiration}")
    private int tokenExpiration;
    private Clock clock = DefaultClock.INSTANCE;

    private Key secret = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public String refreshToken(String token) {
        var createdDate = clock.now();
        var expirationDate = calculateExpirationDate(createdDate);

        var claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder().setClaims(claims).signWith(secret).compact();
    }

    private Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + tokenExpiration);
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(TechUser user) {
        Map<String, Object> claims = new HashMap<>();
        for (GrantedAuthority authority : user.getAuthorities())
            claims.put(authority.getAuthority(), authority.getAuthority().toUpperCase());
        return doGenerateToken(claims, user.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String username) {
        var currentDate = new Date();
        var expirationTimeLong = currentDate.getTime() + tokenExpiration;

        final var createdDate = new Date();
        final Date expirationDate = new Date(currentDate.getTime() + expirationTimeLong);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(secret)
                .compact();
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

}
