package com.springmicroservices.orderservice.external.client;

import com.springmicroservices.orderservice.model.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PRODUCT-SERVICE/product")
public interface ProductService {


    @PutMapping("/reduceQuantity/{id}")
    public ResponseEntity<Void> reduceQuantity(@PathVariable("id") long productId, @RequestParam long quantity );


    @GetMapping("/getProductById/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable long id);
}
