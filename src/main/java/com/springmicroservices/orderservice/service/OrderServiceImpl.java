package com.springmicroservices.orderservice.service;

import com.springmicroservices.orderservice.entity.Order;
import com.springmicroservices.orderservice.exception.NoOrderFoundException;
import com.springmicroservices.orderservice.external.client.PaymentService;
import com.springmicroservices.orderservice.external.client.ProductService;
import com.springmicroservices.orderservice.model.*;
import com.springmicroservices.orderservice.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;


    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;


    @Autowired
    private RestTemplate restTemplate;

    
    @Override
    public long placeOrder(OrderRequest orderRequest) {
        log.info("Place order request: {}",orderRequest);

        //Product service - Block Products (Reduce the quantity)
        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());
        ResponseEntity<ProductResponse> product = productService.getProductById(orderRequest.getProductId());

        log.info("Creating order with status 'CREATED' ");
        // Order Entity -> Save the data with status order created
        Order order = Order.builder()
                .productId(orderRequest.getProductId())
                .productName(product.getBody().getProductName())
                .quantity(orderRequest.getQuantity())
                .amount(orderRequest.getTotalAmount())
                .paymentMode(orderRequest.getPaymentMode().name())
                .orderDate(Instant.now())
                .orderStatus("CREATED")
                .build();


        order = orderRepository.save(order);
        log.info("Order places successfully with Order id : {}",order.getId());
        //Payment Service -> Payments -> Success -> COMPLETE, ELSE

        log.info("Calling payment service API to complete the transaction");
        TransactionDetailsRequest transactionDetailsRequest 
                = TransactionDetailsRequest.builder()
                        .orderId(order.getId())
                        .amount(order.getAmount()   )
                        .paymentMode(orderRequest.getPaymentMode())
                        .referenceNum("REF"+order.getId())
                        .build();


        log.info("Order placed successfully with order id {}", order.getId());

       String orderStatus = null;
                try{
                    ResponseEntity<Long> longResponseEntity =    paymentService.doPayment(transactionDetailsRequest);
                    orderStatus = "PLACED";
                } catch (Exception e){
                    orderStatus = "PAYMENT_FAILED";
                }
                order.setOrderStatus(orderStatus);
                orderRepository.save(order);

        //CANCELLED



        return order.getId();
    }

    @Override
    public Optional<OrderResponse> getOrderById(Long orderId) {
        log.info("Looking for order details for order id : {}",orderId);

        Optional<Order> orderOptional = Optional.ofNullable(orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Order details not found for given order id",
                        "ORDER_NOT_FOUND")));


        if(orderOptional.isPresent()){
            Order order = orderOptional.get();
            log.info("Invoking Product Service to fetch the product for Id :order.productId",order.getProductId());

            ProductResponse productResponse =
                    restTemplate.getForObject("http://PRODUCT-SERVICE/product/getProductById/" + order.getProductId(), ProductResponse.class);


            log.info("Getting payment information from the payment service");

            PaymentResponse paymentResponse =
                    restTemplate.getForObject("http://PAYMENT-SERVICE/payment/getPaymentDetails/" + order.getId(), PaymentResponse.class);



            OrderResponse.ProductDetails productDetails =
                    OrderResponse.ProductDetails.builder()
                            .productName(productResponse.getProductName())
                            .productId(productResponse.getProductId())
                            .price(productResponse.getPrice())
                            .quantity(productResponse.getQuantity())
                            .build();

            OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails
                    .builder()
                    .orderId(order.getId())
                    .paymentDate(paymentResponse.getPaymentDate())
                    .status(paymentResponse.getStatus())
                    .paymentMode(paymentResponse.getPaymentMode())
                    .amount(paymentResponse.getAmount())
                    .paymentId(paymentResponse.getPaymentId())
                    .build();


            return  Optional.ofNullable(OrderResponse.builder()
                    .orderId(order.getId())
                    .orderStatus(order.getOrderStatus())
                    .orderDate(order.getOrderDate())
                    .amount(order.getAmount())
                    .paymentDetails(paymentDetails)
                    .productDetails(productDetails)
                    .build());
        }
        return Optional.empty();
    }

    @Override
    public ResponseEntity<List<OrderResponse>> getAllOrder() {
        log.info("Fetching all Orders ");
        List<Order> allOrders = orderRepository.findAll();

        List<OrderResponse> allOrderResponse = allOrders.stream().map(
                order -> OrderResponse.builder()
                        .orderId(order.getId())
                        .orderStatus(order.getOrderStatus())
                        .productName(order.getProductName())
                        .quantity(order.getQuantity())
                        .paymentMode(order.getPaymentMode())
                        .orderDate(order.getOrderDate())
                        .amount(order.getAmount())
                        .productDetails(OrderResponse.ProductDetails.builder().build())
                        .paymentDetails(OrderResponse.PaymentDetails.builder().build())
                        .build()

        ).collect(Collectors.toList());
        return new ResponseEntity<>(allOrderResponse, HttpStatus.OK);
    }
}