package com.example.userauthapi.validator.impl;

import static com.example.userauthapi.util.RegexUtil.PASSWORD_REGEX;

import com.example.userauthapi.validator.PasswordValidation;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;

public class PasswordValidator implements ConstraintValidator<PasswordValidation, String> {

  private boolean required;

  @Override
  public void initialize(PasswordValidation constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
    this.required = constraintAnnotation.required();
  }

  @Override
  public boolean isValid(String field, ConstraintValidatorContext constraintValidatorContext) {
    if (required) {
      return Objects.nonNull(field) && validateField(field);
    } else {
      return Objects.isNull(field) || validateField(field);
    }
  }

  private boolean validateField(@NotNull String field) {
    if (field.isEmpty()) {
      return false;
    }
    return PASSWORD_REGEX.matcher(field).matches();
  }
}
