package com.springmicroservices.orderservice.external.client.exception;

import com.springmicroservices.orderservice.external.client.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<ErrorResponse> handleProductServiceException(CustomException customException){
        ErrorResponse errorResponse = new ErrorResponse().builder()
                .errorCode(customException.getErrorCode())
                .errorMessage(customException.getMessage()).build();

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(customException.getStatus()));
    }
}
