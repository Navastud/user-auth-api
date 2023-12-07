package com.navastud.userauthapi.filter;

import com.navastud.userauthapi.util.JwtUtil;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String header = request.getHeader("Authorization");

    if (header != null && header.startsWith("Bearer ")) {
      UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    chain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (token != null) {
      String email = JwtUtil.getEmailFromToken(token);

      if (email != null) {
        return new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
      }
      return null;
    }
    return null;
  }
}
