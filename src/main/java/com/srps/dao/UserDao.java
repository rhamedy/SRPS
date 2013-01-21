package com.srps.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.srps.model.User;

@Repository
public class UserDao {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<String> retrieveAllUsers() {
		String SQL = "SELECT username FROM authentication.user";

		List<String> users = jdbcTemplate.query(SQL, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String temp = rs.getString("username");
				return temp;
			}
		});

		return users;
	}

	public boolean isUsernameUsed(String username) {
		String SQL = "SELECT count(username) FROM authentication.user WHERE username = ?";

		int count = jdbcTemplate.queryForInt(SQL, new Object[] { username });

		return (count == 0) ? false : true;
	}

	public void addAccountRequest(User user) {
		String SQL = "INSERT INTO authentication.user (username,first_name,last_name) VALUES (?,?,?)";

		jdbcTemplate.update(
				SQL,
				new Object[] { user.getEmail(), user.getFirstName(),
						user.getLastName() });
	}
	
	public String getPasswordByUsername(String username) { 
		String SQL = "SELECT password FROM authentication.user where username = ?"; 
		
		return jdbcTemplate.queryForObject(SQL, new Object[]{username}, String.class);
	}
}
