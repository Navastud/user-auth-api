package com.navastud.userauthapi.mapper;

import static com.navastud.userauthapi.fixture.UserFixture.buildPhoneDTO;
import static com.navastud.userauthapi.fixture.UserFixture.buildPhoneEntity;
import static com.navastud.userauthapi.fixture.UserFixture.buildUserDTO;
import static com.navastud.userauthapi.fixture.UserFixture.buildUserEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.navastud.userauthapi.model.dto.UserDTO;
import com.navastud.userauthapi.model.entity.PhoneEntity;
import com.navastud.userauthapi.model.entity.UserEntity;
import com.navastud.userauthapi.util.PasswordEncoderUtil;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class UserMapperTest {

  @Test
  void mapUserEntityTest() {
    UserEntity userEntity = buildUserEntity();
    UserDTO userDTO = buildUserDTO();
    UserEntity userEntityMap = UserMapper.INSTANCE.mapUserEntity(userDTO);
    assertThat(userEntityMap)
        .isNotNull()
        .hasFieldOrPropertyWithValue("name", userEntity.getName())
        .hasFieldOrPropertyWithValue("email", userEntity.getEmail())
        .hasFieldOrProperty("password")
        .hasFieldOrPropertyWithValue("isActive", userEntity.getIsActive())
        .hasFieldOrProperty("created")
        .hasFieldOrProperty("lastLogin")
        .hasFieldOrPropertyWithValue("id", null);
    assertThat(userEntityMap.getPhones())
        .isNotEmpty()
        .hasSize(1)
        .element(0)
        .hasFieldOrPropertyWithValue("id", null)
        .hasFieldOrPropertyWithValue("number", userEntity.getPhones().get(0).getNumber())
        .hasFieldOrPropertyWithValue("cityCode", userEntity.getPhones().get(0).getCityCode())
        .hasFieldOrPropertyWithValue("countryCode", userEntity.getPhones().get(0).getCountryCode());
    assertTrue(PasswordEncoderUtil.verify(userDTO.getPassword(), userEntityMap.getPassword()));
  }

  @Test
  void mapUserEntityWithoutPhonesTest() {
    UserEntity userEntity = buildUserEntity();
    UserDTO userDTO = buildUserDTO();
    userDTO.setPhones(Collections.emptyList());
    UserEntity userEntityMap = UserMapper.INSTANCE.mapUserEntity(userDTO);
    assertThat(userEntityMap)
        .isNotNull()
        .hasFieldOrPropertyWithValue("name", userEntity.getName())
        .hasFieldOrPropertyWithValue("email", userEntity.getEmail())
        .hasFieldOrProperty("password")
        .hasFieldOrPropertyWithValue("isActive", userEntity.getIsActive())
        .hasFieldOrProperty("created")
        .hasFieldOrProperty("lastLogin")
        .hasFieldOrPropertyWithValue("id", null);
    assertThat(userEntityMap.getPhones()).isEmpty();
    assertTrue(PasswordEncoderUtil.verify(userDTO.getPassword(), userEntityMap.getPassword()));
  }

  @Test
  void mapPhoneEntityTest() {
    PhoneEntity phoneEntity = buildPhoneEntity();
    assertThat(UserMapper.INSTANCE.mapPhoneEntity(buildPhoneDTO()))
        .isNotNull()
        .hasFieldOrPropertyWithValue("id", null)
        .hasFieldOrPropertyWithValue("number", phoneEntity.getNumber())
        .hasFieldOrPropertyWithValue("cityCode", phoneEntity.getCityCode())
        .hasFieldOrPropertyWithValue("countryCode", phoneEntity.getCountryCode());
  }

  @Test
  void mapUserResponseDTOTest() {
    PhoneEntity phoneEntity = buildPhoneEntity();
    assertThat(UserMapper.INSTANCE.mapPhoneEntity(buildPhoneDTO()))
        .isNotNull()
        .hasFieldOrPropertyWithValue("id", null)
        .hasFieldOrPropertyWithValue("number", phoneEntity.getNumber())
        .hasFieldOrPropertyWithValue("cityCode", phoneEntity.getCityCode())
        .hasFieldOrPropertyWithValue("countryCode", phoneEntity.getCountryCode());
  }
}
