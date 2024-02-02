package com.harington.katabank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.harington.katabank.model.Account;
import com.harington.katabank.model.Client;
import com.harington.katabank.model.Operation;
import com.harington.katabank.repository.AccountRepository;
import com.harington.katabank.repository.ClientRepository;
import com.harington.katabank.repository.OperationRepository;
import com.harington.katabank.service.AccountService;
import com.harington.katabank.service.ClientService;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class KataBankApplicationTests {

	@Autowired
	private AccountService accountService;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private OperationRepository operationRepository;
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Test
	void contextLoads() {
	}

	@Order(1)
	@Test
	public void testDeposit() {
		String rib = "00223456789123456789";
		BigDecimal amount = new BigDecimal("100.00");


		accountService.deposit(rib, amount);

		BigDecimal accountBalance = accountRepository.getBalance(rib);
		assertEquals(new BigDecimal("3100.00"), accountBalance);

		Collection<Operation> operations = operationRepository.findAllByClient(2L);
		Operation desiredOperation = operations.iterator().next();
		assertEquals(amount, desiredOperation.getAmount());
		assertNotNull(desiredOperation.getToAccount());
	}
	
	@Order(2)
	@Test
	public void testWithdrw() {
		String rib = "00133456789123456789";
		BigDecimal amount = new BigDecimal("100.00");

		accountService.withdraw(rib, amount);

		BigDecimal accountBalance = accountRepository.getBalance(rib);
		assertEquals(new BigDecimal("1900.00"), accountBalance);

		Collection<Operation> operations = operationRepository.findAllByClient(1L);
		Operation desiredOperation = operations.iterator().next();
		assertEquals(amount, desiredOperation.getAmount());
		assertNotNull(desiredOperation.getFromAccount());
	}
	
	@Order(3)
	@Test
	public void testCheckOperations() {
		Collection<Operation> operationsOne = accountService.checkOperations(1L);
		Collection<Operation> operationsTwo = accountService.checkOperations(2L);
		assertEquals(1, operationsOne.size());	
		assertEquals(1, operationsTwo.size());	
	}
	
	@Order(4)
	@Test
	public void testInsertClient() {
		Client clientToInsert = new Client(99L, "testName","email@fake.com","easyPass", new String[] {"CLIENT"}, new ArrayList<>());
		clientService.addClient(clientToInsert);
		Client clientRetrived = clientRepository.findOne(99L);
		assertNotNull(clientRetrived);
	}
	
	@Order(5)
	@Test
	public void testInsertAccount() {
		Client client = new Client(99L, "testName","email@fake.com","easyPass", new String[] {"CLIENT"}, new ArrayList<>());
		BigDecimal balance = new BigDecimal("50.00");
		Account accountToInsert = new Account(client, "11133456789123456789", balance, new ArrayList<>());
		accountService.insert(accountToInsert);
		BigDecimal balanceRetrived = accountRepository.getBalance("11133456789123456789");
		assertEquals(balanceRetrived, balance);	
	}
	
	@Order(6)
	public void testFindClients() {
		Collection<Client> clients = clientService.searchForClient("equal(testName)", "like(ail@fak)");
		assertEquals(1, clients.size());
		assertEquals(99L, clients.iterator().next().getId());
		
	}

}
