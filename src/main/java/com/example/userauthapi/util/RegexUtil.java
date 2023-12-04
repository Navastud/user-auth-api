package com.example.userauthapi.util;

import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexUtil {

  public static final String EMAIL_VALIDATION =
      "Error in field 'email' with value: '${validatedValue}'";
  public static final String PASSWORD_VALIDATION =
      "Error in field 'password' with value: '${validatedValue}'";

  public static Pattern EMAIL_REGEX = Pattern.compile("^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$");

  public static Pattern PASSWORD_REGEX =
      Pattern.compile("^(?=.*[A-Z])(?=.*\\d.*\\d)(?=.*[a-z]).{8,12}$");
}
