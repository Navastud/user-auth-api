package com.example.userauthapi.service.impl;

import com.example.userauthapi.exception.NotFoundException;
import com.example.userauthapi.exception.TokenExpiredException;
import com.example.userauthapi.exception.UserAlreadyExistsException;
import com.example.userauthapi.mapper.UserMapper;
import com.example.userauthapi.model.dto.UserDTO;
import com.example.userauthapi.model.dto.UserResponseDTO;
import com.example.userauthapi.model.entity.UserEntity;
import com.example.userauthapi.repository.UserRepository;
import com.example.userauthapi.service.UserService;
import com.example.userauthapi.util.JwtUtil;
import com.example.userauthapi.util.UriUtil;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  /** {@inhericDoc} */
  @Override
  public ResponseEntity<UserResponseDTO> registerUser(UserDTO userDTO) {

    Optional<UserEntity> optUser = userRepository.findByEmail(userDTO.getEmail());

    if (optUser.isPresent()) {
      throw new UserAlreadyExistsException(
          String.format("User with email %s already exists.", userDTO.getEmail()));
    }

    UserEntity userEntity = userRepository.save(UserMapper.INSTANCE.mapUserEntity(userDTO));
    String token = JwtUtil.generateToken(userEntity.getEmail());

    UserResponseDTO userResponseDTO = UserMapper.INSTANCE.mapUserResponseDTO(userEntity, token);

    return ResponseEntity.created(UriUtil.buildUserUri(userEntity.getId())).body(userResponseDTO);
  }

  /** {@inhericDoc} */
  @Override
  public ResponseEntity<UserResponseDTO> loginUser(String token) {

    if (JwtUtil.isTokenExpired(token)) {
      throw new TokenExpiredException("The token has expired.");
    }

    String email = JwtUtil.getEmailFromToken(token);

    UserEntity userEntity =
        userRepository
            .findByEmail(email)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        String.format("User with email %s has not found.", email)));

    userEntity.setLastLogin(LocalDateTime.now());
    userRepository.save(userEntity);

    return ResponseEntity.ok(
        UserMapper.INSTANCE.mapUserResponseDTO(userEntity, JwtUtil.generateToken(email)));
  }
}
