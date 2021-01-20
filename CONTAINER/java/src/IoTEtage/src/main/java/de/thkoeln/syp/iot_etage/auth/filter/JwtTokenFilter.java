package de.thkoeln.syp.iot_etage.auth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import de.thkoeln.syp.iot_etage.exceptions.CustomException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

  private final JwtTokenUtil jwtTokenUtil;

  // Konstruktor
  public JwtTokenFilter(JwtTokenUtil jwtTokenUtil) {
    this.jwtTokenUtil = jwtTokenUtil;
  }

  // doFitlerInternal überschreiben
  @Override
  protected void doFilterInternal(
    HttpServletRequest httpServletRequest, 
    HttpServletResponse httpServletResponse, 
    FilterChain filterChain
  ) throws ServletException, IOException {

    String token = this.jwtTokenUtil.resolveToken(httpServletRequest);

    try {
      if (token != null && this.jwtTokenUtil.validateToken(token)) {
        // find User of the token
        Authentication auth = this.jwtTokenUtil.getAuthentication(token);
        SecurityContextHolder.getContext()
          .setAuthentication(auth);
      }
    } catch (CustomException ex) {
      //this is very important, since it guarantees the user is not authenticated at all
      SecurityContextHolder.clearContext();
      httpServletResponse.sendError(
        ex.getHttpStatus().value(), 
        ex.getMessage()
      );
      return;
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  } 

}