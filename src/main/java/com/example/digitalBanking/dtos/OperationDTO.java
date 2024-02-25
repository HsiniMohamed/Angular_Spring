package com.example.digitalBanking.dtos;

import java.util.Date;

import com.example.digitalBanking.enums.OperationType;

import lombok.Data;




@Data
public class OperationDTO {

	private Long id;
	private Date dateOperation;
	private double amount;
	private OperationType type;
	private String description;
	
}
