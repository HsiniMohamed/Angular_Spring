package com.example.digitalBanking.dtos;

import java.util.Date;

import com.example.digitalBanking.enums.AccountStatus;

import lombok.Data;

@Data
public class BankAccountDTO {

	private String id;
	private Date dateCreation;
	private double balance;
	private String currency;
	private AccountStatus status;
	private String type;
	private CustomerDTO customerDTO;

}
