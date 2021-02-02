package de.thkoeln.syp.iot_etage.auth.filter;

import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import de.thkoeln.syp.iot_etage.domain.entity.User;
import de.thkoeln.syp.iot_etage.exceptions.CustomException;
import de.thkoeln.syp.iot_etage.service.UserPrincipalDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil{
  @Value("${security.jwt.token.secret-key:secret-key}")
  private String secretKey;

  @Value("${security.jwt.token.expire-length:3600000}")
  private long validityInMilliseconds = 3600000; // 1h

  @Autowired
  private UserPrincipalDetailsService userPrincipalDetailsSrv;

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder()
      .encodeToString(this.secretKey.getBytes());
  }

  public String createToken(User user) {
    // set Header
    Header header = Jwts.header();
    header.setType("JWT");
    
    // set Claims (Permissions, Groups, User)
    Claims claims = Jwts.claims()
      .setSubject(user.getName());
    claims.put(
      "auth", 
      new SimpleGrantedAuthority(user.getAppRole().getAuthority())
    );

    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()//
        .setHeader((Map<String, Object>) header)
        .setClaims(claims)//
        .setIssuedAt(now)//
        .setExpiration(validity)//
        .signWith(SignatureAlgorithm.HS256, secretKey)//
        .compact();
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = this.userPrincipalDetailsSrv.loadUserByUsername(this.getUsername(token));

    return new UsernamePasswordAuthenticationToken(
      userDetails, 
      null, 
      userDetails.getAuthorities()
    );
  }

  public String getUsername(String token) {
    return Jwts.parser()
      .setSigningKey(secretKey)
      .parseClaimsJws(token)
      .getBody()
      .getSubject();
  }

  public String resolveToken(HttpServletRequest req) {
    String token = req.getHeader("Authorization");
    //
    Enumeration<String> headerNames = req.getHeaderNames();
    if (headerNames != null) {
      while (headerNames.hasMoreElements()) {
              System.out.println("Header: " + req.getHeader(headerNames.nextElement()));
      }
    }

    System.out.println(req.getHeaderNames());
    //


    if (token != null) {
      return token;
    }
    return null;
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token);
      System.out.println(Jwts.parser());
      System.out.println(Jwts.parser().setSigningKey(this.secretKey));
      System.out.println(Jwts.parser().setSigningKey(this.secretKey).parse(token));
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      throw new CustomException(
        "Expired or invalid JWT token",
         HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }
}
