package com.example.userauthapi.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PasswordEncoderUtilTest {

  @Test
  void encodePasswordTest() {
    String currentPassword = "a2asfGfdfdf4";
    assertNotEquals(currentPassword, PasswordEncoderUtil.encode(currentPassword));
  }

  @Test
  void verifyPasswordReturnTrueTest() {
    String currentPassword = "a2asfGfdfdf4";
    String storedPassword = PasswordEncoderUtil.encode(currentPassword);
    assertTrue(PasswordEncoderUtil.verify(currentPassword, storedPassword));
  }

  @Test
  void verifyPasswordReturnFalseTest() {
    String currentPassword = "a2asfGfdfdf4";
    assertFalse(PasswordEncoderUtil.verify(currentPassword, currentPassword));
  }
}
