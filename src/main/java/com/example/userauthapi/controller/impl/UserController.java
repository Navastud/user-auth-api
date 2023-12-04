package com.example.userauthapi.controller.impl;

import com.example.userauthapi.controller.UserOperations;
import com.example.userauthapi.model.dto.UserDTO;
import com.example.userauthapi.model.dto.UserResponseDTO;
import com.example.userauthapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
public class UserController implements UserOperations {

  private final UserService userService;

  /** {@inhericDoc} */
  @Override
  public ResponseEntity<UserResponseDTO> singUp(UserDTO userDTO) {
    log.info("URL: /api/v1/users/sign-up, body:" + userDTO.toString());
    return userService.registerUser(userDTO);
  }

  /** {@inhericDoc} */
  @Override
  public ResponseEntity<UserResponseDTO> login(String token) {
    log.info("URL: /api/v1/users/login, token:" + token);
    return userService.loginUser(token);
  }
}
