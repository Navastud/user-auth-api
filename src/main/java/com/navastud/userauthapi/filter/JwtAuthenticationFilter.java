package com.navastud.userauthapi.filter;

import com.navastud.userauthapi.exception.TokenExpiredException;
import com.navastud.userauthapi.util.JwtUtil;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) {
    String token = request.getHeader("Authorization");

    if (token != null && token.startsWith("Bearer ")) {
      try {
        String email = JwtUtil.getEmailFromToken(token);

        if (email != null) {
          return authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(email, null));
        }
      } catch (SecurityException e) {
        throw new TokenExpiredException(e.getMessage());
      }
    }

    return null;
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult)
      throws IOException, ServletException {
    super.successfulAuthentication(request, response, chain, authResult);
    chain.doFilter(request, response);
  }
}
