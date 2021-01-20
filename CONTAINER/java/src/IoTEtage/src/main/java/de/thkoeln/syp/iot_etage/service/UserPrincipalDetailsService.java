package de.thkoeln.syp.iot_etage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.thkoeln.syp.iot_etage.auth.UserPrincipal;
import de.thkoeln.syp.iot_etage.domain.entity.User;
import de.thkoeln.syp.iot_etage.domain.repository.UserRepository;

@Service
public class UserPrincipalDetailsService implements UserDetailsService{

  @Autowired
  private final UserRepository userRepository;

  //Konstruktor
  public UserPrincipalDetailsService(UserRepository userRepository){
    super();
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = this.userRepository.findUserByName(username);
    if (user == null){
      throw new UsernameNotFoundException("User nicht vorhanden " + username);
    }
    return new UserPrincipal(user);
  }

}
