package com.harington.katabank.service.impl;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harington.katabank.model.Client;
import com.harington.katabank.query.ClientCriteria;
import com.harington.katabank.query.ClientCriteriaQuery;
import com.harington.katabank.repository.ClientRepository;
import com.harington.katabank.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {
	
	@Autowired
	ClientRepository clientRepository;
	
	private static final String REGEX = "(equal|notequal|like|in|notin|under|over)";
	private static final Pattern PATTERN_CRITERIA = Pattern.compile(REGEX);

	private final Matcher matcherCriteria = PATTERN_CRITERIA.matcher("");


	@Override
	public Collection<Client> findAllClients(){
		return clientRepository.findAll();
	}
	
	@Override
	public Collection<Client> searchForClient(String fullName, String email){
		var query = new ClientCriteriaQuery()
				.where()
				.email(getConsumer(email))
				.and()
				.fullName(getConsumer(fullName));
		return clientRepository.findAllWithCriteria(query.build());
	}
	
	private Consumer<ClientCriteria> getConsumer(String query){
		if(query == null) {
			return null;
		}
		matcherCriteria.reset(query);
		var criteriaExists = matcherCriteria.find();
		if (!criteriaExists) {
			throw new IllegalArgumentException(
					"could not parse criteria '" + query + "', criteria should match the regex " + REGEX);
		}
		var indexBegining = query.indexOf("(");
		var op = query.substring(0, indexBegining);
		var criteriaValues = query.substring(indexBegining+1, query.length()-1);
		switch(op) {
			case "equal":
				return e -> e.equalTo(criteriaValues);
			case "notequal":
				return e->e.notEqual(criteriaValues);
			case "like":
				return e->e.like(criteriaValues);
			case "in":
				return e->e.in(criteriaValues.split(","));
			case "notin":
				return e->e.notIn(criteriaValues.split(","));
			case "under":
				return e->e.under(criteriaValues);
			case "over":
				return e->e.over(criteriaValues);
		}
		return null;
	}
	
	@Override
	public void addClient(Client client) {
		clientRepository.insert(client);
	}
}
