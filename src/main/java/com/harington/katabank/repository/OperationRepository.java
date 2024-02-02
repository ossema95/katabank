package com.harington.katabank.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.harington.katabank.model.Account;
import com.harington.katabank.model.Operation;

@Repository
public class OperationRepository {

	@Autowired
	DataSource ds;

	private static final String INSERT = "INSERT INTO OPERATION VALUES (?,?,?,?,?)";
	private static final String SELECT_ACCOUNTS_RIB = "SELECT rib from ACCOUNT where associatedClient = ?";
	private static final String SELECT_ALL = "SELECT amount,fromAccount,toAccount,balance,operationDate from OPERATION WHERE fromAccount in ("
			+ SELECT_ACCOUNTS_RIB + ") OR toAccount in (" + SELECT_ACCOUNTS_RIB + ")";

	public Collection<Operation> findAllByClient(Long clientId) {
		try (var conn = ds.getConnection(); var ps = conn.prepareStatement(SELECT_ALL)) {
			ps.setLong(1, clientId);
			ps.setLong(2, clientId);
			var rs = ps.executeQuery();
			Collection<Operation> result = new ArrayList<>();
			while (rs.next()) {
				var from = rs.getString(Operation.FROM_ACCOUNT) != null
						? new Account(null, rs.getString(Operation.FROM_ACCOUNT), null, null)
						: null;
				var to = rs.getString(Operation.TO_ACCOUNT) != null
						? new Account(null, rs.getString(Operation.TO_ACCOUNT), null, null)
						: null;
				var amount = rs.getBigDecimal(Operation.AMOUNT);
				Operation op = new Operation(amount, from, to, rs.getBigDecimal(Account.BALANCE),
						rs.getTimestamp(Operation.OPERATION_DATE).toLocalDateTime());
				result.add(op);
			}
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void insert(Operation operation, Connection conn) {
		Connection connection;
		var reusable = false;
		if (conn == null) {
			try {
				connection = ds.getConnection();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		} else {
			connection = conn;
			reusable = true;
		}
		try (var ps = connection.prepareStatement(INSERT)) {
			var fromAccount = operation.getFromAccount();
			var toAccount = operation.getToAccount();
			ps.setBigDecimal(1, operation.getAmount());
			ps.setString(2, fromAccount != null ? fromAccount.getRib() : null);
			ps.setString(3, toAccount != null ? toAccount.getRib() : null);
			ps.setBigDecimal(4, operation.getBalance());
			var operatinDate = operation.getOperationDate();
			ps.setTimestamp(5,
					operatinDate != null ? Timestamp.valueOf(operatinDate) : Timestamp.valueOf(LocalDateTime.now()));

			ps.executeUpdate();
		} catch (SQLException e) {
			if (!reusable) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					throw new RuntimeException(e);
				}
			}
			throw new RuntimeException(e);
		} finally {
			if (!reusable) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
				;
			}
		}
	}
}
