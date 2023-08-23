package com.springmicroservices.orderservice.model;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {

    private long productId;

    private long quantity;

    private PaymentMode paymentMode;

    private long totalAmount;

}
