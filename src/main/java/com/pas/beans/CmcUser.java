package com.pas.beans;

import java.io.Serializable;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class CmcUser implements Serializable
{
	private static final long serialVersionUID = 131158039169073163L;
	
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String userRole;
	private String emailAddress;
	private String usageReason;
	private Integer totalSiteVisits;
	
	//not stored in DB
	private String fullName;
	
	@Override
	public String toString() 
	{
		String myString = "userName: " + userName + " first name: " + firstName + " last name: " + lastName;
		return myString;
	}

	@DynamoDbPartitionKey
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

	public Integer getTotalSiteVisits() {
		return totalSiteVisits;
	}

	public void setTotalSiteVisits(Integer totalSiteVisits) {
		this.totalSiteVisits = totalSiteVisits;
	}

	@DynamoDbIgnore
	public String getFullName() 
	{
		this.setFullName(this.getFirstName() + " " + this.getLastName());
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	
}
