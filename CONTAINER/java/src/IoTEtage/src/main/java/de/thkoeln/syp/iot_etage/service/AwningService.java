package de.thkoeln.syp.iot_etage.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* Service für Markisolettensteuerung  
*/

@Service
public class AwningService {
  
  private static final Logger logger = LoggerFactory.getLogger(AwningService.class);

  @Autowired
  public AwningService(){}

  /**
  * aktuellen Zustand lesen 
  */

  public void getCurrentAwningState(){

    return;
  }

  /**
  * Markisoletten Zustand initialisieren 
  */

  public void initAwningState(){

  }

  /**
  * aktuellen Zustand ändern 
  */
  public void changeAwningState(){

    return;
  }

  /**
  * Markissolettenzustand auf Default setzen 
  */
  public void setAwningStateToDefault(){

    return;
  }
}
