package com.harington.katabank.service;

import java.math.BigDecimal;
import java.util.Collection;

import com.harington.katabank.model.Account;
import com.harington.katabank.model.Operation;

public interface AccountService {

	void deposit(String rib, BigDecimal amount);

	void withdraw(String rib, BigDecimal amount);

	Collection<Operation> checkOperations(Long clientId);

	void insert(Account account);

}