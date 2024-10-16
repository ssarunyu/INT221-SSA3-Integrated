package int221.sit.taskboard.Jwt;

import int221.sit.taskboard.entities.itbkk_shared.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // กำหนดอายุ access token เป็น 30 นาที
    @Value("#{${jwt.max-token-interval-hour}*60*60*1000}")
    private int JWT_TOKEN_VALIDITY;

    // กำหนดอายุ refresh token เป็น 24 ชั่วโมง
    @Value("#{${jwt.refresh-token-interval-hour}*60*60*1000}")
    private int JWT_REFRESH_TOKEN_VALIDITY;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getUserIdFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("oid", String.class));
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(Users userInfo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userInfo.getUsername());
        claims.put("name", userInfo.getName());
        claims.put("oid", userInfo.getUserId());
        claims.put("email", userInfo.getEmail());
        claims.put("role", userInfo.getRole());
        return doGenerateToken(claims);
    }

    private String doGenerateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
//                .setSubject(subject)
                .setIssuer("https://intproj23.sit.kmutt.ac.th/ssa3/")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(signatureAlgorithm, SECRET_KEY)
                .compact();
    }

    // Method เพื่อสร้าง refresh token โดยใช้ข้อมูลจาก token หลัก
    public String gerarRefreshToken(Users userInfo) {
        Map<String, Object> refreshClaims = new HashMap<>();
        refreshClaims.put("oid", userInfo.getUserId());

        // ตั้งค่า expired ของ refresh token เป็น 24 ชั่วโมง
        return doGenerateRefreshToken(refreshClaims);
    }

    private String doGenerateRefreshToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setIssuer("https://intproj23.sit.kmutt.ac.th/ssa3/")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY))
                .signWith(signatureAlgorithm, SECRET_KEY)
                .compact();
    }

    public String generateTokenWithClaims(Users userInfo) {
        Map<String, Object> newClaims = new HashMap<>();
        newClaims.put("oid", userInfo.getUserId());
        newClaims.put("name", userInfo.getName());
        newClaims.put("email", userInfo.getEmail());
        newClaims.put("role", userInfo.getRole());


        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(newClaims)
                .setIssuer("https://intproj23.sit.kmutt.ac.th/ssa3/")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(signatureAlgorithm, SECRET_KEY)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String usernameFromToken = getUsernameFromToken(token);
        return (usernameFromToken != null && usernameFromToken.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}

