package com.example.digitalBanking.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.digitalBanking.dtos.AccountHistoryDTO;
import com.example.digitalBanking.dtos.BankAccountDTO;
import com.example.digitalBanking.dtos.CurrentBankingAccountDTO;
import com.example.digitalBanking.dtos.CustomerDTO;
import com.example.digitalBanking.dtos.OperationDTO;
import com.example.digitalBanking.dtos.SavingBankAccountDTO;
import com.example.digitalBanking.entities.BankAccount;
import com.example.digitalBanking.entities.CurrentAccount;
import com.example.digitalBanking.entities.Customer;
import com.example.digitalBanking.entities.Operation;
import com.example.digitalBanking.entities.SavingAccount;
import com.example.digitalBanking.enums.OperationType;
import com.example.digitalBanking.exceptions.BalanceNotSufficientException;
import com.example.digitalBanking.exceptions.BankAccountNotFoundException;
import com.example.digitalBanking.exceptions.CustomerNotFoundException;
import com.example.digitalBanking.mappers.BankAccountMapperImpl;
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
	private BankAccountMapperImpl dtoMapper;
	@Override
	public CustomerDTO saveCostumer(CustomerDTO customerDTO) {
		log.info("Saving new Custommer");
		Customer customer =dtoMapper.fromCustomerDTO(customerDTO);
		Customer savedCustomer= customerRepository.save(customer);
		return dtoMapper.fromCustomer(savedCustomer);
	}

	@Override
	public CurrentBankingAccountDTO saveCurrentBankAccount(double initialBalnce, long customerId, double overdraft)
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
		return dtoMapper.fromCurrentAccount(savedBankAccount);
	}



	@Override
	public SavingBankAccountDTO saveSavingBankAccount(double initialBalnce, long customerId, double interetRate)
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
		return dtoMapper.fromSavingAccount(savedBankAccount);
	}

	@Override
	public List<CustomerDTO> listCustomers() {
		
	 List<CustomerDTO> customerDTOs= customerRepository.findAll()
			 .stream()
			 .map(customer->dtoMapper.fromCustomer(customer))
			 .toList();
	 	return customerDTOs;
	}

	@Override
	public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
		BankAccount bankAccount = bankAccountRepository.findById(accountId)
				.orElseThrow(()->new BankAccountNotFoundException("BankAccount not foun"));
		if(bankAccount instanceof SavingAccount) {
			SavingAccount savingAccount =(SavingAccount) bankAccount;
			return dtoMapper.fromSavingAccount(savingAccount);
		}
		else {
			
			CurrentAccount currentAccount =(CurrentAccount) bankAccount;
			return dtoMapper.fromCurrentAccount(currentAccount);
		}
	}

	@Override
	public void debit(String accountId, String description, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
		
		BankAccount bankAccount = bankAccountRepository.findById(accountId)
				.orElseThrow(()->new BankAccountNotFoundException("BankAccount not foun"));
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
		BankAccount bankAccount = bankAccountRepository.findById(accountId)
				.orElseThrow(()->new BankAccountNotFoundException("BankAccount not foun"));
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
	public List<BankAccountDTO> bankAccountList(){
		
		List<BankAccount> bankAccounts= bankAccountRepository.findAll();
		List<BankAccountDTO> bankAccountDTOs =bankAccounts.stream().map(bankAccount->{
			if(bankAccount instanceof SavingAccount) {
				SavingAccount savingAccount =(SavingAccount) bankAccount;
				return dtoMapper.fromSavingAccount(savingAccount);
			}else {
				CurrentAccount currentAccount=(CurrentAccount) bankAccount;
				return dtoMapper.fromCurrentAccount(currentAccount);
			}
		}).toList();
		return bankAccountDTOs;
	}

	@Override
	public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
		
		Customer customer= customerRepository.findById(customerId)
		.orElseThrow(()-> new CustomerNotFoundException("Customer not found"));
		return dtoMapper.fromCustomer(customer);
	}

	@Override
	public CustomerDTO updateCostumer(CustomerDTO customerDTO) {
		Customer customer =dtoMapper.fromCustomerDTO(customerDTO);
		Customer savedCustomer= customerRepository.save(customer);
		return dtoMapper.fromCustomer(savedCustomer);
	}

	@Override
	public void deleteCustomer(Long customerId) {
		customerRepository.deleteById(customerId);
		
	}

	@Override
	public List<OperationDTO> accountHistory(String accountId) {
		
		List<Operation> operations= operationRepository.findByBankAccountId(accountId);
		return operations.stream().map(operation->dtoMapper.fromOperation(operation)).toList();
	}

	@Override
	public AccountHistoryDTO getAccountHistoryPages(String accountId, int page, int size) throws BankAccountNotFoundException {
		
	Page<Operation> operations=	operationRepository.findByBankAccountIdOrderByDateOperationDesc(accountId, PageRequest.of(page, size));
		BankAccount bankAccount =bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException(accountId));
		AccountHistoryDTO accountHistoryDTO =new AccountHistoryDTO();
		List<OperationDTO> operationDTOs= operations.getContent().stream().map(operation->dtoMapper.fromOperation(operation)).toList();
		accountHistoryDTO.setOperationDTOs(operationDTOs);
		accountHistoryDTO.setAccountId(bankAccount.getId());
		accountHistoryDTO.setBalance(bankAccount.getBalance());
		accountHistoryDTO.setCurrentPage(page);
		accountHistoryDTO.setPageSize(size);
		accountHistoryDTO.setTotalPages(operations.getTotalPages());
		return accountHistoryDTO;
	}

	@Override
	public List<CustomerDTO> searchCustomers(String keyword) {
		List<Customer> customers= customerRepository.searchCustomers(keyword);
		List<CustomerDTO>customerDTOs =customers.stream().map(cust->dtoMapper.fromCustomer(cust)).toList();
		return customerDTOs;
	}

	@Override
	public List<BankAccountDTO> bankAccountListByCustomer(Long customerId) throws CustomerNotFoundException {
		Customer customer =customerRepository.findById(customerId).orElse(null);

		if (customer==null) {
			throw new CustomerNotFoundException("Customer not found");
		}		List<BankAccount> bankAccounts= bankAccountRepository.findByCustomer(customer);
		List<BankAccountDTO> bankAccountDTOs =bankAccounts.stream().map(bankAccount->{
			if(bankAccount instanceof SavingAccount) {
				SavingAccount savingAccount =(SavingAccount) bankAccount;
				return dtoMapper.fromSavingAccount(savingAccount);
			}else {
				CurrentAccount currentAccount=(CurrentAccount) bankAccount;
				return dtoMapper.fromCurrentAccount(currentAccount);
			}
		}).toList();
		return bankAccountDTOs;
	}




}
