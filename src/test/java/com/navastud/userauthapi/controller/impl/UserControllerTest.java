package com.navastud.userauthapi.controller.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.navastud.userauthapi.controller.UserController;
import com.navastud.userauthapi.exception.NotFoundException;
import com.navastud.userauthapi.exception.UserAlreadyExistsException;
import com.navastud.userauthapi.fixture.UserFixture;
import com.navastud.userauthapi.model.dto.UserDTO;
import com.navastud.userauthapi.model.dto.UserResponseDTO;
import com.navastud.userauthapi.service.impl.UserServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ExtendWith(SpringExtension.class)
class UserControllerTest {

  @InjectMocks private UserController userController;

  @Mock private UserServiceImpl userService;

  @BeforeEach
  void setUp() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
  }

  @Test
  @SneakyThrows
  void singUpTest() {
    UserResponseDTO userResponseDTO = UserFixture.buildUserResponseDTO();
    when(userService.registerUser(any(UserDTO.class))).thenReturn(userResponseDTO);
    ResponseEntity<UserResponseDTO> response = userController.singUp(UserFixture.buildUserDTO());
    assertThat(response).isNotNull().hasNoNullFieldsOrProperties();
    assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    assertThat(response.getBody())
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasFieldOrPropertyWithValue("name", userResponseDTO.getName())
        .hasFieldOrPropertyWithValue("email", userResponseDTO.getEmail())
        .hasFieldOrPropertyWithValue("password", userResponseDTO.getPassword())
        .hasFieldOrPropertyWithValue("token", userResponseDTO.getToken())
        .hasFieldOrPropertyWithValue("isActive", userResponseDTO.getIsActive())
        .hasFieldOrPropertyWithValue("created", userResponseDTO.getCreated())
        .hasFieldOrPropertyWithValue("lastLogin", userResponseDTO.getLastLogin())
        .hasFieldOrPropertyWithValue("id", userResponseDTO.getId());
    assertThat(response.getBody().getPhones())
        .isNotEmpty()
        .hasSize(1)
        .element(0)
        .hasNoNullFieldsOrProperties()
        .hasFieldOrPropertyWithValue("number", userResponseDTO.getPhones().get(0).getNumber())
        .hasFieldOrPropertyWithValue("cityCode", userResponseDTO.getPhones().get(0).getCityCode())
        .hasFieldOrPropertyWithValue(
            "countryCode", userResponseDTO.getPhones().get(0).getCountryCode());
    verify(userService, times(1)).registerUser(any(UserDTO.class));
  }

  @Test
  @SneakyThrows
  void singUpTestThrowExceptionWhenUserAlreadyExistTest() {
    String message = String.format("User with email %s already exists.", UserFixture.EMAIL);
    when(userService.registerUser(any(UserDTO.class)))
        .thenThrow(new UserAlreadyExistsException(message));
    assertThatThrownBy(() -> userController.singUp(UserFixture.buildUserDTO()))
        .isNotNull()
        .isInstanceOf(UserAlreadyExistsException.class)
        .hasNoNullFieldsOrProperties()
        .hasMessage(message);
    verify(userService, times(1)).registerUser(any(UserDTO.class));
  }

  @Test
  void loginTest() {
    UserResponseDTO userResponseDTO = UserFixture.buildUserResponseDTO();
    when(userService.loginUser(anyString())).thenReturn(userResponseDTO);
    ResponseEntity<UserResponseDTO> response = userController.login(userResponseDTO.getToken());
    assertThat(response).isNotNull().hasNoNullFieldsOrProperties();
    assertEquals(response.getStatusCode(), HttpStatus.OK);
    assertThat(response.getBody())
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasFieldOrPropertyWithValue("name", userResponseDTO.getName())
        .hasFieldOrPropertyWithValue("email", userResponseDTO.getEmail())
        .hasFieldOrPropertyWithValue("password", userResponseDTO.getPassword())
        .hasFieldOrPropertyWithValue("token", userResponseDTO.getToken())
        .hasFieldOrPropertyWithValue("isActive", userResponseDTO.getIsActive())
        .hasFieldOrPropertyWithValue("created", userResponseDTO.getCreated())
        .hasFieldOrPropertyWithValue("lastLogin", userResponseDTO.getLastLogin())
        .hasFieldOrPropertyWithValue("id", userResponseDTO.getId());
    assertThat(response.getBody().getPhones())
        .isNotEmpty()
        .hasSize(1)
        .element(0)
        .hasNoNullFieldsOrProperties()
        .hasFieldOrPropertyWithValue("number", userResponseDTO.getPhones().get(0).getNumber())
        .hasFieldOrPropertyWithValue("cityCode", userResponseDTO.getPhones().get(0).getCityCode())
        .hasFieldOrPropertyWithValue(
            "countryCode", userResponseDTO.getPhones().get(0).getCountryCode());
    verify(userService, times(1)).loginUser(anyString());
  }

  @Test
  void loginReturnExceptionWhenUserNotFoundTest() {
    String message = String.format("User with email %s has not found.", UserFixture.EMAIL);
    when(userService.loginUser(anyString())).thenThrow(new NotFoundException(message));
    assertThatThrownBy(() -> userController.login(UserFixture.buildToken()))
        .isNotNull()
        .isInstanceOf(NotFoundException.class)
        .hasNoNullFieldsOrProperties()
        .hasMessage(message);
    verify(userService, times(1)).loginUser(anyString());
  }
}
