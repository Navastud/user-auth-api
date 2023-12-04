package com.example.userauthapi.util;

import com.example.userauthapi.exception.InternalServerErrorException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UriUtil {

  /**
   * Create a user URI to redirect to user details. Throws {@link URISyntaxException} if user ID is
   * null or invalid for creating a new URI.
   *
   * @param id User UUID
   * @return URI
   */
  public static URI buildUserUri(UUID id) {
    try {
      if (Objects.isNull(id)) {
        throw new URISyntaxException("ID", "User ID is null or invalid.");
      }

      return new URI("/users/" + id);
    } catch (URISyntaxException e) {
      throw new InternalServerErrorException(e.getMessage());
    }
  }
}
