package com.srps.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.srps.dao.UserDao;

@Repository
public class UserServices {

	@Autowired
	private UserDao userDao;
	
	public List<String> retrieveAllUsers() { 
		return userDao.retrieveAllUsers();
	}
	
	public String getCurrentUsername() { 
		return SecurityContextHolder.getContext().getAuthentication().getName(); 
	}
	
}
