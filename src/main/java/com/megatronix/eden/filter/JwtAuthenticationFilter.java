package com.megatronix.eden.filter;

import java.io.IOException;
import jakarta.servlet.ServletException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.megatronix.eden.util.JwtUtil;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  @Resource
  private JwtUtil jwtUtil;
  @Resource
  private UserDetailsService userDetailsService;

  @Override
  public void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response,
      @Nonnull FilterChain filterChain) throws IOException, ServletException {
    String authorization = obtainAuthorization(request);
    if (StrUtil.isNotBlank(authorization) && jwtUtil.validateAuthorization(authorization)) {
      String email = jwtUtil.getEmailFromAuthorization(authorization);
      UserDetails userDetails = userDetailsService.loadUserByUsername(email);
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
          userDetails, null, userDetails.getAuthorities());

      usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
    filterChain.doFilter(request, response);
  }

  private String obtainAuthorization(HttpServletRequest request) {
    String bearerAuthorization = request.getHeader("authorization");
    if (StrUtil.isNotBlank(bearerAuthorization) && bearerAuthorization.startsWith("Bearer ")) {
      return bearerAuthorization.substring(7);
    }
    return bearerAuthorization;
  }
}
