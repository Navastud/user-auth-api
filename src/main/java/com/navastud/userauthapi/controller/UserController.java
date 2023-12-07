package com.navastud.userauthapi.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.navastud.userauthapi.model.dto.UserDTO;
import com.navastud.userauthapi.model.dto.UserResponseDTO;
import com.navastud.userauthapi.service.UserService;
import com.navastud.userauthapi.util.UriUtil;
import io.swagger.v3.oas.annotations.Operation;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {

  private final UserService userService;

  @PostMapping(
      value = "/sign-up",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @Operation(summary = "Sing-Up User")
  @PermitAll
  public ResponseEntity<UserResponseDTO> singUp(@Valid @RequestBody UserDTO userDTO) {
    log.info("URL: /api/v1/users/sign-up, body:" + userDTO.toString());
    UserResponseDTO userResponseDTO = userService.registerUser(userDTO);
    return ResponseEntity.created(UriUtil.buildUserUri(userResponseDTO.getId()))
        .body(userResponseDTO);
  }

  @GetMapping(value = "/login", produces = APPLICATION_JSON_VALUE)
  @Operation(summary = "Login User")
  public ResponseEntity<UserResponseDTO> login(@RequestHeader("Authorization") String token) {
    log.info("URL: /api/v1/users/login, token:" + token);
    return ResponseEntity.ok(userService.loginUser(token));
  }
}
