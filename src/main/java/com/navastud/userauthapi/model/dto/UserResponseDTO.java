package com.navastud.userauthapi.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

  private UUID id;
  private LocalDateTime created;
  private LocalDateTime lastLogin;
  private String token;
  @Default private Boolean isActive = true;
  private String name;
  private String email;
  private String password;
  private List<PhoneDTO> phones;
}
