package com.paymentgateway.stripe.user.service;

import com.paymentgateway.stripe.user.dto.GeneralResponse;
import com.paymentgateway.stripe.user.dto.ProductRequest;
import com.paymentgateway.stripe.user.dto.RegRequest;
import com.paymentgateway.stripe.user.dto.StripeResponse;
import com.paymentgateway.stripe.user.model.User;
import com.paymentgateway.stripe.user.repository.UserRepository;
import com.paymentgateway.stripe.utils.MyPasswordEncoder;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Product;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.ProductCreateParams;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    @Transactional(propagation = Propagation.REQUIRED)
    public GeneralResponse registerUser(@Valid RegRequest request) {
        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        String email = request.getEmailAddress();
        String phoneNumber = request.getPhoneNumber();
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setName(firstName + " " + lastName)
                .setEmail(email)
                .setPhone(phoneNumber)
                .setDescription("This is a registering our user as customer on stripe for easy payment")
                .build();
        Customer customer;
        try {
            customer = Customer.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(e.getMessage());
        }
        User user = User.builder()
                .phoneNumber(phoneNumber)
                .emailAddress(email)
                .stripeId(customer.getId())
                .password(MyPasswordEncoder.encodes(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .registeredAt(getCreatedAt(customer.getCreated()))
                .build();
        userRepository.save(user);
        return GeneralResponse.builder()
                .message("User signed up successfully")
                .build();
    }

    public StripeResponse retrieveCustomer(Long userId) throws StripeException {
        User user = userRepository.findById(userId).get();
        Customer customer = Customer.retrieve(user.getStripeId());
        return StripeResponse.builder()
                .stripeId(customer.getId())
                .name(customer.getName())
                .balance(customer.getBalance())
                .phone(customer.getPhone())
                .description(customer.getDescription())
                .created(user.getRegisteredAt())
                .email(customer.getEmail())
                .build();
    }

    private LocalDateTime getCreatedAt(Long created) {
        Instant instant = Instant.ofEpochSecond(created);
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }



    public String deleteUser(Long userId) throws StripeException {
        User user = userRepository.findById(userId).get();
        String deletedCustomer = deleteCustomer(user.getStripeId());
        userRepository.delete(user);
        return deletedCustomer;
    }
    private String deleteCustomer(String customerId) throws StripeException {
        Customer customer = Customer.retrieve(customerId);
        Customer deletedCustomer = customer.delete();
        return deletedCustomer.toJson();
    }

    public String createProduct(ProductRequest request) throws StripeException {
        ProductCreateParams params =
                ProductCreateParams.builder()
                        .setName(request.getServiceName())
                        .setDescription(request.getServiceDescription())
                        .setActive(request.isActive())
                        .putMetadata("Service Provider ID", request.getServiceProviderStripeId())
                        .setDefaultPriceData(ProductCreateParams.DefaultPriceData.builder()
                                .setCurrency("AUD")
                                .setUnitAmount(request.getServicePricePerUnit() * 100)
                                .build())
                        .build();
        Product product = Product.create(params);
        return product.getId();
    }
}
