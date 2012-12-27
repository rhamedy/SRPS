package com.srps.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.srps.dao.UserDao;

@Repository
public class UserService {

	@Autowired
	private UserDao userDao;
	
	public List<String> retrieveAllUsers() { 
		return userDao.retrieveAllUsers();
	}
	
}
