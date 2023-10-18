package com.paymentgateway.stripe.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegRequest {

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    @NotBlank
    private String phoneNumber;

    @Email(message = "Please provide a valid email address")
    @NotNull
    @NotBlank
    private String emailAddress;

    @NotNull
    @NotBlank
    private String password;
}
