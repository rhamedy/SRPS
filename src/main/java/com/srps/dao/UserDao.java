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

	public List<String> retrieveUsernames() {
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

		return jdbcTemplate.queryForObject(SQL, new Object[] { username },
				String.class);
	}

	public boolean isAdmin(String username) {
		String SQL = "SELECT count(*) FROM authentication.user u, authentication.role r, "
				+ "authentication.user_role ur WHERE u.username = ? AND u.username = ur.username "
				+ "AND ur.role_id = r.role_id AND r.role_name = ?";

		int count = jdbcTemplate.queryForInt(SQL, new Object[] { username,
				"Admin" });

		return (count == 0) ? false : true;
	}

	public User getUserByUsername(String username) {
		String SQL = "SELECT * FROM authentication.user WHERE username = ?";

		return jdbcTemplate.queryForObject(SQL, new Object[] { username },
				new RowMapper<User>() {
					public User mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						User user = new User();
						user.setDateOfBirth(rs.getDate("dob"));
						user.setDisabled(rs.getBoolean("disabled"));
						user.setEmail(rs.getString("username"));
						user.setFirstName(rs.getString("first_name"));
						user.setLastName(rs.getString("last_name"));

						return user;
					}
				});
	}

	public List<User> retrieveUsers() {
		String SQL = "SELECT * FROM authentication.user";

		List<User> users = jdbcTemplate.query(SQL, new RowMapper<User>() {
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setDateOfBirth(rs.getDate("dob"));
				user.setDisabled(rs.getBoolean("disabled"));
				user.setEmail(rs.getString("username"));
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));

				return user;
			}
		});

		return users;
	}

	public void updateUser(User user) {
		String SQL = "UPDATE authentication.user SET first_name = ?, last_name = ?, "
				+ "dob = ? WHERE username = ?";

		jdbcTemplate.update(
				SQL,
				new Object[] { user.getFirstName(), user.getLastName(),
						user.getDateOfBirth(), user.getEmail()});
	}
}
