package com.harington.katabank.model;

import java.util.ArrayList;
import java.util.List;

public class Client {
	
	public static final String ID = "id";
	public static final String FULL_NAME = "fullName";
	public static final String EMAIL = "email";
	public static final String PASSWORD = "password";
	public static final String ROLES = "roles";
	public static final String ACCOUNTS = "accounts";

	private Long id;
	private String fullName;
	private String email;
	private String password;
	private String[] roles;
	private List<Account> accounts;
	
	public Client(){
	}

	public Client(Long id, String fullName, String email, String password, String[] roles, ArrayList<Account> accounts) {
		this.id = id;
		this.password = password;
		this.roles = roles;
		this.fullName = fullName;
		this.email = email;
		this.accounts = accounts;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public String getFullName() {
		return fullName;
	}

	public String getEmail() {
		return email;
	}

	public List<Account> getAccounts() {
		return accounts;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	
	
}
