package com.example.userauthapi.service.impl;

import static com.example.userauthapi.fixture.UserFixture.EMAIL;
import static com.example.userauthapi.fixture.UserFixture.buildToken;
import static com.example.userauthapi.fixture.UserFixture.buildUserDTO;
import static com.example.userauthapi.fixture.UserFixture.buildUserEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.userauthapi.exception.NotFoundException;
import com.example.userauthapi.exception.TokenExpiredException;
import com.example.userauthapi.model.dto.UserDTO;
import com.example.userauthapi.model.dto.UserResponseDTO;
import com.example.userauthapi.model.entity.UserEntity;
import com.example.userauthapi.repository.UserRepository;
import com.example.userauthapi.util.JwtUtil;
import com.example.userauthapi.util.PasswordEncoderUtil;
import io.jsonwebtoken.Claims;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest(properties = {"jwt.expiration=3600000", "password.secret.key=spring-security-2023"})
class UserServiceImplTest {

  @InjectMocks private UserServiceImpl userService;

  @Mock private UserRepository userRepository;

  @Test
  void registerUserReturnPersistenceUserTest() {

    UserDTO userDTO = buildUserDTO();
    UserEntity userEntity = buildUserEntity();

    when(userRepository.findByEmail(eq(EMAIL))).thenReturn(Optional.empty());
    when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

    ResponseEntity<UserResponseDTO> response = userService.registerUser(userDTO);
    assertThat(response).isNotNull().hasNoNullFieldsOrProperties();

    assertThat(response.getBody())
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasFieldOrPropertyWithValue("name", userDTO.getName())
        .hasFieldOrPropertyWithValue("email", userDTO.getEmail())
        .hasFieldOrPropertyWithValue("phones", userDTO.getPhones())
        .hasFieldOrProperty("token")
        .hasFieldOrPropertyWithValue("isActive", userEntity.getIsActive())
        .hasFieldOrPropertyWithValue("created", userEntity.getCreated())
        .hasFieldOrPropertyWithValue("lastLogin", userEntity.getLastLogin())
        .hasFieldOrPropertyWithValue("id", userEntity.getId());

    assertFalse(JwtUtil.isTokenExpired(response.getBody().getToken()));
    assertTrue(PasswordEncoderUtil.verify(userDTO.getPassword(), response.getBody().getPassword()));
    verify(userRepository, times(1)).findByEmail(eq(EMAIL));
    verify(userRepository, times(1)).save(any(UserEntity.class));
  }

  @Test
  void registerUserThrowExceptionWhenUserAlreadyExistTest() {
    when(userRepository.findByEmail(eq(EMAIL))).thenReturn(Optional.of(buildUserEntity()));
    assertThatThrownBy(() -> userService.registerUser(buildUserDTO()))
        .hasNoNullFieldsOrProperties()
        .hasMessage(String.format("User with email %s already exists.", EMAIL));

    verify(userRepository, times(1)).findByEmail(eq(EMAIL));
    verify(userRepository, never()).save(any(UserEntity.class));
  }

  @Test
  void loginUserTest() {
    String token = JwtUtil.generateToken(EMAIL);
    UserEntity userEntity = buildUserEntity();
    when(userRepository.findByEmail(eq(EMAIL))).thenReturn(Optional.of(userEntity));
    ResponseEntity<UserResponseDTO> userResponseDTOResponseEntity = userService.loginUser(token);
    assertThat(userResponseDTOResponseEntity.getBody())
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasFieldOrPropertyWithValue("name", userEntity.getName())
        .hasFieldOrPropertyWithValue("email", userEntity.getEmail())
        .hasFieldOrPropertyWithValue("password", userEntity.getPassword())
        .hasFieldOrPropertyWithValue("token", token)
        .hasFieldOrPropertyWithValue("isActive", userEntity.getIsActive())
        .hasFieldOrPropertyWithValue("created", userEntity.getCreated())
        .hasFieldOrPropertyWithValue("lastLogin", userEntity.getLastLogin())
        .hasFieldOrPropertyWithValue("id", userEntity.getId());

    assertThat(userResponseDTOResponseEntity.getBody().getPhones())
        .isNotEmpty()
        .hasSize(1)
        .element(0)
        .hasNoNullFieldsOrProperties()
        .hasFieldOrPropertyWithValue("number", userEntity.getPhones().get(0).getNumber())
        .hasFieldOrPropertyWithValue("cityCode", userEntity.getPhones().get(0).getCityCode())
        .hasFieldOrPropertyWithValue("countryCode", userEntity.getPhones().get(0).getCountryCode());
    verify(userRepository, times(1)).findByEmail(eq(EMAIL));
  }

  @Test
  void loginUserThrowExceptionWhenUserHasNotFoundTest() {
    String token = JwtUtil.generateToken(EMAIL);
    when(userRepository.findByEmail(eq(EMAIL))).thenReturn(Optional.empty());
    assertThatThrownBy(() -> userService.loginUser(token))
        .isNotNull()
        .isInstanceOf(NotFoundException.class)
        .hasMessage(String.format("User with email %s has not found.", EMAIL));
    verify(userRepository, times(1)).findByEmail(eq(EMAIL));
  }

  @Test
  void loginUserThrowExceptionWhenTokenHasExpiredTest() {
    String token = buildToken();
    Claims claimsFromToken = JwtUtil.getClaimsFromToken(token);
    LocalDateTime localDateTime = LocalDateTime.now().minusHours(2);
    claimsFromToken.setExpiration(
        Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
    assertThatThrownBy(() -> userService.loginUser(token))
        .isNotNull()
        .isInstanceOf(TokenExpiredException.class)
        .hasMessage("The token has expired.");
    verify(userRepository, never()).findByEmail(eq(EMAIL));
  }
}
