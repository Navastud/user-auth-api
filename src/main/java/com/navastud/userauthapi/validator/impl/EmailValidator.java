package com.navastud.userauthapi.validator.impl;

import com.navastud.userauthapi.util.RegexUtil;
import com.navastud.userauthapi.validator.EmailValidation;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;

public class EmailValidator implements ConstraintValidator<EmailValidation, String> {

  private boolean required;

  @Override
  public void initialize(EmailValidation constraintAnnotation) {
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
    return RegexUtil.EMAIL_REGEX.matcher(field).matches();
  }
}
