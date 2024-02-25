package com.example.digitalBanking.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.example.digitalBanking.dtos.CurrentBankingAccountDTO;
import com.example.digitalBanking.dtos.CustomerDTO;
import com.example.digitalBanking.dtos.OperationDTO;
import com.example.digitalBanking.dtos.SavingBankAccountDTO;
import com.example.digitalBanking.entities.CurrentAccount;
import com.example.digitalBanking.entities.Customer;
import com.example.digitalBanking.entities.Operation;
import com.example.digitalBanking.entities.SavingAccount;

@Service
public class BankAccountMapperImpl {

	public CustomerDTO fromCustomer(Customer customer) {
		CustomerDTO customerDTO=new CustomerDTO();
		BeanUtils.copyProperties(customer, customerDTO);
		return customerDTO;
	}
	
	public Customer fromCustomerDTO(CustomerDTO customerDTO) {
		Customer customer =new Customer();
		BeanUtils.copyProperties(customerDTO, customer);
		return customer;
	}
	
	public SavingBankAccountDTO fromSavingAccount(SavingAccount savingAccount) {
		SavingBankAccountDTO savingBankAccountDTO =new SavingBankAccountDTO();
		BeanUtils.copyProperties(savingAccount, savingBankAccountDTO);
		savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());
		savingBankAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
		return savingBankAccountDTO;
	}
	
	public SavingAccount fromSavingAccountDTO(SavingBankAccountDTO savingBankAccountDTO) {
		SavingAccount savingAccount=new SavingAccount();
		BeanUtils.copyProperties(savingBankAccountDTO, savingAccount);
		savingAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
		return savingAccount;
		
	}
	public CurrentBankingAccountDTO fromCurrentAccount(CurrentAccount currentAccount) {
		CurrentBankingAccountDTO currentBankingAccountDTO=new CurrentBankingAccountDTO();
		BeanUtils.copyProperties(currentAccount, currentBankingAccountDTO);
		currentBankingAccountDTO.setType(currentAccount.getClass().getSimpleName());
		currentBankingAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
		return currentBankingAccountDTO;
	}
	public CurrentAccount fromCurrentAccountDTO(CurrentBankingAccountDTO currentBankingAccountDTO) {
		CurrentAccount currentAccount =new CurrentAccount();
		BeanUtils.copyProperties(currentBankingAccountDTO, currentAccount);
		currentAccount.setCustomer(fromCustomerDTO(currentBankingAccountDTO.getCustomerDTO()));
		return currentAccount;
	}
	
	public OperationDTO fromOperation(Operation operation ) {
		OperationDTO operationDTO =new OperationDTO();
		BeanUtils.copyProperties(operation, operationDTO);
		return operationDTO;
	}
}
