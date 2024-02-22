package com.example.digitalBanking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.digitalBanking.entities.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

}
