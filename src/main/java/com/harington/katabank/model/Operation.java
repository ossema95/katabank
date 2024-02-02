package com.harington.katabank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Operation {
	
	public static final String AMOUNT = "amount";
	public static final String FROM_ACCOUNT = "fromAccount";
	public static final String TO_ACCOUNT = "toAccount";
	public static final String BALANCE = "balance";
	public static final String OPERATION_DATE = "operationDate";

	private BigDecimal amount;
	private Account fromAccount;
	private Account toAccount;
	private BigDecimal balance;
	private LocalDateTime operationDate;
	public Operation() {
		super();
	}
	public Operation(BigDecimal amount, Account fromAccount, Account toAccount, BigDecimal balance,
			LocalDateTime operationDate) {
		super();
		this.amount = amount;
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.balance = balance;
		this.operationDate = operationDate;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Account getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(Account fromAccount) {
		this.fromAccount = fromAccount;
	}
	public Account getToAccount() {
		return toAccount;
	}
	public void setToAccount(Account toAccount) {
		this.toAccount = toAccount;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public LocalDateTime getOperationDate() {
		return operationDate;
	}
	public void setOperationDate(LocalDateTime operationDate) {
		this.operationDate = operationDate;
	}
	
}
