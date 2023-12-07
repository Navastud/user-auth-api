package com.navastud.userauthapi.service;

import com.navastud.userauthapi.model.dto.UserDTO;
import com.navastud.userauthapi.model.dto.UserResponseDTO;

public interface UserService {

  /**
   * Register a new user and generated new token
   *
   * @param userDTO User DTO
   * @return ResponseEntity<UserResponseDTO>
   */
  UserResponseDTO registerUser(UserDTO userDTO);

  /**
   * Login a user from token
   *
   * @param token JWT
   * @return ResponseEntity<UserResponseDTO>
   */
  UserResponseDTO loginUser(String token);
}
