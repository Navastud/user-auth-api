package com.navastud.userauthapi.util;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

class UriUtilTest {

  @Test
  @SneakyThrows
  void buildUserUriSuccess() {
    UUID userId = UUID.randomUUID();
    assertEquals(new URI("/users/" + userId), UriUtil.buildUserUri(userId));
  }

  @Test
  void buildUserUriWithNullIdShouldThrowException() {
    UUID userId = null;
    assertThatThrownBy(() -> UriUtil.buildUserUri(userId))
        .hasNoNullFieldsOrProperties()
        .hasMessage("User ID is null or invalid.: ID");
  }
}
