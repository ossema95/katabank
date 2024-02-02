package com.harington.katabank.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harington.katabank.model.Account;
import com.harington.katabank.model.Operation;
import com.harington.katabank.repository.AccountRepository;
import com.harington.katabank.repository.OperationRepository;
import com.harington.katabank.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	DataSource ds;
	
	@Autowired
	OperationRepository operationRepository;
	
	@Override
	public void deposit(String rib, BigDecimal amount){
		//need to in the same transaction
		var accountDepositBalance = accountRepository.getBalance(rib);
		var newBalance = accountDepositBalance.add(amount);
		var accountDeposit = new Account(null, rib, newBalance, null);
		Connection conn = null;
		try {
			conn = ds.getConnection();
			accountRepository.update(accountDeposit,conn);
			operationRepository.insert(new Operation(amount, null, accountDeposit,newBalance, null),conn);
			conn.commit();
		} catch (SQLException | RuntimeException e1) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			throw new RuntimeException(e1);
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public void withdraw(String rib, BigDecimal amount) {
		//need to run in the same transaction
		var accountWithdrawalBalance = accountRepository.getBalance(rib);
		var newBalance = accountWithdrawalBalance.add(amount.negate());
		var accountWithdrawal = new Account(null, rib, newBalance , null);
		Connection conn = null;
		try {
			conn = ds.getConnection();
			accountRepository.update(accountWithdrawal,conn);
			operationRepository.insert(new Operation(amount, accountWithdrawal, null,newBalance, null),conn);
			conn.commit();
		} catch (SQLException e1) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			throw new RuntimeException(e1);
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public Collection<Operation> checkOperations(Long clientId){
		var results = operationRepository.findAllByClient(clientId);
		return results.stream()
			.peek(e-> {
				if(e.getFromAccount() == null) {
					e.getToAccount().setBalance(e.getBalance().add(e.getAmount().negate()));
				}else {
					e.getFromAccount().setBalance(e.getBalance().add(e.getAmount()));
				}
			}).collect(Collectors.toCollection(ArrayList<Operation>::new));
		
	}
	
	@Override
	public void insert(Account account) {
		accountRepository.insert(account);
	}

}
