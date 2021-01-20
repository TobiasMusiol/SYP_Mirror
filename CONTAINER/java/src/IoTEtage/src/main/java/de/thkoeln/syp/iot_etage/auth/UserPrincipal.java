package de.thkoeln.syp.iot_etage.auth;

import de.thkoeln.syp.iot_etage.auth.helper.AppRole;
import de.thkoeln.syp.iot_etage.auth.helper.AuthoritiesOfGroups;
import de.thkoeln.syp.iot_etage.auth.helper.AppAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import de.thkoeln.syp.iot_etage.domain.entity.User;

public class UserPrincipal implements UserDetails {

  private User user;
  // Authorities, die anhand der AppRole des Users erstellt werden 
  private List<AppAuthority> userAuthorities;

  //Konstruktor
  public UserPrincipal(User user){
    super();

    this.user = user;
    this.setUserAuthorities(this.user.getAppRole());
  }

  // UserDetails Methoden
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
    
    this.userAuthorities.forEach(authority->{
        grantedAuthorities.add(new SimpleGrantedAuthority(authority.name()));
    });

    grantedAuthorities.add(new SimpleGrantedAuthority(this.user.getAppRole().name()));

    return grantedAuthorities;
    /*
    return Collections.singleton(
      new SimpleGrantedAuthority(this.user.getAppRole().getAuthority())
    );
    */
  }

  @Override
  public String getPassword() {
    // TODO Auto-generated method stub
    return this.user.getPassword();
  }

  @Override
  public String getUsername() {
    // TODO Auto-generated method stub
    return this.user.getName();
  }

  @Override
  public boolean isAccountNonExpired() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public boolean isEnabled() {
    // TODO Auto-generated method stub
    return true;
  }

  public void setUserAuthorities(AppRole role){
    this.userAuthorities = AuthoritiesOfGroups.getAuthoritiesByRoleName(role);
  }

  public User getUser(){
    return this.user;
  }
}
