package de.thkoeln.syp.iot_etage;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import de.thkoeln.syp.iot_etage.auth.filter.JwtTokenFilter;
import de.thkoeln.syp.iot_etage.service.UserPrincipalDetailsService;

@Configuration
@EnableWebSecurity(debug = true) // In Prod 
@EnableGlobalMethodSecurity( // WEbSecurity statt Form-Based
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {
  
  @Autowired
  private UserPrincipalDetailsService userPrincipalDetailsService;

  private final JwtTokenFilter jwtTokenFilter;

  public ApplicationSecurityConfiguration(JwtTokenFilter jwtTokenFilter) {
    this.jwtTokenFilter = jwtTokenFilter;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .cors();
      
    http = http
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and();

    http = http
      .exceptionHandling()
      .authenticationEntryPoint(
          (request, response, ex) -> {
              response.sendError(
                  HttpServletResponse.SC_UNAUTHORIZED,
                  ex.getMessage()
              );
          }
      )
      .and();

    http
      .authorizeRequests()
      .antMatchers(HttpMethod.POST, "/events").hasIpAddress("192.16.1.20")
      .antMatchers(HttpMethod.POST, "/sensors").hasIpAddress("192.16.1.20")
      .antMatchers("/auth/**").permitAll()
      .anyRequest().authenticated();
    
    http.addFilterBefore(
      this.jwtTokenFilter,
      UsernamePasswordAuthenticationFilter.class
    );
    
    /*
    http.addFilterBefore(
      new JwtTokenFilter(new JwtTokenUtil()),
      UsernamePasswordAuthenticationFilter.class
    );
    */
  }
  
  // Funktion authenticationProvider() erstellen
  @Bean
  public DaoAuthenticationProvider authenticationProvider(){
      // Instanz von DaoAuthenticationProvider erstellen
      DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
      //als UserDetailSerivce den userDetailService
      provider.setUserDetailsService(userPrincipalDetailsService);
      provider.setPasswordEncoder(passwordEncoder());
      provider.setAuthoritiesMapper(authoritiesMapper());
      return provider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public GrantedAuthoritiesMapper authoritiesMapper(){
      SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
      authorityMapper.setConvertToUpperCase(true);
      authorityMapper.setDefaultAuthority("USER"); // per Default wird zu USER-ROLE erstellt
      return authorityMapper;
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
  
}
