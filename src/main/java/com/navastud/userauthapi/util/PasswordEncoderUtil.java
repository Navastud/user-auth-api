package com.navastud.userauthapi.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@UtilityClass
public class PasswordEncoderUtil {

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public static String encode(String password) {
    return passwordEncoder.encode(password);
  }

  public static boolean verify(String rawPassword, String storedPassword) {
    return passwordEncoder.matches(rawPassword, storedPassword);
  }
}
