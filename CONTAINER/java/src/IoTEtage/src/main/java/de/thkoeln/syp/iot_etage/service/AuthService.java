package de.thkoeln.syp.iot_etage.service;

import org.springframework.stereotype.Service;

import de.thkoeln.syp.iot_etage.controller.dto.UserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.thkoeln.syp.iot_etage.auth.UserPrincipal;
import de.thkoeln.syp.iot_etage.auth.filter.JwtTokenUtil;
import de.thkoeln.syp.iot_etage.domain.entity.User;
import de.thkoeln.syp.iot_etage.domain.repository.UserRepository;
import de.thkoeln.syp.iot_etage.exceptions.CustomException;
import de.thkoeln.syp.iot_etage.utils.UserMapper;

@Service
public class AuthService {

  @Autowired
  private final UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private AuthenticationManager authenticationManager;

  public AuthService(UserRepository userRepository){
    this.userRepository = userRepository;
  }

  /**
  * create User 
  * @return generated Token
  */
  public String registerUser(UserDto userDto){
    if (!userRepository.existsByName(userDto.getName())) {
      User user = UserMapper.convertUserDtoToUser(userDto);

      user.setPassword(passwordEncoder.encode(user.getPassword()));
      user = userRepository.save(user);

      return "Herzlichen Glückwunsch!!! Sie haben nun ein Account für IoT Etage Blau";
    } else {
      throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }
  /**
  * check credentials nad return jwt-Token
  */
  public String loginUser(UserDto userDto){
    try {
      Authentication authObj = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
          userDto.getName(), 
          userDto.getPassword()
        )
      );

      UserPrincipal userPrincipal = (UserPrincipal) authObj.getPrincipal();

      return jwtTokenUtil.createToken(userPrincipal.getUser());
    } catch (AuthenticationException e) {
      throw new CustomException(
        "Invalid username/password supplied", 
        HttpStatus.UNPROCESSABLE_ENTITY
      );
    }
  }

  /*
  public String logoutUser(){

  }
  */

}
