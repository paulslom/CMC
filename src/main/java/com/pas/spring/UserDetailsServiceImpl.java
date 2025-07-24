package com.pas.spring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pas.beans.CmcUser;
import com.pas.dao.CmcUsersDAO;
import com.pas.dynamodb.DynamoClients;
import com.pas.dynamodb.DynamoUtil;

@Service
public class UserDetailsServiceImpl implements UserDetailsService 
{
	  private static Logger logger = LogManager.getLogger(UserDetailsServiceImpl.class);	
	  	  
	  @Override
	  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	  {
		  logger.info("User " + username + " attempting to log in");

          DynamoClients dynamoClients = null;
          CmcUser cmcuser = null;
          
          try
		  {
              dynamoClients = DynamoUtil.getDynamoClients();
              CmcUsersDAO cmcUsersDAO = new CmcUsersDAO(dynamoClients);
   			  cmcuser = cmcUsersDAO.getCmcUserFromDB(username);
		  }
          catch (Exception e)
          {
           	  logger.error(e.getMessage(), e);
          }           
     
		  UserBuilder builder = null;

		  if (cmcuser != null && cmcuser.getUserName() != null && cmcuser.getUserName().trim().length() > 0)
		  {
			  builder = org.springframework.security.core.userdetails.User.withUsername(username);
			  builder.password(cmcuser.getPassword());
			  builder.roles(cmcuser.getUserRole());

			  logger.info("User " + username + " successfully found on database as " + cmcuser.getUserName());
			  
			  return builder.build();
		  }
		  else
		  {
			  logger.info("User " + username + " not found on database.");
			  throw new UsernameNotFoundException("User not found.");
		  }
		        
	  }  
	  
}