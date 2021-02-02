package de.thkoeln.syp.iot_etage.controller.dto;

import org.springframework.http.HttpStatus;

public class ResponseDto {
  private HttpStatus httpStatus;
  private String message;

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
