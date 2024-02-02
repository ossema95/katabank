package com.harington.katabank.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.harington.katabank.model.Account;

@Repository
public class AccountRepository {

	@Autowired
	DataSource ds;

	private static final String INSERT = "INSERT INTO ACCOUNT VALUES (?,?,?)";
	private static final String UPDATE = "UPDATE ACCOUNT SET balance = ? where rib = ?";
	private static final String SELECT_BALANCE = "SELECT balance from ACCOUNT WHERE rib = ?";

	public void insert(Account account) {
		try (var conn = ds.getConnection(); var ps = conn.prepareStatement(INSERT)) {
			ps.setLong(1, account.getAssociatedClient().getId());
			ps.setBigDecimal(2, account.getBalance());
			ps.setString(3, account.getRib());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void update(Account account,Connection conn) {
		Connection connection;
		var reusable = false;
		if(conn == null) {
			try {
				connection= ds.getConnection();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}else {
			connection = conn;
			reusable =true;
		}
		try (var ps = connection.prepareStatement(UPDATE)) {
			ps.setBigDecimal(1, account.getBalance());
			ps.setString(2, account.getRib());
			ps.executeUpdate();
		} catch (SQLException e) {
			if(!reusable) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					throw new RuntimeException(e);
				}
			}
			throw new RuntimeException(e);
		}finally {
			if(!reusable) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				};
			}
		}
	}

	public BigDecimal getBalance(String rib) {
		try (var conn = ds.getConnection(); var ps = conn.prepareStatement(SELECT_BALANCE)) {
			ps.setString(1, rib);
			var rs = ps.executeQuery();
			BigDecimal result = null;
			if (rs.next()) {
				result = rs.getBigDecimal(Account.BALANCE);
			}
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
