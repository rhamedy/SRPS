package com.srps.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
