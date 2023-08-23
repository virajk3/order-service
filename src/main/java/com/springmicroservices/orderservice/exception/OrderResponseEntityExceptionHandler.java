package com.springmicroservices.orderservice.exception;

import com.springmicroservices.orderservice.external.client.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class OrderResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NoOrderFoundException.class})
    public ResponseEntity<ErrorResponse> handleNoOrderFoundException(NoOrderFoundException exception){
           return new ResponseEntity<>(
                   ErrorResponse.builder()
                           .errorMessage(exception.getMessage())
                           .errorCode(exception.getErrorCode())
                           .build(), HttpStatus.NOT_FOUND);
    }
}
