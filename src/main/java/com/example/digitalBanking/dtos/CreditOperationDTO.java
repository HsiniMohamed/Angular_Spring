package com.example.digitalBanking.dtos;

import lombok.Data;

@Data
public class CreditOperationDTO {

	private String accountId;
	private double amount;
	private String description;
}
