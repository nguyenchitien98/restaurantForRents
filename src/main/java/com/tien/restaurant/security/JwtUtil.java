package com.tien.restaurant.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String jwtSecret = "YourSecretKeyMustBeAtLeast256BitsLongYourSecretKeyMustBeAtLeast256BitsLong"; // 256 bit min
    private final long jwtExpirationMs = 86400000; // 1 ngày

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateJwtToken(String email, String tenantId, String agentId, String role) {
        return Jwts.builder()
                .setSubject(email)         // sub
                .claim("tenantId", tenantId)   // custom claim
                .claim("agentId", agentId)     // custom claim
                .claim("role", role)            // thêm role
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey()) // ✅ hợp lệ, KHÔNG deprecated nếu dùng parserBuilder
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public String getTenantIdFromToken(String token) {
        return getClaims(token).get("tenantId", String.class);
    }

    public String getAgentIdFromToken(String token) {
        return getClaims(token).get("agentId", String.class);
    }
}