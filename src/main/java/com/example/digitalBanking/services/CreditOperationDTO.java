package com.example.digitalBanking.services;

import lombok.Data;

@Data
public class CreditOperationDTO {

	private String accountId;
	private double amount;
	private String description;
}
