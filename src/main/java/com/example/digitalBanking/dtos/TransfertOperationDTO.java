package com.example.digitalBanking.dtos;

import lombok.Data;

@Data
public class TransfertOperationDTO {

	String accountIdSoure;
	String accountIdDestination;
	double amount;
}
