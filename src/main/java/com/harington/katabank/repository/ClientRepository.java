package com.harington.katabank.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.harington.katabank.model.Account;
import com.harington.katabank.model.Client;
import com.harington.katabank.model.Operation;

@Repository
public class ClientRepository {

	private static final String VARCHAR = "VARCHAR";
	@Autowired
	DataSource ds;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	private static final String SELECT_ONE_EMAIL = "SELECT id,fullName,email,password,roles from CLIENT WHERE email = ?";
	private static final String SELECT_ALL = "SELECT id,fullName,email,rib,balance,password,roles from CLIENT left join ACCOUNT ON id=associatedClient ";
	private static final String SELECT_ONE_ID = "SELECT id,fullName,email,rib,balance,password,roles from CLIENT left join ACCOUNT ON id=associatedClient WHERE id = ?";
	private static final String INSERT = "INSERT INTO CLIENT VALUES (?,?,?,?,?)";

	public Client findByEmail(String email) {
		try (var conn = ds.getConnection(); var ps = conn.prepareStatement(SELECT_ONE_EMAIL)) {
			ps.setString(1, email);
			var rs = ps.executeQuery();
			Client client = null;
			if (rs.next()) {
				client = new Client(rs.getLong(Client.ID), rs.getString(Client.FULL_NAME), rs.getString(Client.EMAIL),
						rs.getString(Client.PASSWORD), rs.getObject(Client.ROLES, String[].class),
						new ArrayList<Account>());
			}
			return client;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void insert(Client client) {
		try (var conn = ds.getConnection(); var ps = conn.prepareStatement(INSERT)) {
			ps.setString(1, client.getEmail());
			ps.setString(2, passwordEncoder.encode(client.getPassword()));
			ps.setArray(3, conn.createArrayOf(VARCHAR, client.getRoles()));
			ps.setString(4, client.getFullName());
			ps.setLong(5, client.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Client findOne(Long id) {
		try (var conn = ds.getConnection(); var ps = conn.prepareStatement(SELECT_ONE_ID)) {
			ps.setLong(1, id);
			var rs = ps.executeQuery();
			Client client = null;
			if (rs.next()) {
				client = getClientFromResultSet(rs);
				var rib = rs.getString(Account.RIB);
				if (rib != null) {
					Account account = new Account(null, rib, rs.getBigDecimal(Account.BALANCE),
							new ArrayList<Operation>());
					client.getAccounts().add(account);
				}
				while (rs.next()) {
					Account account2 = new Account(null, rs.getString(Account.RIB), rs.getBigDecimal(Account.BALANCE),
							new ArrayList<Operation>());
					client.getAccounts().add(account2);
				}
			}
			return client;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Collection<Client> findAll() {
		try (var conn = ds.getConnection(); var ps = conn.prepareStatement(SELECT_ALL)) {
			var rs = ps.executeQuery();
			Map<Long, Client> clients = new HashMap<>();
			while (rs.next()) {
				Client cl = clients.computeIfAbsent(rs.getLong(Client.ID), ifAbsentFunction(rs));
				var rib = rs.getString(Account.RIB);
				if (rib != null) {
					Account account = new Account(null, rib, rs.getBigDecimal(Account.BALANCE),
							new ArrayList<Operation>());
					cl.getAccounts().add(account);
				}
			}
			return clients.values();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private Function<Long, Client> ifAbsentFunction(ResultSet rs) {
		return (Long k) -> {
			try {
				return getClientFromResultSet(rs);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		};
	}

	private Client getClientFromResultSet(ResultSet rs) throws SQLException {
		return new Client(rs.getLong(Client.ID), rs.getString(Client.FULL_NAME), rs.getString(Client.EMAIL),
				rs.getString(Client.PASSWORD), rs.getObject(Client.ROLES, String[].class), new ArrayList<Account>());
	}

	public Collection<Client> findAllWithCriteria(String query) {
		try (var conn = ds.getConnection(); var ps = conn.prepareStatement(SELECT_ALL + query)) {
			var rs = ps.executeQuery();
			Map<Long, Client> clients = new HashMap<>();
			while (rs.next()) {
				Client cl = clients.computeIfAbsent(rs.getLong(Client.ID), ifAbsentFunction(rs));
				var rib = rs.getString(Account.RIB);
				if (rib != null) {
					Account account = new Account(null, rib, rs.getBigDecimal(Account.BALANCE),
							new ArrayList<Operation>());
					cl.getAccounts().add(account);
				}
			}
			return clients.values();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
