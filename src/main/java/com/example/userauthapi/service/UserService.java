package com.example.userauthapi.service;

import com.example.userauthapi.model.dto.UserDTO;
import com.example.userauthapi.model.dto.UserResponseDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {

  /**
   * Register a new user and generated new token
   *
   * @param userDTO User DTO
   * @return ResponseEntity<UserResponseDTO>
   */
  ResponseEntity<UserResponseDTO> registerUser(UserDTO userDTO);

  /**
   * Login a user from token
   *
   * @param token JWT
   * @return ResponseEntity<UserResponseDTO>
   */
  ResponseEntity<UserResponseDTO> loginUser(String token);
}
