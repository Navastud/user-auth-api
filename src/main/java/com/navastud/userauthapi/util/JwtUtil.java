package com.navastud.userauthapi.util;

import com.navastud.userauthapi.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.crypto.SecretKey;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

@Log4j2
@UtilityClass
public class JwtUtil {

  @Value("${jwt.expiration}")
  private long expiration;

  private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

  public static SecretKey getSecretKey() {
    return secretKey;
  }

  public static String generateToken(String email) {
    Map<String, Object> claims = new HashMap<>();
    String token =
        Jwts.builder()
            .setClaims(claims)
            .setSubject(email)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSecretKey(), SignatureAlgorithm.HS512)
            .compact();
    log.info("Generated token is {}", token);
    return token;
  }

  public static String getEmailFromToken(String token) {
    return getClaimsFromToken(token).getSubject();
  }

  public static boolean isTokenExpired(String token) {

    if (Objects.isNull(token) || token.isEmpty()) {
      throw new TokenExpiredException("Token is incorrect or null.");
    }

    final Date expirationDate = getClaimsFromToken(token).getExpiration();
    return !expirationDate.before(new Date());
  }

  public static Claims getClaimsFromToken(String token) {
    try {
      token = token.replaceAll("Bearer ", "");
      return Jwts.parserBuilder()
          .setSigningKey(getSecretKey())
          .setAllowedClockSkewSeconds(60)
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (JwtException exception) {
      throw new TokenExpiredException(exception.getMessage());
    }
  }
}
