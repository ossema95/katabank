package com.harington.katabank.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harington.katabank.model.Account;
import com.harington.katabank.service.AccountService;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AccountController.class)
class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AccountService accountService;
	
    @Test
    public void testCreditAccount() throws Exception {
    	
    	doNothing().when(accountService).deposit(anyString(), any(BigDecimal.class));
        // Arrange
        String rib = "123456";
        BigDecimal amount = new BigDecimal("100.00");

        // Act
        mockMvc.perform(put("/accounts/{rib}/credit", rib)
                .param("amount", amount.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void testDebitAccount() throws Exception {

    	doNothing().when(accountService).withdraw(anyString(), any(BigDecimal.class));
        String rib = "123456";
        BigDecimal amount = new BigDecimal("50.00");

        mockMvc.perform(put("/accounts/{rib}/debit", rib)
                .param("amount", amount.toString()))
                .andExpect(status().isOk());
    }


    @Test
    public void testAddAccount() throws Exception {
    	
    	doNothing().when(accountService).insert(any(Account.class));
    	
        Account mockAccount = new Account();

        mockMvc.perform(post("/accounts/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockAccount)))
                .andExpect(status().isOk());
    }

    // Helper method to convert objects to JSON format
    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
