package com.navastud.userauthapi.validator.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.navastud.userauthapi.validator.EmailValidation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class EmailValidatorTest {

  private final EmailValidator emailValidator = new EmailValidator();
  @Mock private EmailValidation emailValidation;

  @ParameterizedTest
  @ValueSource(strings = {"julio@testssw.cl", "test@google.com", "test-2@my-domain.uk"})
  void emailValidatorShouldReturnTrueWhenFieldValueIsValidIsNotRequired(String email) {
    when(emailValidation.required()).thenReturn(false);
    emailValidator.initialize(emailValidation);
    assertTrue(isValid(email));
  }

  @ParameterizedTest
  @ValueSource(strings = {"julio@testssw", "testgoogle.com", "test-2@"})
  void emailValidatorShouldReturnFalseWhenFieldValueIsNotValidAndIsNotRequired(String email) {
    when(emailValidation.required()).thenReturn(false);
    emailValidator.initialize(emailValidation);
    assertFalse(isValid(email));
  }

  @ParameterizedTest
  @NullSource
  void emailValidatorShouldReturnTrueWhenFieldValueNullAndIsNotRequired(String email) {
    when(emailValidation.required()).thenReturn(false);
    emailValidator.initialize(emailValidation);
    assertTrue(isValid(email));
  }

  @ParameterizedTest
  @ValueSource(strings = {"julio@testssw.cl", "test@google.com", "test-2@my-domain.uk"})
  void emailValidatorShouldReturnTrueWhenFieldValueIsValidAndIsRequired(String email) {
    when(emailValidation.required()).thenReturn(true);
    emailValidator.initialize(emailValidation);
    assertTrue(isValid(email));
  }

  @ParameterizedTest
  @ValueSource(strings = {"julio@testssw", "testgoogle.com", "test-2@"})
  void emailValidatorShouldReturnFalseWhenFieldValueIsNotValidAndIsRequired(String email) {
    when(emailValidation.required()).thenReturn(true);
    emailValidator.initialize(emailValidation);
    assertFalse(isValid(email));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void emailValidatorShouldReturnFalseWhenFieldValueNullAndEmptyAndIsRequired(String email) {
    when(emailValidation.required()).thenReturn(true);
    emailValidator.initialize(emailValidation);
    assertFalse(isValid(email));
  }

  private boolean isValid(String value) {
    return emailValidator.isValid(value, null);
  }
}
