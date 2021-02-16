package de.thkoeln.syp.iot_etage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import de.thkoeln.syp.iot_etage.auth.helper.AppRole;
import de.thkoeln.syp.iot_etage.controller.dto.UserDto;
import de.thkoeln.syp.iot_etage.exceptions.CustomException;
import de.thkoeln.syp.iot_etage.service.AuthService;

@Component
public class StartRunner implements CommandLineRunner {

  @Value("${application.admin.username}")
  private String username;
  @Value("${application.admin.userPassword}")
  private String userPassword;

  @Autowired
  AuthService authService;

  @Override
  public void run(String... args) throws Exception {

    String registerMessage = "";

    try{
    UserDto adminUser = new UserDto();
    adminUser.setId(0);
    adminUser.setName(this.username);
    adminUser.setPassword(this.userPassword);
    adminUser.setRole(AppRole.ADMIN);

    registerMessage = authService.registerUser(adminUser);
    System.out.println(registerMessage);
    }
    catch(CustomException e){
      System.out.println("User existiert bereits");
    }
  }
}
