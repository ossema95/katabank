package com.harington.katabank.service;

import java.util.Collection;

import com.harington.katabank.model.Client;

public interface ClientService {

	Collection<Client> findAllClients();

	Collection<Client> searchForClient(String fullName, String email);

	void addClient(Client client);

}