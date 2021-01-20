package de.thkoeln.syp.iot_etage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import de.thkoeln.syp.iot_etage.auth.helper.AppRole;
import de.thkoeln.syp.iot_etage.controller.dto.UserDto;
import de.thkoeln.syp.iot_etage.exceptions.CustomException;
import de.thkoeln.syp.iot_etage.service.AuthService;

@Component
public class StartRunner implements CommandLineRunner {

  @Autowired
  AuthService authService;

  @Override
  public void run(String... args) throws Exception {

    String registerMessage = "";

    try{
    UserDto adminUser = new UserDto();
    adminUser.setId(0);
    adminUser.setName("Admin");
    adminUser.setPassword("Admin");
    adminUser.setRole(AppRole.ADMIN);

    registerMessage = authService.registerUser(adminUser);
    System.out.println(registerMessage);
    }
    catch(CustomException e){
      System.out.println("User existiert bereits");
    }

    try{
      UserDto mcuUser = new UserDto();
      mcuUser.setId(0);
      mcuUser.setName("mcu");
      mcuUser.setPassword("mcu");
      mcuUser.setRole(AppRole.MCU);
      
      registerMessage = authService.registerUser(mcuUser);
      System.out.println(registerMessage);
    }catch(CustomException e){
      System.out.println("User existiert bereits");
    }
  }
}
