package com.springmicroservices.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDetailsRequest {

    private long orderId;
    private long amount;
    private String referenceNum;
    private PaymentMode paymentMode;

}
