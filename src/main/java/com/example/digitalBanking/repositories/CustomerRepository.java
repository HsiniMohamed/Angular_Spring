package com.example.digitalBanking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.digitalBanking.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
