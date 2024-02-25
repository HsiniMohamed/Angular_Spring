package com.example.digitalBanking.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public  class SavingBankAccountDTO extends BankAccountDTO{

	
	private double interstRate;
	
}
