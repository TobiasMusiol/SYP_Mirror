package de.thkoeln.syp.iot_etage.controller.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import de.thkoeln.syp.iot_etage.auth.helper.AppRole;

public class UserDto {
  private long id;
  @NotEmpty
  private String name;
  @NotEmpty
  @Min(value=8)
  private String password;
  @NotEmpty
  private AppRole role;

  public UserDto(){}

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public AppRole getRole() {
    return role;
  }

  public void setRole(AppRole role) {
    this.role = role;
  }  
}
