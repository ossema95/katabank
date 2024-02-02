package com.harington.katabank.model;

import java.math.BigDecimal;
import java.util.List;

public class Account {

	public static final String ASSOCIATED_CLIENT_KEY = "associatedClient";
	public static final String RIB = "rib";
	public static final String BALANCE = "balance";
	public static final String OPERATIONS = "operations";
	
	private Client associatedClient;
	private String rib;
	private BigDecimal balance;
	private List<Operation> operations;
	
	public Account() {
		super();
	}
	public Account(Client associatedClient, String rib, BigDecimal balance, List<Operation> operations) {
		super();
		this.associatedClient = associatedClient;
		this.rib = rib;
		this.balance = balance;
		this.operations = operations;
	}
	public Client getAssociatedClient() {
		return associatedClient;
	}
	public void setAssociatedClient(Client associatedClient) {
		this.associatedClient = associatedClient;
	}
	public String getRib() {
		return rib;
	}
	public void setRib(String rib) {
		this.rib = rib;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public List<Operation> getOperations() {
		return operations;
	}
	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}
	
}
