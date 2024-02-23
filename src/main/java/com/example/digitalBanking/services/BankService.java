package com.example.digitalBanking.services;

import java.util.List;

import com.example.digitalBanking.entities.BankAccount;
import com.example.digitalBanking.entities.CurrentAccount;
import com.example.digitalBanking.entities.Customer;
import com.example.digitalBanking.entities.SavingAccount;
import com.example.digitalBanking.exceptions.BalanceNotSufficientException;
import com.example.digitalBanking.exceptions.BankAccountNotFoundException;
import com.example.digitalBanking.exceptions.CustomerNotFoundException;

public interface BankService {

	Customer saveCostumer(Customer customer);
	CurrentAccount saveCurrentBankAccount(double initialBalnce,long customerId,double overdraft) throws CustomerNotFoundException;
	SavingAccount saveSavingBankAccount(double initialBalnce,long customerId,double interetRate) throws CustomerNotFoundException;
	List<Customer> listCustomers();
	BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
	void debit(String accountId,String description,double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
	void credit(String accountId,String description,double amount) throws BankAccountNotFoundException;
	void transfert(String accountIdSoure,String accountIdDestination,double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
	List<BankAccount> bankAccountList();
	

	
	
}
