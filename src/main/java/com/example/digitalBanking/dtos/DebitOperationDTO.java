package com.example.digitalBanking.dtos;

import lombok.Data;

@Data
public class DebitOperationDTO {
	private String accountId;
	private double amount;
	private String description;

}