package com.springmicroservices.orderservice.controller;

import com.springmicroservices.orderservice.model.OrderRequest;
import com.springmicroservices.orderservice.model.OrderResponse;
import com.springmicroservices.orderservice.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
@Log4j2
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {


    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<Long> placeOrder( @RequestBody OrderRequest orderRequest) {
        long orderId = orderService.placeOrder(orderRequest);
        log.info("Order id : {}",orderId);
        return new ResponseEntity<>(orderId, HttpStatus.OK);

    }

    @GetMapping("/getOrderDetails/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable("orderId") Long orderId){

        Optional<OrderResponse> orderOptional = orderService.getOrderById(orderId);
        OrderResponse orderResponse = orderOptional.get();
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }

    @GetMapping("/getAllOrders")
    public ResponseEntity<List<OrderResponse>> getAllOrders(){

        return orderService.getAllOrder();

    }


}
