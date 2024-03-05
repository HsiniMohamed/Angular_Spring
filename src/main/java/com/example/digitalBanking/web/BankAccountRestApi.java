package com.example.digitalBanking.web;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.digitalBanking.dtos.AccountHistoryDTO;
import com.example.digitalBanking.dtos.BankAccountDTO;
import com.example.digitalBanking.dtos.CreditOperationDTO;
import com.example.digitalBanking.dtos.DebitOperationDTO;
import com.example.digitalBanking.dtos.OperationDTO;
import com.example.digitalBanking.dtos.TransfertOperationDTO;
import com.example.digitalBanking.exceptions.BalanceNotSufficientException;
import com.example.digitalBanking.exceptions.BankAccountNotFoundException;
import com.example.digitalBanking.exceptions.CustomerNotFoundException;
import com.example.digitalBanking.services.BankService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class BankAccountRestApi {

	private BankService bankService;
	
	@GetMapping("/accounts/{accountId}")
	public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
		return bankService.getBankAccount(accountId);
	}
	
	@GetMapping("/accounts")
	public List<BankAccountDTO>  getBankAccountst() throws BankAccountNotFoundException {
		return bankService.bankAccountList();
	}
	@GetMapping("/accounts/{customerId}/customer")
	public List<BankAccountDTO>  getBankAccountstBycustomer(@PathVariable Long customerId) throws CustomerNotFoundException  {
		return bankService.bankAccountListByCustomer(customerId);
	}
	@GetMapping("/accounts/{accountId}/operations")
	public List<OperationDTO> getHistory(@PathVariable String accountId){
		return bankService.accountHistory(accountId);
	}
	@GetMapping("/accounts/{accountId}/pageOperations")
	public AccountHistoryDTO getAccountHistory(@PathVariable String accountId,
			@RequestParam(name = "page",defaultValue = "0") int page,
			@RequestParam(name = "size",defaultValue = "5") int size) throws BankAccountNotFoundException{
		return bankService.getAccountHistoryPages(accountId,page,size);
	}
	@PostMapping("accounts/credit")
	public void credit(@RequestBody CreditOperationDTO creditOperationDTO) throws BankAccountNotFoundException {
		bankService.credit(creditOperationDTO.getAccountId(), creditOperationDTO.getDescription(), creditOperationDTO.getAmount());
	}
	@PostMapping("accounts/debit")
	public void debit(@RequestBody DebitOperationDTO debitOperationDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
		bankService.debit(debitOperationDTO.getAccountId(), debitOperationDTO.getDescription(), debitOperationDTO.getAmount());
	}
	@PostMapping("accounts/transfer")
	public void transfert(@RequestBody TransfertOperationDTO transfertOperationDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
		bankService.transfert(transfertOperationDTO.getAccountIdSoure(), transfertOperationDTO.getAccountIdDestination(), transfertOperationDTO.getAmount());
	}
	
}
