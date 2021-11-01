package com.example.rest.dotpay.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentEntry {
    private String date;
    private String amount;
    private String currency;
    private String status;
    @JsonProperty("request_ip")
    private String requestIp;
    private String mcc;
    private String channel;
    @JsonProperty("merchant_id")
    private String merchantId;
}
