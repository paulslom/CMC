package com.pas.spring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//import com.pas.beans.CmcMain;

@Service
public class UserDetailsServiceImpl implements UserDetailsService 
{
	  private static Logger logger = LogManager.getLogger(UserDetailsServiceImpl.class);	
	  
	  //@Autowired
	  //private CmcMain cmcMain;
	  	  
	  @Override
	  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	  {
		  logger.info("User " + username + " attempting to log in");
	      return null; //this doesn't do anything yet
	  }
	  
}