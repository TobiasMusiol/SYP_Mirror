package de.thkoeln.syp.iot_etage.mqtt;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.aspectj.apache.bcel.generic.Instruction;

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

  public InstructionResponseDto(){}

  public InstructionResponseDto(int mcuid, boolean success, String errorMessage){
    this.MCUID = mcuid;
    this.action = null;
    this.success = success;
    this.message = errorMessage;
  }
  
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
