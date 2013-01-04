package com.srps.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SurveyDao {

	private JdbcTemplate jdbcTemplate; 	
	
	@Autowired
	public void setDataSource(DataSource dataSource) { 
		this.jdbcTemplate = new JdbcTemplate(dataSource); 
	}
	
	public int getNextFormId() { 
		String SQL = "SELECT COUNT(*) from survey.blank_form"; 
		return jdbcTemplate.queryForInt(SQL); 
	}
	
	public List<String> retrieveFormsByUsername(String username) { 
		String SQL = "SELECT form_name from survey.blank_form f, survey.form_user fu" +
				" where (fu.username = ? OR fu.username = ?) and fu.form_id = f.form_id "; 
		
		List<String> formNames = jdbcTemplate.query(SQL, new Object[]{username, "everyone"}, 
				new RowMapper<String>() {
				public String mapRow(ResultSet rs, int rowNum) throws SQLException { 
					return rs.getString("form_name"); 
				} 
		});
		
		return formNames; 
	}
	
	public boolean storeBlankForm(int formId, String filename) { 
		String SQL = "INSERT INTO survey.blank_form (form_id,form_name) values(?,?)";
		
		int insert = jdbcTemplate.update(SQL, new Object[]{formId, filename}); 
		
		if(insert > 0 ) { 
			return true; 
		} else { 
			return false; 
		}
	}
	
	public boolean relateFormToUser(int formId, String username) { 
		String SQL = "INSERT INTO survey.form_user (form_id, username) values (?,?)";
		
		int insert = jdbcTemplate.update(SQL, new Object[]{formId, username}); 
		
		if(insert > 0 ) { 
			return true; 
		} else { 
			return false;
		}
	}
}
