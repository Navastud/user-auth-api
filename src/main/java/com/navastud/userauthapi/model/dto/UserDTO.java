package com.navastud.userauthapi.model.dto;

import com.navastud.userauthapi.validator.EmailValidation;
import com.navastud.userauthapi.validator.PasswordValidation;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

  @NotBlank(message = "The field 'name' cannot be empty or null.")
  private String name;

  @EmailValidation private String email;

  @PasswordValidation private String password;

  private List<PhoneDTO> phones;
}
