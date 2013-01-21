package com.srps.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.srps.dao.UserDao;
import com.srps.model.User;
import com.srps.util.MailClient;

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

	public boolean isUsernameUsed(String username) {
		return userDao.isUsernameUsed(username);
	}

	public void addAccountRequest(User user) {
		userDao.addAccountRequest(user);
	}

	public void sendConfirmationEmail(User user) {
		final String password = userDao
				.getPasswordByUsername("srps.system@gmail.com");
		
		System.out.println("password is : " + password);
		String subject = "Account Request Received.";
		String text = "You will receive a confirmation email with your username and "
				+ "password shortly after your account has been created. \n"
				+ "\n thank you for your interest in using our services. "
				+ "\n\n SRPS Team";

		MailClient.sendEmail(user.getEmail(), password, subject, text);
	}
}
