package com.Bank.auth_service.service.impl;
import com.Bank.auth_service.security.JwtService;
import com.Bank.auth_service.dto.AuthResponse;
import com.Bank.auth_service.dto.LoginRequest;
import com.Bank.auth_service.dto.RegisterRequest;
import com.Bank.auth_service.entity.Customer;
import com.Bank.auth_service.exception.InvalidCredentialsException;
import com.Bank.auth_service.exception.ResourceAlreadyExistsException;
import com.Bank.auth_service.repository.CustomerRepository;
import com.Bank.auth_service.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(CustomerRepository customerRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    @Override
    public String register(RegisterRequest request) {

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        if (customerRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new ResourceAlreadyExistsException("Mobile number already exists");
        }

        Customer customer = Customer.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .mobileNumber(request.getMobileNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        customerRepository.save(customer);

        return "Customer Registered Successfully";
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid Email or Password"));

        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            throw new InvalidCredentialsException("Invalid Email or Password");
        }

        String token = jwtService.generateToken(customer.getEmail());

        return new AuthResponse("Customer Login Successful", token);
    }
}