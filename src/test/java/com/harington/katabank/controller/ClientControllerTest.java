package com.harington.katabank.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harington.katabank.model.Client;
import com.harington.katabank.service.ClientService;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ClientController.class)
public class ClientControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	ClientService clientService;
	
	@MockBean
	JobLauncher jobLuncher;
	
	@MockBean
	Job job;

	@Test
	public void testFind() throws Exception {
		Client clientTest = new Client();
		when(clientService.searchForClient(anyString(), anyString())).thenReturn(List.of(clientTest));
		// Arrange
		String fullName = "equal(fullNameTest)";
		String email = "equal(emailTest)";

		// Act
		mockMvc.perform(get("/clients/find").param("fullName", fullName).param("email", email))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testAddClient() throws Exception {
		Client clientTest = new Client();
		doNothing().when(clientService).addClient(clientTest);

		// Act
		mockMvc.perform(post("/clients/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(clientTest)))
                .andExpect(status().isOk());
	}
	
	@Test
	public void testStartJobBatchSuccess() throws Exception {
		JobExecution executionMock = mock(JobExecution.class);
		when(jobLuncher.run(any(), any())).thenReturn(executionMock);
		
		MockMultipartFile file = new MockMultipartFile("file", "clients.csv", "text/plain", "some csv data".getBytes());
		// Act
		
		mockMvc.perform(MockMvcRequestBuilders.multipart("/clients/batch/start").file(file))
                .andExpect(status().isOk());
	}
	
	@Test
	public void testStartJobBatchBadRequest() throws Exception {
		JobExecution executionMock = mock(JobExecution.class);
		when(jobLuncher.run(any(), any())).thenReturn(executionMock);
		
		MockMultipartFile file = new MockMultipartFile("notFile", "clients.csv", "text/plain", "some csv data".getBytes());
		// Act
		
		mockMvc.perform(MockMvcRequestBuilders.multipart("/clients/batch/start").file(file))
                .andExpect(status().isBadRequest());
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
