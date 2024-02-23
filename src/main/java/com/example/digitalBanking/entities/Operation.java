package com.example.digitalBanking.entities;

import java.util.Date;

import com.example.digitalBanking.enums.OperationType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Operation {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Date dateOperation;
	private double amount;
	@Enumerated(EnumType.STRING)
	private OperationType type;
	private String description;
	@ManyToOne
	private BankAccount bankAccount;
}
