package de.thkoeln.syp.iot_etage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.thkoeln.syp.iot_etage.auth.helper.AppRole;
import de.thkoeln.syp.iot_etage.controller.dto.UserDto;
import de.thkoeln.syp.iot_etage.service.AuthService;
import de.thkoeln.syp.iot_etage.utils.AppList;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(path="/auth")
public class AuthController {
  
  private final AuthService authService;

  @Autowired
  public AuthController(AuthService authService){
    this.authService = authService;
  }

  @PostMapping(
    path="/login",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @CrossOrigin
  @ResponseStatus(HttpStatus.ACCEPTED)
  public ResponseEntity<Map<String, Object>> checkUserCredentials(@RequestBody UserDto userDto) {
    String token = this.authService.loginUser(userDto);

    Map<String, Object> responseBody = new LinkedHashMap<>();
    responseBody.put("status", Integer.valueOf(HttpStatus.CREATED.value()));
    responseBody.put("user-token", token);
    
    return ResponseEntity.status(HttpStatus.OK).body(responseBody);
  }

  @PostMapping(
    path="/register",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @CrossOrigin
  public ResponseEntity<Map<String, Object>> createNewUser(@RequestBody UserDto userDto){

    String message = this.authService.registerUser(userDto);
    Integer status = Integer.valueOf(HttpStatus.CREATED.value());
    
    Map<String, Object> responseBody = new LinkedHashMap<>();
    responseBody.put("status", status);
    responseBody.put("message", message);

    return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
  }

  @GetMapping(
    path="/useritems",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @CrossOrigin
  public ResponseEntity<List<AppRole>> getUserItems(){
    List<AppRole> appRoles = new ArrayList<>();

    Stream.of(AppRole.values()).forEach(appRole -> {
      if (appRole != AppRole.ADMIN && appRole != AppRole.MCU){
        appRoles.add(appRole);
      }
    });

    return ResponseEntity.ok().body(appRoles);
  }

  @GetMapping(
    path="/apps",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @CrossOrigin
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<List<Map<String, String>>> getApps(){

    return ResponseEntity.ok().body(AppList.appList);
  }

  @PostMapping(
    path="/renewtoken",
    consumes=MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE 
  )
  @CrossOrigin
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Map<String, Object>> renewToken(){

    String token = this.authService.renewToken();

    Map<String, Object> responseBody = new LinkedHashMap<>();
    responseBody.put("status", Integer.valueOf(HttpStatus.CREATED.value()));
    responseBody.put("user-token", token);

    return ResponseEntity.status(HttpStatus.OK).body(responseBody);
  }
  
}
