package de.thkoeln.syp.iot_etage.controller.dto;

import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class InstructionDto {
  private int mcuid;
  @NotEmpty
  private String action;
  @NotNull
  private Map<String, Object> payload;

  public int getMcuid() {
    return mcuid;
  }

  public void setMcuid(int mcuid) {
    this.mcuid = mcuid;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public Map<String, Object> getPayload() {
    return payload;
  }

  public void setPayload(Map<String, Object> payload) {
    this.payload = payload;
  }
}
