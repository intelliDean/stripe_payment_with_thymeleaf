package com.paymentgateway.stripe.user.controller;

import com.paymentgateway.stripe.user.dto.GeneralResponse;
import com.paymentgateway.stripe.user.dto.ProductRequest;
import com.paymentgateway.stripe.user.dto.RegRequest;
import com.paymentgateway.stripe.user.dto.StripeResponse;
import com.paymentgateway.stripe.user.service.UserService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Tag(name = "User Controller")
@RequestMapping("api/v1/stripe")
public class UserController {

    private final UserService userService;

    @PostMapping("register")
    @Operation(summary = "To register user")
    public ResponseEntity<GeneralResponse> registerUser(@RequestBody RegRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(request));
    }

    @GetMapping("retrieve/{userId}")
    @Operation(summary = "To retrieve a user")
    public ResponseEntity<StripeResponse> retrieve(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(
                    userService.retrieveCustomer(userId)
            );
        } catch (StripeException e) {
            throw new RuntimeException("User could not be found");
        }
    }
    @PostMapping("delete/{userId}")
    @Operation(summary = "To delete user")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(
                    userService.deleteUser(userId)
            );
        } catch (StripeException e) {
            throw new RuntimeException("User delete error");
        }
    }
    @PostMapping("create-product")
    @Operation(summary = "To delete user")
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest request) {
        try {
            return ResponseEntity.ok(
                    userService.createProduct(request)
            );
        } catch (StripeException e) {
            throw new RuntimeException("Error creating product");
        }
    }
}
