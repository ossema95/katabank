package com.harington.katabank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.harington.katabank.model.Client;
import com.harington.katabank.repository.ClientRepository;
import com.harington.katabank.service.impl.ClientServiceImpl;

public class ClientServiceImplTest {

    @InjectMocks
    private ClientServiceImpl clientService;

    @Mock
    private ClientRepository clientRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllClients() {
        // Arrange
        when(clientRepository.findAll()).thenReturn(Arrays.asList(
                new Client(1L, "test1", "test1@example.com", "test", new String[] {"CLIENT"}, new ArrayList<>()),
                new Client(2L, "test2", "test2@example.com", "test", new String[] {"CLIENT"}, new ArrayList<>())
        ));

        // Act
        Collection<Client> result = clientService.findAllClients();

        // Assert
        assertEquals(2, result.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void testSearchForClient() {
        // Arrange
        String fullName = "like(test)";
        String email = "like(test)";
        when(clientRepository.findAllWithCriteria(any())).thenReturn(Arrays.asList(
        		new Client(1L, "test1", "test1@example.com", "test", new String[] {"CLIENT"}, new ArrayList<>()),
                new Client(2L, "test2", "test2@example.com", "test", new String[] {"CLIENT"}, new ArrayList<>())
        ));

        // Act
        Collection<Client> result = clientService.searchForClient(fullName, email);

        // Assert
        assertEquals(2, result.size());
        verify(clientRepository, times(1)).findAllWithCriteria(any());
    }

    @Test
    public void testAddClient() {
        // Arrange
        Client client = new Client(1L, "test1", "test1@example.com", "test", new String[] {"CLIENT"}, new ArrayList<>());

        // Act
        clientService.addClient(client);

        // Assert
        verify(clientRepository, times(1)).insert(client);
    }
}
