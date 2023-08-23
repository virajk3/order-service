package com.springmicroservices.orderservice.external.client.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springmicroservices.orderservice.external.client.exception.CustomException;
import com.springmicroservices.orderservice.external.client.response.ErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {

        log.info("::{}",response.request().url());
        log.info("::{}",response.request().headers());

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ErrorResponse errorResponse = objectMapper.readValue(response.body().asInputStream(), ErrorResponse.class);
            throw new CustomException(errorResponse.getErrorMessage(),errorResponse.getErrorCode(),response.status());

        } catch (IOException e) {
            throw new CustomException("Internal Servier Error",
                    "INTERNAL_SERVER_ERROR",
                    500);
        }
    }
}
