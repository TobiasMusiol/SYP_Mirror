package de.thkoeln.syp.iot_etage.auth.helper;

import com.fasterxml.jackson.annotation.JsonValue;

import org.springframework.security.core.GrantedAuthority;

public enum AppRole implements GrantedAuthority {
  USER("User"),
  ADMIN("Admin"),
  OFFICE_WORKER("Büromitarbeiter"),
  FACILITY_MANAGER("Facility Manager");
  
  public final String value;

  // Konstruktor
  private AppRole(String value)
  {
    this.value = value;
  }

  // Methoden
  public String getAuthority() {
    return name();
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  public class Names{
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    public static final String OFFICE_WORKER = "OFFICE_WORKER";
    public static final String FACILITY_MANAGER = "FACILITY_MANAGER";
  }
}
 