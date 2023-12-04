package com.example.userauthapi.util;

import static com.example.userauthapi.fixture.UserFixture.EMAIL;
import static com.example.userauthapi.fixture.UserFixture.buildToken;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.userauthapi.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class JwtUtilTest {

  @Test
  void generateTokenShouldNotBeNull() {
    assertNotNull(JwtUtil.generateToken(EMAIL));
  }

  @Test
  void generateTokenShouldHaveCorrectFormat() {
    assertTrue(
        JwtUtil.generateToken(EMAIL)
            .matches("[a-zA-Z0-9-_=]+\\.[a-zA-Z0-9-_=]+\\.[a-zA-Z0-9-_.+/=]+"));
  }

  @Test
  void getEmailFromTokenShouldExtractEmail() {
    String token = JwtUtil.generateToken(EMAIL);
    assertEquals(EMAIL, JwtUtil.getEmailFromToken(token));
  }

  @Test
  void isTokenExpiredShouldReturnFalseForFutureExpiration() {
    String token = JwtUtil.generateToken(EMAIL);
    assertFalse(JwtUtil.isTokenExpired(token));
  }

  @Test
  void isTokenExpiredShouldReturnTrueForPastExpiration() {
    String token = buildToken();
    Claims claimsFromToken = JwtUtil.getClaimsFromToken(token);
    LocalDateTime localDateTime = LocalDateTime.now().minusHours(2);
    claimsFromToken.setExpiration(
        Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
    assertTrue(JwtUtil.isTokenExpired(token));
  }

  @Test
  void isTokenExpiredShouldReturnTrueForModifiedToken() {
    String token = JwtUtil.generateToken(EMAIL);
    String modifiedToken = token.replace('a', 'b');

    assertThatThrownBy(() -> JwtUtil.isTokenExpired(modifiedToken))
        .isNotNull()
        .isInstanceOf(TokenExpiredException.class)
        .hasMessage(
            "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.");
  }

  @Test
  void isTokenExpiredShouldReturnTrueForNullToken() {
    assertThatThrownBy(() -> JwtUtil.isTokenExpired(null))
        .isNotNull()
        .isInstanceOf(TokenExpiredException.class)
        .hasMessage("Token is incorrect or null.");
  }
}
