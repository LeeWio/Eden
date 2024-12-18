package com.megatronix.eden.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableJpaAuditing // Enable JPA auditing(automatic auditing for entities)
@EnableMethodSecurity // Enable method-level for annotations like @PreAuthorize and @secured
public class SecurityConfig {

  /**
   * Password Encoder Bean to encrypt and validate passwords.
   * 
   * @return a BCryptPasswordEncoder instance
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // BCrypt is widely recommended for password hashing due to its security
                                        // features
  }

  /**
   * AuthenticationManager Bean.
   * 
   * AuthenticationManager is used to authenticate user credentials.
   * 
   * @param authenticationConfiguration - Spring Security's configuration for
   *                                    authentication
   * @return the AuthenticationManager instance
   * @throws Exception if there is any error during authentication manager
   *                   creation
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  /**
   * SecurityContext Bean to manage the security context for the current user.
   * 
   * This context holds the authentication details (such as the current logged-in
   * user).
   * Setting the strategy to InheritableThreadLocal allows for propagating
   * security context in multi-threaded environments.
   * 
   * @return the SecurityContext instance
   */
  @Bean
  public SecurityContext securityContextHolder() {
    // Set the strategy for SecurityContext to propagate in multi-threaded
    // environments
    SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    return SecurityContextHolder.getContext(); // Return the current security context
  }

  /**
   * Security Filter Chain Bean.
   * 
   * This bean defines the security configuration for HTTP requests.
   * It controls which requests are authorized and which are not.
   * 
   * @param http - HttpSecurity to configure security settings
   * @return SecurityFilterChain that contains the security filters
   * @throws Exception if there is any error during filter chain creation
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .cors((cors) -> {
          UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
          CorsConfiguration configuration = new CorsConfiguration();
          configuration.addAllowedHeader("*");
          configuration.addAllowedMethod("*");
          configuration.addAllowedOriginPattern("*");
          source.registerCorsConfiguration("/**", configuration);
          cors.configurationSource(source);
        })
        .authorizeHttpRequests((authorize) -> authorize
            .requestMatchers(HttpMethod.POST, "auth/authenticate").permitAll()
            .anyRequest().permitAll());

    return http.build(); // Return the configured SecurityFilterChain
  }
}
