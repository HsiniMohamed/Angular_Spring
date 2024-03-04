package com.example.digitalBanking.services;

import java.util.List;

import com.example.digitalBanking.dtos.AccountHistoryDTO;
import com.example.digitalBanking.dtos.BankAccountDTO;
import com.example.digitalBanking.dtos.CurrentBankingAccountDTO;
import com.example.digitalBanking.dtos.CustomerDTO;
import com.example.digitalBanking.dtos.OperationDTO;
import com.example.digitalBanking.dtos.SavingBankAccountDTO;
import com.example.digitalBanking.exceptions.BalanceNotSufficientException;
import com.example.digitalBanking.exceptions.BankAccountNotFoundException;
import com.example.digitalBanking.exceptions.CustomerNotFoundException;

public interface BankService {

	
	CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;
	CustomerDTO saveCostumer(CustomerDTO customerDTO);
	CustomerDTO updateCostumer(CustomerDTO customerDTO);
	void deleteCustomer(Long customerId);
	CurrentBankingAccountDTO saveCurrentBankAccount(double initialBalnce,long customerId,double overdraft) throws CustomerNotFoundException;
	SavingBankAccountDTO saveSavingBankAccount(double initialBalnce,long customerId,double interetRate) throws CustomerNotFoundException;
	List<CustomerDTO> listCustomers();
	BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
	void debit(String accountId,String description,double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
	void credit(String accountId,String description,double amount) throws BankAccountNotFoundException;
	void transfert(String accountIdSoure,String accountIdDestination,double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
	List<BankAccountDTO> bankAccountList();
	List<BankAccountDTO> bankAccountListByCustomer(Long customerId) throws CustomerNotFoundException;
	List<OperationDTO> accountHistory(String accountId);
	AccountHistoryDTO getAccountHistoryPages(String accountId, int page, int size) throws BankAccountNotFoundException;
	List<CustomerDTO> searchCustomers(String kyword);
	

	
	
}
