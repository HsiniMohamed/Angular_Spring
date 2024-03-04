package com.example.digitalBanking.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.digitalBanking.entities.BankAccount;
import com.example.digitalBanking.entities.Customer;


public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
	List<BankAccount> findByCustomer(Customer customer);

}
