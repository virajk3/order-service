package com.springmicroservices.orderservice.config;

import com.springmicroservices.orderservice.external.client.decoder.CustomErrorDecoder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class FeignConfig {

    @Bean
    public CustomErrorDecoder getCustomErrorDecoder(){
        return new CustomErrorDecoder();
    }

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}
