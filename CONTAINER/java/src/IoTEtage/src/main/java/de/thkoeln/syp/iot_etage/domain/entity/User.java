package de.thkoeln.syp.iot_etage.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import de.thkoeln.syp.iot_etage.auth.helper.AppRole;

@Entity
@Table(name="users")
public class User {
  @Id
  @Column(name="id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name="name", nullable=false, unique=true)
  private String name;

  @Column(name="password")
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name="role")
  private AppRole appRole;

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

  public AppRole getAppRole() {
    return appRole;
  }

  public void setRole(AppRole role) {
    this.appRole = role;
  }
  
}
