package de.thkoeln.syp.iot_etage.utils;

import de.thkoeln.syp.iot_etage.auth.helper.AppRole;
import de.thkoeln.syp.iot_etage.controller.dto.UserDto;
import de.thkoeln.syp.iot_etage.domain.entity.User;

public class UserMapper {
  
  public static  UserDto convertUserToUserDto(User user){
    UserDto userDto = new UserDto();

    userDto.setId(user.getId());
    userDto.setName(user.getName());
    userDto.setPassword(user.getPassword());
    userDto.setRole(user.getAppRole());

    return userDto;
  }

  public static User convertUserDtoToUser(UserDto userDto){
    User user = new User();
    
    user.setId(userDto.getId());
    user.setName(userDto.getName());
    user.setPassword(userDto.getPassword());
    user.setRole(
      userDto.getRole() == null && userDto.getRole().equals("") ? 
        AppRole.USER : userDto.getRole()
    );
    /*
    if (userDto.getRole() == null || userDto.getRole().equals("")){
      user.setRole(AppRole.USER);
    }
    else{
      user.setRole(userDto.getRole());
    }
    */

    return user; 
  }
}
