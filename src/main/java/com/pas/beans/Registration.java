package com.pas.beans;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("pc_Registration")
@ViewScoped
public class Registration implements Serializable
{
	private static final long serialVersionUID = 131158039169073163L;
	
	private static Logger logger = LogManager.getLogger(Registration.class);	
	
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String userRole;
	private String emailAddress;
	private String usageReason;
	
	//not stored in DB
	private String fullName;
	
	@Override
	public String toString() 
	{
		String myString = "userName: " + userName + " first name: " + firstName + " last name: " + lastName;
		return myString;
	}

	public void onloadRegistrationPage()
	{
		logger.info("loading new registration page");
		this.reset();
	}
	
	public void reset()
	{
		setUserName("");
		setPassword("");
		setFirstName("");
		setLastName("");
		setUserRole("");
		setEmailAddress("");
		setUsageReason("");
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getUsageReason() {
		return usageReason;
	}

	public void setUsageReason(String usageReason) {
		this.usageReason = usageReason;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	
	
	
}
