package com.example.digitalBanking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.digitalBanking.entities.Operation;

public interface OperationRepository extends JpaRepository<Operation, Long> {

}
