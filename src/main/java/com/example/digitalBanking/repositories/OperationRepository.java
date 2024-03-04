package com.example.digitalBanking.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.digitalBanking.entities.Operation;

public interface OperationRepository extends JpaRepository<Operation, Long> {

	List<Operation> findByBankAccountId(String accountId);
	Page<Operation> findByBankAccountIdOrderByDateOperationDesc(String accountId,Pageable pageable);}
