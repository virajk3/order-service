package com.springmicroservices.orderservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoOrderFoundException extends RuntimeException{

    private String errorCode;
    public NoOrderFoundException(String message, String errorCode){
        super(message);
        this.errorCode = errorCode;
    }

}
