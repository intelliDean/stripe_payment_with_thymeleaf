package com.paymentgateway.stripe.controller;

import com.paymentgateway.stripe.dto.Request;
import com.paymentgateway.stripe.dto.Response;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentIntentController {
    @PostMapping("/create-payment-intent")
    public Response createPaymentIntent(@RequestBody Request request) throws StripeException {
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(request.getAmount() * 100L)
                        .putMetadata("Service", request.getProductName())
                        .setCurrency("aud")
                        .setReceiptEmail(request.getEmail())
                        .setCustomer("cus_OkPkioaxiwMbto")
                        .setDescription("This payment is for " + request.getProductName() + " service")
                        .putMetadata("Product Owner", "cus_OkQNo14TNYmNvO")
                        .putMetadata("Product ID", "prod_OlNaYJymscl2W3")
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams
                                        .AutomaticPaymentMethods
                                        .builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();
        PaymentIntent intent = PaymentIntent.create(params);
        return Response.builder()
                .intentID(intent.getId())
                .clientSecret(intent.getClientSecret())
                .build();
    }
}