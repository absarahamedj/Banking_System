package com.Bank.auth_service.repository;

import com.Bank.auth_service.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByMobileNumber(String mobileNumber);
}