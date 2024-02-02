package com.harington.katabank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.harington.katabank.model.Account;
import com.harington.katabank.model.Operation;
import com.harington.katabank.repository.AccountRepository;
import com.harington.katabank.repository.OperationRepository;
import com.harington.katabank.service.impl.AccountServiceImpl;

public class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private DataSource dataSource;

    @Mock
    private OperationRepository operationRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDeposit() throws SQLException {
        // Arrange
        String rib = "123456";
        BigDecimal amount = new BigDecimal("100.00");
        when(accountRepository.getBalance(rib)).thenReturn(new BigDecimal("500.00"));
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);

        // Act
        accountService.deposit(rib, amount);

        // Assert
        verify(accountRepository, times(1)).update(any(), eq(connection));
        verify(operationRepository, times(1)).insert(any(), eq(connection));
        verify(connection, times(1)).commit();
        verify(connection, times(1)).close();
    }

    @Test
    public void testWithdraw() throws SQLException {
        // Arrange
        String rib = "123456";
        BigDecimal amount = new BigDecimal("50.00");
        when(accountRepository.getBalance(rib)).thenReturn(new BigDecimal("200.00"));
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);

        // Act
        accountService.withdraw(rib, amount);

        // Assert
        verify(accountRepository, times(1)).update(any(), eq(connection));
        verify(operationRepository, times(1)).insert(any(), eq(connection));
        verify(connection, times(1)).commit();
        verify(connection, times(1)).close();
    }

    @Test
    public void testCheckOperations() {
        // Arrange
        Long clientId = 1L;
        when(operationRepository.findAllByClient(clientId)).thenReturn(Arrays.asList(
                new Operation(new BigDecimal("100.00"), null, new Account(null, "123", new BigDecimal("500.00"), null), new BigDecimal("600.00"), null),
                new Operation(new BigDecimal("50.00"), new Account(null, "456", new BigDecimal("200.00"), null), null, new BigDecimal("150.00"), null)
        ));

        // Act
        Collection<Operation> result = accountService.checkOperations(clientId);

        // Assert
        assertEquals(2, result.size());
//        assertEquals(new BigDecimal("500.00"), result.iterator().next().getToAccount().getBalance());
//        assertEquals(new BigDecimal("200.00"), result.iterator().next().getFromAccount().getBalance());
    }

    @Test
    public void testInsert() {
        // Arrange
        Account account = new Account(null, "789", new BigDecimal("1000.00"), null);

        // Act
        accountService.insert(account);

        // Assert
        verify(accountRepository, times(1)).insert(account);
    }
}

