package com.navastud.userauthapi.util;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.navastud.userauthapi.exception.TokenExpiredException;
import com.navastud.userauthapi.fixture.UserFixture;
import io.jsonwebtoken.Claims;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class JwtUtilTest {

  @Test
  void generateTokenShouldNotBeNullTest() {
    assertNotNull(JwtUtil.generateToken(UserFixture.EMAIL));
  }

  @Test
  void generateTokenShouldHaveCorrectFormatTest() {
    assertTrue(
        JwtUtil.generateToken(UserFixture.EMAIL)
            .matches("[a-zA-Z0-9-_=]+\\.[a-zA-Z0-9-_=]+\\.[a-zA-Z0-9-_.+/=]+"));
  }

  @Test
  void generateTokenShouldHaveCorrectFormatWithMockClaimsTest() {
    assertTrue(
        JwtUtil.generateToken(UserFixture.EMAIL)
            .matches("[a-zA-Z0-9-_=]+\\.[a-zA-Z0-9-_=]+\\.[a-zA-Z0-9-_.+/=]+"));
  }

  @Test
  void getEmailFromTokenShouldExtractEmailTest() {
    String token = JwtUtil.generateToken(UserFixture.EMAIL);
    Assertions.assertEquals(UserFixture.EMAIL, JwtUtil.getEmailFromToken(token));
  }

  @Test
  void isTokenExpiredShouldReturnFalseForFutureExpirationTest() {
    String token = JwtUtil.generateToken(UserFixture.EMAIL);
    assertFalse(JwtUtil.isTokenExpired(token));
  }

  @Test
  void isTokenExpiredShouldReturnTrueForPastExpirationTest() {
    String token = UserFixture.buildToken();
    Claims claimsFromToken = JwtUtil.getClaimsFromToken(token);
    LocalDateTime localDateTime = LocalDateTime.now().minusHours(2);
    claimsFromToken.setExpiration(
        Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
    assertTrue(JwtUtil.isTokenExpired(token));
  }

  @Test
  void isTokenExpiredShouldReturnTrueForModifiedTokenTest() {
    String token = JwtUtil.generateToken(UserFixture.EMAIL);
    String modifiedToken = token.replace('a', 'b');

    assertThatThrownBy(() -> JwtUtil.isTokenExpired(modifiedToken))
        .isNotNull()
        .isInstanceOf(TokenExpiredException.class)
        .hasMessage(
            "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.");
  }

  @Test
  void isTokenExpiredShouldReturnTrueForNullTokenTest() {
    assertThatThrownBy(() -> JwtUtil.isTokenExpired(null))
        .isNotNull()
        .isInstanceOf(TokenExpiredException.class)
        .hasMessage("Token is incorrect or null.");
  }
}
