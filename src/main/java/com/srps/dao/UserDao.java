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
				+ "dob = ?, disabled = ? WHERE username = ?";

		jdbcTemplate.update(
				SQL,
				new Object[] { user.getFirstName(), user.getLastName(),
						user.getDateOfBirth(), user.isDisabled(), user.getEmail()});
	}
	
	public String getUserRole(String email) { 
		String SQL = "SELECT r.role_name FROM authentication.user u, authentication.role r, " +
				"authentication.user_role ur WHERE u.username = ? AND u.username = ur.username" +
				" AND ur.role_id = r.role_id"; 
		
		return jdbcTemplate.queryForObject(SQL, new Object[]{email}, String.class);
	}
	
	public int getRoleIdByRoleName(String roleName) { 
		String SQL = "SELECT role_id FROM authentication.role WHERE role_name = ?"; 
		
		return jdbcTemplate.queryForInt(SQL, new Object[]{roleName});
	}
	
	public boolean userHasRoleAssigned(String email) { 
		String SQL = "SELECT COUNT(*) FROM authentication.user u, authentication.role r, " +
				"authentication.user_role ur WHERE u.username = ? AND u.username = ur.username" +
				" AND ur.role_id = r.role_id"; 
		
		int result = jdbcTemplate.queryForInt(SQL, new Object[]{email}); 
		
		return (result > 0)? true : false; 
	}
	
	public void updateUserRole(String username, int roleId) { 
		String SQL = "UPDATE authentication.user_role SET role_id = ? WHERE username = ?"; 
		
		jdbcTemplate.update(SQL, new Object[]{roleId, username}); 
	}
	
	public void assignUserRole(String username, int roleId) { 
		String SQL = "INSERT INTO authentication.user_role (username, role_id) values (?,?)"; 
		
		jdbcTemplate.update(SQL, new Object[]{username, roleId}); 
	}
	
	public void deleteUserRoleRelation(String username) { 
		String SQL = "DELETE FROM authentication.user_role WHERE username = ?";
		
		jdbcTemplate.update(SQL, new Object[]{username}); 
	}
	
	public void deleteUser(String username) { 
		String SQL = "DELETE FROM authentication.user WHERE username = ?"; 
		
		jdbcTemplate.update(SQL, new Object[]{username}); 
	}
	
	public void changePassword(String username, String newPassword) { 
		String SQL = "UPDATE authentication.user SET password = ? WHERE username = ?"; 
				
		jdbcTemplate.update(SQL, new Object[]{newPassword, username}); 
	}
	
	public boolean doesUserHavePassword(String username) { 
		String SQL = "SELECT password FROM authentication.user WHERE username = ?"; 
		
		String password = jdbcTemplate.queryForObject(SQL, new Object[]{username}, String.class); 
		
		System.out.println("password from dao : " + password);
		
		return (password == null || password.length() == 0) ? false : true; 
	}
}
