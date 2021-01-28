package de.thkoeln.syp.iot_etage.auth.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.qos.logback.core.spi.AppenderAttachable;


public class AuthoritiesOfGroups {

  // Configured Groups
  public static final AppAuthority[] ADMIN;
  public static final AppAuthority[] USER;
  public static final AppAuthority[] MCU;
  public static final AppAuthority[] OFFICE_WORKER_PERMISSIONS;
  public static final AppAuthority[] FACILITY_MANAGER_PERMISSIONS;
  public static Map<AppRole, List<AppAuthority>> groupPermissions;
  static {
    USER = new AppAuthority[]{
      AppAuthority.READ_AIR,
      AppAuthority.READ_AWNING,
      AppAuthority.READ_LIGHT,
      AppAuthority.READ_ROOM_STATUS,
      AppAuthority.READ_EVENTS,
      AppAuthority.READ_SENSORS
    };
    MCU = new AppAuthority[]{
      AppAuthority.EDIT_AIR,
      AppAuthority.EDIT_AWNING,
      AppAuthority.EDIT_LIGHT,
      AppAuthority.EDIT_EVENTS,
      AppAuthority.EDIT_SENSORS
    };
    OFFICE_WORKER_PERMISSIONS = new AppAuthority[]{
      AppAuthority.READ_LIGHT,
      AppAuthority.READ_AIR,
      AppAuthority.READ_AWNING,
      AppAuthority.READ_ROOM_STATUS,
      AppAuthority.EDIT_ROOM_STATUS_OFFICE_WORKER,
      AppAuthority.READ_EVENTS,
      AppAuthority.READ_SENSORS
    };
    FACILITY_MANAGER_PERMISSIONS = new AppAuthority[]{
      AppAuthority.READ_AIR,
      AppAuthority.EDIT_AIR,
      AppAuthority.READ_AWNING,
      AppAuthority.EDIT_AWNING,
      AppAuthority.READ_LIGHT,
      AppAuthority.EDIT_LIGHT,
      AppAuthority.READ_ROOM_STATUS,
      AppAuthority.EDIT_ROOM_STATUS_FM,
      AppAuthority.READ_EVENTS,
      AppAuthority.EDIT_EVENTS,
      AppAuthority.READ_SENSORS,
      AppAuthority.EDIT_SENSORS
    };
    ADMIN = AppAuthority.class.getEnumConstants();

    Map<AppRole, List<AppAuthority>> grPerm = new HashMap<>();
    grPerm = new HashMap<>();
    grPerm.put(AppRole.ADMIN, new ArrayList<>());
    grPerm.put(AppRole.USER, new ArrayList<>(Arrays.asList(AuthoritiesOfGroups.USER)));
    grPerm.put(AppRole.MCU, new ArrayList<>(Arrays.asList(AuthoritiesOfGroups.MCU)));
    grPerm.put(AppRole.FACILITY_MANAGER, new ArrayList<>(Arrays.asList(AuthoritiesOfGroups.FACILITY_MANAGER_PERMISSIONS)));
    grPerm.put(AppRole.OFFICE_WORKER, new ArrayList<>(Arrays.asList(AuthoritiesOfGroups.OFFICE_WORKER_PERMISSIONS)));
    grPerm.put(AppRole.ADMIN, new ArrayList<>(Arrays.asList(AuthoritiesOfGroups.ADMIN)));
    groupPermissions = Collections.unmodifiableMap(grPerm);
  }
  
  public static List<AppAuthority> getAuthoritiesByRoleName(AppRole role){
    return groupPermissions.get(role);
  }

  public static Map<AppRole, List<AppAuthority>> getRoleAuthorities(){
    return AuthoritiesOfGroups.groupPermissions;
  }
}
