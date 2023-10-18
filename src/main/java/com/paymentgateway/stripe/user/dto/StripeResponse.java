package com.paymentgateway.stripe.user.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StripeResponse {

    private String stripeId;
    private String name;
    private String email;
    private String phone;
    private String description;
    private Long balance;
    private LocalDateTime created;

}
