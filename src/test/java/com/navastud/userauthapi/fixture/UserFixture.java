package com.navastud.userauthapi.fixture;

import com.navastud.userauthapi.model.dto.PhoneDTO;
import com.navastud.userauthapi.model.dto.UserDTO;
import com.navastud.userauthapi.model.dto.UserResponseDTO;
import com.navastud.userauthapi.model.entity.PhoneEntity;
import com.navastud.userauthapi.model.entity.UserEntity;
import com.navastud.userauthapi.util.JwtUtil;
import com.navastud.userauthapi.util.PasswordEncoderUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserFixture {

  public static final String EMAIL = "julio@testssw.cl";
  public static final String NAME = "Julio Gonzalez";
  public static final String PASSWORD = "a2asfGfdfdf4";
  public static final int NUMBER = 87650009;
  public static final int CITY_CODE = 7;
  public static final String COUNTRY_CODE = "25";
  public static long EXPIRATION = 3600000;

  public static UserDTO buildUserDTO() {
    return UserDTO.builder()
        .email(EMAIL)
        .name(NAME)
        .password(PASSWORD)
        .phones(Collections.singletonList(buildPhoneDTO()))
        .build();
  }

  public static PhoneDTO buildPhoneDTO() {
    return PhoneDTO.builder().number(NUMBER).cityCode(CITY_CODE).countryCode(COUNTRY_CODE).build();
  }

  public static UserEntity buildUserEntity() {
    return UserEntity.builder()
        .id(UUID.randomUUID())
        .name(NAME)
        .email(EMAIL)
        .password(PasswordEncoderUtil.encode(PASSWORD))
        .phones(Collections.singletonList(buildPhoneEntity()))
        .isActive(true)
        .created(LocalDateTime.now())
        .lastLogin(LocalDateTime.now())
        .build();
  }

  public static PhoneEntity buildPhoneEntity() {
    return PhoneEntity.builder()
        .id(UUID.randomUUID())
        .number(NUMBER)
        .cityCode(CITY_CODE)
        .countryCode(COUNTRY_CODE)
        .build();
  }

  public static String buildToken() {
    Map<String, Object> claims = new HashMap<>();
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(EMAIL)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
        .signWith(JwtUtil.getSecretKey(), SignatureAlgorithm.HS512)
        .compact();
  }

  public static UserResponseDTO buildUserResponseDTO() {
    return UserResponseDTO.builder()
        .id(UUID.randomUUID())
        .name(NAME)
        .email(EMAIL)
        .password(PASSWORD)
        .token(buildToken())
        .created(LocalDateTime.now())
        .lastLogin(LocalDateTime.now())
        .isActive(true)
        .phones(Collections.singletonList(buildPhoneDTO()))
        .build();
  }
}
