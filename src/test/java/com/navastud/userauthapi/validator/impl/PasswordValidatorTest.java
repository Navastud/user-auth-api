package com.navastud.userauthapi.validator.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.navastud.userauthapi.validator.PasswordValidation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PasswordValidatorTest {

  private final PasswordValidator passwordValidator = new PasswordValidator();
  @Mock private PasswordValidation passwordValidation;

  @ParameterizedTest
  @ValueSource(strings = {"a2asfGfdfdf4", "Qwerty-2023", "J4v4-2023"})
  void passwordValidatorShouldReturnTrueWhenFieldValueIsValidIsNotRequired(String password) {
    when(passwordValidation.required()).thenReturn(false);
    passwordValidator.initialize(passwordValidation);
    assertTrue(isValid(password));
  }

  @ParameterizedTest
  @ValueSource(strings = {"a2asfGGfdfdf4", "Qwerty_@!2023", "J4v4-2023-987654321"})
  void passwordValidatorShouldReturnFalseWhenFieldValueIsNotValidAndIsNotRequired(String password) {
    when(passwordValidation.required()).thenReturn(false);
    passwordValidator.initialize(passwordValidation);
    assertFalse(isValid(password));
  }

  @ParameterizedTest
  @NullSource
  void passwordValidatorShouldReturnTrueWhenFieldValueNullAndIsNotRequired(String password) {
    when(passwordValidation.required()).thenReturn(false);
    passwordValidator.initialize(passwordValidation);
    assertTrue(isValid(password));
  }

  @ParameterizedTest
  @ValueSource(strings = {"a2asfGfdfdf4", "Qwerty-2023", "J4v4-2023"})
  void passwordValidatorShouldReturnTrueWhenFieldValueIsValidAndIsRequired(String password) {
    when(passwordValidation.required()).thenReturn(true);
    passwordValidator.initialize(passwordValidation);
    assertTrue(isValid(password));
  }

  @ParameterizedTest
  @ValueSource(strings = {"a2asfGGfdfdf4", "Qwerty_@!2023", "J4v4-2023-987654321"})
  void passwordValidatorShouldReturnFalseWhenFieldValueIsNotValidAndIsRequired(String password) {
    when(passwordValidation.required()).thenReturn(true);
    passwordValidator.initialize(passwordValidation);
    assertFalse(isValid(password));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void passwordValidatorShouldReturnFalseWhenFieldValueNullAndEmptyAndIsRequired(String password) {
    when(passwordValidation.required()).thenReturn(true);
    passwordValidator.initialize(passwordValidation);
    assertFalse(isValid(password));
  }

  private boolean isValid(String value) {
    return passwordValidator.isValid(value, null);
  }
}
