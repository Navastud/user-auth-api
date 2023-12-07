package com.navastud.userauthapi.config;

import com.navastud.userauthapi.filter.JwtAuthenticationFilter;
import com.navastud.userauthapi.filter.JwtAuthorizationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf()
        .disable()
        .authorizeRequests()
        .antMatchers(
            "/users/sign-up",
            "/user/{id}",
            "/actuator",
            "/actuator/health",
            "/actuator/health/**",
            "/swagger-ui/index.html",
            "/swagger-ui/**",
            "/h2-console/**",
            "/api-docs/**")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .exceptionHandling()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .formLogin()
        .disable()
        .addFilter(new JwtAuthenticationFilter(authenticationManager()))
        .addFilter(new JwtAuthorizationFilter(authenticationManager()))
        .headers().frameOptions().disable()
        .addHeaderWriter(
            new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
  }
}
