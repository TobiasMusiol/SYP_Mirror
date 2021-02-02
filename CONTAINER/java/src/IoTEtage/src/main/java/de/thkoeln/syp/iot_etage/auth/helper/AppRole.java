package de.thkoeln.syp.iot_etage.auth.helper;

import org.springframework.security.core.GrantedAuthority;

public enum AppRole implements GrantedAuthority {
  USER("User"),
  ADMIN("Admin"),
  MCU("MCU"),
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
}
 