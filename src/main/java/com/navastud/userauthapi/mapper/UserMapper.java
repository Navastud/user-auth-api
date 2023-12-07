package com.navastud.userauthapi.mapper;

import com.navastud.userauthapi.model.dto.PhoneDTO;
import com.navastud.userauthapi.model.dto.UserDTO;
import com.navastud.userauthapi.model.dto.UserResponseDTO;
import com.navastud.userauthapi.model.entity.PhoneEntity;
import com.navastud.userauthapi.model.entity.UserEntity;
import com.navastud.userauthapi.util.PasswordEncoderUtil;
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
  @Mapping(target = "user", ignore = true)
  PhoneEntity mapPhoneEntity(PhoneDTO phoneDTO);

  UserResponseDTO mapUserResponseDTO(UserEntity userEntity, String token);

  @Named("EncodePassword")
  default String encodePassword(String currentPassword) {
    return PasswordEncoderUtil.encode(currentPassword);
  }
}
