package com.harington.katabank.controller;

import java.math.BigDecimal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harington.katabank.model.Account;
import com.harington.katabank.model.Operation;
import com.harington.katabank.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	
	@Autowired
	AccountService accountService;

	@PutMapping("/{rib}/credit")
	public void creditAccount(@PathVariable(name = "rib", required = true) String rib,
			@RequestParam(name = "amount", required = true) BigDecimal amount) {
		accountService.deposit(rib, amount);
	}
	
	@PutMapping("/{rib}/debit")
	public void debitAccount(@PathVariable(name = "rib", required = true) String rib,
			@RequestParam(name = "amount", required = true) BigDecimal amount) {
		accountService.withdraw(rib, amount);
	}
	
	@GetMapping("/operations/{clientId}")
	public Collection<Operation> getClientOperations(@PathVariable(name = "clientId", required = true) Long clientId){
		return accountService.checkOperations(clientId);
	}
	
	@PostMapping("/save")
	public void addAccount(@RequestBody Account account) {
		accountService.insert(account);
	}
	
}
