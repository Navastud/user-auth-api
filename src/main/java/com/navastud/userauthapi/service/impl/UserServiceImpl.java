package com.navastud.userauthapi.service.impl;

import com.navastud.userauthapi.exception.NotFoundException;
import com.navastud.userauthapi.exception.TokenExpiredException;
import com.navastud.userauthapi.exception.UserAlreadyExistsException;
import com.navastud.userauthapi.mapper.UserMapper;
import com.navastud.userauthapi.model.dto.UserDTO;
import com.navastud.userauthapi.model.dto.UserResponseDTO;
import com.navastud.userauthapi.model.entity.UserEntity;
import com.navastud.userauthapi.repository.UserRepository;
import com.navastud.userauthapi.service.UserService;
import com.navastud.userauthapi.util.JwtUtil;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  /**
   * {@inhericDoc}
   */
  @Override
  public UserResponseDTO registerUser(UserDTO userDTO) {

    Optional<UserEntity> optUser = userRepository.findByEmail(userDTO.getEmail());

    if (optUser.isPresent()) {
      throw new UserAlreadyExistsException(
          String.format("User with email %s already exists.", userDTO.getEmail()));
    }

    UserEntity userEntity = UserMapper.INSTANCE.mapUserEntity(userDTO);
    userEntity.getPhones().forEach(phone -> phone.setUser(userEntity));

    String token = JwtUtil.generateToken(userEntity.getEmail());

    return UserMapper.INSTANCE.mapUserResponseDTO(userRepository.save(userEntity), token);
  }

  /**
   * {@inhericDoc}
   */
  @Override
  public UserResponseDTO loginUser(String token) {

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

    return UserMapper.INSTANCE.mapUserResponseDTO(userEntity, JwtUtil.generateToken(email));
  }
}
