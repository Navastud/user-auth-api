package com.example.userauthapi.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.example.userauthapi.model.dto.UserDTO;
import com.example.userauthapi.model.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/users")
@Validated
public interface UserOperations {

  @PostMapping(
      value = "/sign-up",
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @Operation(summary = "Sing-Up User")
  @PermitAll
  ResponseEntity<UserResponseDTO> singUp(@Valid @RequestBody UserDTO userDTO);

  @GetMapping(value = "/login", produces = APPLICATION_JSON_VALUE)
  ResponseEntity<UserResponseDTO> login(@RequestHeader("Authorization") String token);
}
