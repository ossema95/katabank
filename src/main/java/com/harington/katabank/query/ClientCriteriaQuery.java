package com.harington.katabank.query;

import java.util.function.Consumer;

import com.harington.katabank.model.Client;

public class ClientCriteriaQuery {

	private static final String _1_1 = " 1 = 1 ";
	private static final String AND = " AND ";
	private static final String WHERE = " WHERE ";
	private StringBuilder whereStatementBuilder = new StringBuilder();
	private ClientCriteria clientCriteria = new ClientCriteria();

	public ClientCriteriaQuery where() {
		this.whereStatementBuilder.append(WHERE);
		return this;
	}
	
	public ClientCriteriaQuery and() {
		this.whereStatementBuilder.append(AND);
		return this;
	}
	
	public ClientCriteriaQuery email(Consumer<ClientCriteria> criteriaConsumer) {
		if(criteriaConsumer != null) {
			criteriaConsumer.accept(clientCriteria.getClientCriteria(Client.EMAIL,String.class));
			whereStatementBuilder.append(clientCriteria.getCriteriaString());
			return this;
		}
		whereStatementBuilder.append(_1_1);
		return this;
	}
	
	public ClientCriteriaQuery fullName(Consumer<ClientCriteria> criteriaConsumer) {
		if(criteriaConsumer != null) {
			criteriaConsumer.accept(clientCriteria.getClientCriteria(Client.FULL_NAME,String.class));
			whereStatementBuilder.append(clientCriteria.getCriteriaString());
			return this;
		}
		whereStatementBuilder.append(_1_1);
		return this;
	}
	
	public ClientCriteriaQuery id(Consumer<ClientCriteria> criteriaConsumer) {
		if(criteriaConsumer != null) {
			criteriaConsumer.accept(clientCriteria.getClientCriteria(Client.ID,Long.class));
			whereStatementBuilder.append(clientCriteria.getCriteriaString());
			return this;
		}
		whereStatementBuilder.append(_1_1);
		return this;
	}
	
	public String build() {
		return whereStatementBuilder.toString();
	}
}
