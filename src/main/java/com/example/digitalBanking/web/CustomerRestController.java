package com.example.digitalBanking.web;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.digitalBanking.dtos.CustomerDTO;
import com.example.digitalBanking.exceptions.CustomerNotFoundException;
import com.example.digitalBanking.services.BankService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {
	private BankService bankService;

	@GetMapping("/customers")
	public List<CustomerDTO> customers(){
		return bankService.listCustomers();
	}
	@GetMapping("/customers/search")
	public List<CustomerDTO> searchCustomers(@RequestParam(name="keyword", defaultValue = "") String keyword){
		return bankService.searchCustomers("%"+keyword+"%");
	}
	
	@GetMapping("/customers/{id}")
	public CustomerDTO getCustomer(@PathVariable(name="id") Long customerId) throws CustomerNotFoundException {
		log.info("get Customer DTO");
		return bankService.getCustomer(customerId);
	}
	
	@PostMapping("/customers")
	public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
		
		return bankService.saveCostumer(customerDTO);
	}
	
	@PutMapping("/customers/{customerId}")
	public CustomerDTO updateCustomer(@RequestBody CustomerDTO customerDTO, @PathVariable Long customerId) {
		customerDTO.setId(customerId);
		return bankService.updateCostumer(customerDTO);
	}
	
	@DeleteMapping("/customers/{customerId}")
	public void deleteCustomer( @PathVariable Long customerId) {
	 bankService.deleteCustomer(customerId);
	}
}
