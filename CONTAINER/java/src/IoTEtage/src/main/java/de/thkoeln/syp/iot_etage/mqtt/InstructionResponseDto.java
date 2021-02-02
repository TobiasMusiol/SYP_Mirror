package de.thkoeln.syp.iot_etage.mqtt;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InstructionResponseDto {
  // 'MCUID': 1001,
  @JsonProperty("MCUID")
  private int MCUID;
  // 'action': 'setThreshold',
  private String action;
  // 'success' : 'true OR false',
  private boolean success;
  // 'message': 'Why it failed.'
  private String message;
  
  public int getMCUID() {
    return MCUID;
  }

  public void setMCUID(int MCUID) {
    this.MCUID = MCUID;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
