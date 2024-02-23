package com.example.digitalBanking.exceptions;

public class BalanceNotSufficientException extends Exception {
	
	public BalanceNotSufficientException(String mesage) {
		super(mesage);
	}
}
