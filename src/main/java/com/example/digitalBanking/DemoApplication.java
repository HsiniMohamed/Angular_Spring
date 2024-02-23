package com.example.digitalBanking;


import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.digitalBanking.entities.BankAccount;
import com.example.digitalBanking.entities.CurrentAccount;
import com.example.digitalBanking.entities.Customer;
import com.example.digitalBanking.entities.Operation;
import com.example.digitalBanking.entities.SavingAccount;
import com.example.digitalBanking.enums.AccountStatus;
import com.example.digitalBanking.enums.OperationType;
import com.example.digitalBanking.exceptions.BalanceNotSufficientException;
import com.example.digitalBanking.exceptions.BankAccountNotFoundException;
import com.example.digitalBanking.exceptions.CustomerNotFoundException;
import com.example.digitalBanking.repositories.BankAccountRepository;
import com.example.digitalBanking.repositories.CustomerRepository;
import com.example.digitalBanking.repositories.OperationRepository;
import com.example.digitalBanking.services.BankService;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(BankService bankService) {
		return args->{
			Stream.of("Youssef","Lili","Lina").forEach(name ->{
				Customer customer =new Customer();
				customer.setName(name);
				customer.setEmail(name+ "gmail.com");
				bankService.saveCostumer(customer);
				
				
			});
			bankService.listCustomers().forEach(customer->{
				try {
					bankService.saveCurrentBankAccount(Math.random()*999999, customer.getId(), 9000);
					bankService.saveSavingBankAccount(Math.random()*213333,customer.getId() , 5.5);
						
						List<BankAccount> bankAccounts= bankService.bankAccountList();
						for (BankAccount bankAccount : bankAccounts) {
							for (int i = 0; i < 10; i++) {
									
								bankService.credit(bankAccount.getId(), "credit", Math.random()*1200000);
								bankService.debit(bankAccount.getId(), "debit", Math.random()*80000);
								
							}
						}
						
					
				} catch (CustomerNotFoundException   e) {
					e.printStackTrace();
				} catch (BalanceNotSufficientException | BankAccountNotFoundException e) {
					e.printStackTrace();
				}
				
			
			});
		};
	}
	
	//@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							OperationRepository operationRepository) {
		return args -> {
			Stream.of("Hassan","Yassin","Aiche").forEach(name ->{
				Customer customer =new Customer();
				customer.setName(name);
				customer.setEmail(name+ "gmail.com");
				customerRepository.save(customer);
				
				
			});
			customerRepository.findAll().forEach(cust->{
				CurrentAccount currentAccount =new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random()*9000);
				currentAccount.setDateCreation(new Date());
				currentAccount.setCurrency("DT");
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCustomer(cust);
				currentAccount.setOverdraft(9000);
				bankAccountRepository.save(currentAccount);
				
				SavingAccount savingAccount =new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random()*9000);
				savingAccount.setDateCreation(new Date());
				savingAccount.setCurrency("DT");
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setCustomer(cust);
				savingAccount.setInterstRate(5.5);
				bankAccountRepository.save(savingAccount);
			});
			bankAccountRepository.findAll().forEach(acc->{
				for (int i = 0; i <10; i++) {
					
					Operation operation =new Operation();
					operation.setDateOperation(new Date());
					operation.setAmount(Math.random()*12000);
					operation.setType(Math.random()>0.5?OperationType.DEBIT:OperationType.CREDIT);
					operation.setBankAccount(acc);
					operationRepository.save(operation);
				}
			});
			
		};
	}

}
