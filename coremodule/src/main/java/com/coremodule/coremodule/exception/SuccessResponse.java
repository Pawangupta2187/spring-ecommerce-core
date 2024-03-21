package com.coremodule.coremodule.exception;

import lombok.Data;

import java.util.Date;

@Data
public class SuccessResponse {
    private Date timestamp;
    private String message;


    public SuccessResponse(String message) {
        this.timestamp = new Date();
        this.message = message;
          }
}
