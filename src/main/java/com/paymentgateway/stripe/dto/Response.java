package com.paymentgateway.stripe.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private String intentID;
    private String clientSecret;
}