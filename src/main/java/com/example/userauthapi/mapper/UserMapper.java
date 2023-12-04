package com.example.userauthapi.mapper;

import com.example.userauthapi.model.dto.PhoneDTO;
import com.example.userauthapi.model.dto.UserDTO;
import com.example.userauthapi.model.dto.UserResponseDTO;
import com.example.userauthapi.model.entity.PhoneEntity;
import com.example.userauthapi.model.entity.UserEntity;
import com.example.userauthapi.util.PasswordEncoderUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "created", ignore = true)
  @Mapping(target = "lastLogin", ignore = true)
  @Mapping(target = "isActive", ignore = true)
  @Mapping(target = "password", qualifiedByName = "EncodePassword")
  UserEntity mapUserEntity(UserDTO dto);

  @Mapping(target = "id", ignore = true)
  PhoneEntity mapPhoneEntity(PhoneDTO phoneDTO);

  UserResponseDTO mapUserResponseDTO(UserEntity userEntity, String token);

  @Named("EncodePassword")
  default String encodePassword(String currentPassword) {
    return PasswordEncoderUtil.encode(currentPassword);
  }
}
