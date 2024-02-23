package com.example.digitalBanking.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.digitalBanking.entities.BankAccount;
import com.example.digitalBanking.entities.CurrentAccount;
import com.example.digitalBanking.entities.Customer;
import com.example.digitalBanking.entities.Operation;
import com.example.digitalBanking.entities.SavingAccount;
import com.example.digitalBanking.enums.OperationType;
import com.example.digitalBanking.exceptions.BalanceNotSufficientException;
import com.example.digitalBanking.exceptions.BankAccountNotFoundException;
import com.example.digitalBanking.exceptions.CustomerNotFoundException;
import com.example.digitalBanking.repositories.BankAccountRepository;
import com.example.digitalBanking.repositories.CustomerRepository;
import com.example.digitalBanking.repositories.OperationRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankServiceImpl implements BankService {

	private BankAccountRepository bankAccountRepository;
	private CustomerRepository customerRepository;
	private OperationRepository operationRepository;
	@Override
	public Customer saveCostumer(Customer customer) {
		log.info("Saving new Custommer");
		Customer savedCustomer= customerRepository.save(customer);
		return savedCustomer;
	}

	@Override
	public CurrentAccount saveCurrentBankAccount(double initialBalnce, long customerId, double overdraft)
			throws CustomerNotFoundException {
		
		Customer customer =customerRepository.findById(customerId).orElse(null);

		if (customer==null) {
			throw new CustomerNotFoundException("Customer not found");
		}
		CurrentAccount currentAccount = new CurrentAccount();
		currentAccount.setId(UUID.randomUUID().toString());
		currentAccount.setBalance(initialBalnce);
		currentAccount.setDateCreation(new Date());
		currentAccount.setOverdraft(overdraft);
		currentAccount.setCustomer(customer);
		CurrentAccount savedBankAccount= bankAccountRepository.save(currentAccount);
		return savedBankAccount;
	}



	@Override
	public SavingAccount saveSavingBankAccount(double initialBalnce, long customerId, double interetRate)
			throws CustomerNotFoundException {
		Customer customer =customerRepository.findById(customerId).orElse(null);

		if (customer==null) {
			throw new CustomerNotFoundException("Customer not found");
		}
		SavingAccount savingAccount = new SavingAccount();
		savingAccount.setId(UUID.randomUUID().toString());
		savingAccount.setBalance(initialBalnce);
		savingAccount.setDateCreation(new Date());
		savingAccount.setInterstRate(interetRate);
		savingAccount.setCustomer(customer);
		SavingAccount savedBankAccount= bankAccountRepository.save(savingAccount);
		return savedBankAccount;
	}

	@Override
	public List<Customer> listCustomers() {
		
		return customerRepository.findAll();
	}

	@Override
	public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
		BankAccount bankAccount = bankAccountRepository.findById(accountId)
				.orElseThrow(()->new BankAccountNotFoundException("BankAccount not foun"));
		
		return bankAccount;
	}

	@Override
	public void debit(String accountId, String description, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
		
		BankAccount bankAccount = getBankAccount(accountId);
		if ((bankAccount instanceof CurrentAccount && (bankAccount.getBalance()+((CurrentAccount) bankAccount).getOverdraft()<amount)
				)|| bankAccount instanceof SavingAccount &&(bankAccount.getBalance()<amount)) {
			throw new BalanceNotSufficientException("Balance not sufficient");
		}else {
			Operation operation=new Operation();
			operation.setDescription(description);
			operation.setAmount(amount);
			operation.setType(OperationType.DEBIT);
			operation.setDateOperation(new Date());
			operation.setBankAccount(bankAccount);
			operationRepository.save(operation);
			bankAccount.setBalance(bankAccount.getBalance()-amount);
			bankAccountRepository.save(bankAccount);
			
		}
		
	}

	@Override
	public void credit(String accountId, String description, double amount) throws BankAccountNotFoundException {
		BankAccount bankAccount = getBankAccount(accountId);
			Operation operation=new Operation();
			operation.setDescription(description);
			operation.setAmount(amount);
			operation.setType(OperationType.CREDIT);
			operation.setDateOperation(new Date());
			operation.setBankAccount(bankAccount);
			operationRepository.save(operation);
			bankAccount.setBalance(bankAccount.getBalance()+amount);
			bankAccountRepository.save(bankAccount);
	}

	@Override
	public void transfert(String accountIdSoure, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
		debit(accountIdSoure, "Transfer to "+accountIdDestination, amount);
		credit(accountIdDestination, "Transfer from "+accountIdSoure, amount);
	}
	@Override
	public List<BankAccount> bankAccountList(){
		return bankAccountRepository.findAll();
	}




}
