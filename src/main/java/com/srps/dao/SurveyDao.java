package com.srps.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.srps.model.Survey;

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
		String SQL = "SELECT form_name from survey.blank_form f, survey.form_user fu"
				+ " where (fu.username = ? OR fu.username = ?) and fu.form_id = f.form_id ";

		List<String> formNames = jdbcTemplate.query(SQL, new Object[] {
				username, "everyone" }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("form_name");
			}
		});

		return formNames;
	}

	public boolean storeBlankForm(int formId, String filename) {
		String SQL = "INSERT INTO survey.blank_form (form_id,form_name) values(?,?)";

		int insert = jdbcTemplate
				.update(SQL, new Object[] { formId, filename });

		if (insert > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean relateFormToUser(int formId, String username) {
		String SQL = "INSERT INTO survey.form_user (form_id, username) values (?,?)";

		int insert = jdbcTemplate
				.update(SQL, new Object[] { formId, username });

		if (insert > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean saveSubmission(Survey survey) {
		String SQL = "INSERT INTO survey.submissions (id, username, form_name, " +
				"has_image, has_geopoint, has_audio, submission_date, private) " +
				"values (?,?,?,?,?,?,?,?)";
		
		int insert = jdbcTemplate.update(SQL, new Object[]{
				survey.getSubmissionId(), 
				survey.getUsername(), 
				survey.getFormName(), 
				survey.isImage(), 
				survey.isGeopoint(), 
				survey.isAudio(), 
				survey.getSubmissionDate(), 
				survey.isVisibility()
		}); 
		
		if (insert > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public List<Survey> getPublicSubmissions() { 
		String SQL = "SELECT * FROM survey.submissions WHERE private = FALSE"; 
		
		List<Survey> surveys = jdbcTemplate.query(SQL, new RowMapper<Survey>() { 
			public Survey mapRow(ResultSet rs, int rowNum) throws SQLException { 
				Survey temp = new Survey(); 
				
				temp.setSubmissionId(rs.getString("id")); 
				temp.setAudio(rs.getBoolean("has_audio")); 
				temp.setImage(rs.getBoolean("has_image")); 
				temp.setGeopoint(rs.getBoolean("has_geopoint")); 
				temp.setSubmissionDate(rs.getTimestamp("submission_date"));
				temp.setVisibility(rs.getBoolean("private")); 
				temp.setFormName(rs.getString("form_name")); 
				temp.setUsername(rs.getString("username")); 
				
				return temp;
			}
		}); 
		
		return surveys; 
	}
}
