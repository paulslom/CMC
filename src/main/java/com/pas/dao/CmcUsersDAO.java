package com.pas.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pas.beans.CmcUser;
import com.pas.dynamodb.DynamoClients;
import com.pas.util.Utils;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;

public class CmcUsersDAO implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(CmcUsersDAO.class);
	
	private Map<String,CmcUser> fullUserMap = new HashMap<>();
	private Map<String,CmcUser> adminUserMap = new HashMap<>();

	private List<CmcUser> fullCmcUsersList = new ArrayList<>();
	
	private static DynamoClients dynamoClients;
	private static DynamoDbTable<CmcUser> CmcUsersTable;
	private static final String AWS_TABLE_NAME = "CmcUsers";
	
	public CmcUsersDAO(DynamoClients dynamoClients2)
	{
	   try 
	   {
	       dynamoClients = dynamoClients2;
	       CmcUsersTable = dynamoClients.getDynamoDbEnhancedClient().table(AWS_TABLE_NAME, TableSchema.fromBean(CmcUser.class));
	   } 
	   catch (final Exception ex) 
	   {
	      logger.error("Got exception while initializing CmcUsersDAO. Ex = " + ex.getMessage(), ex);
	   }	   
	}

	public List<String> getAdminUserList()
	{
		List<String> adminUserList = new ArrayList<>();		
		this.getAdminUserMap().forEach((k, v) -> adminUserList.add(k));		
		return adminUserList;		
	}
	
	public List<CmcUser> readAllUsersFromDB() throws Exception
	{				
		Iterator<CmcUser> results = CmcUsersTable.scan().items().iterator();
            
        while (results.hasNext()) 
        {
            CmcUser gu = results.next();
            
            fullCmcUsersList.add(gu);
            
            if (this.getFullUserMap().containsKey(gu.getUserName()))
			{
				logger.error("duplicate user: " + gu.getUserName());
			}
			else
			{
				this.getFullUserMap().put(gu.getUserName(), gu);
								
				if (gu.getUserRole().contains("ADMIN"))
				{
					this.getAdminUserMap().put(gu.getUserName(), gu);
				}
			}
        }
          	
		logger.info("LoggedDBOperation: function-inquiry; table:CmcUsers; rows:" + this.getFullUserMap().size());
				
		//this loop only for debugging purposes
		/*
		for (Map.Entry<String, CmcUser> entry : this.getFullUserMap().entrySet()) 
		{
		    String key = entry.getKey();
		    CmcUser CmcUser = entry.getValue();

		    logger.info("Key = " + key + ", value = " + CmcUser.getUserName());
		}
		*/
		
		logger.info("exiting");
		
		return fullCmcUsersList;
	}
		
	public CmcUser getCmcUser(String username)
    {	    	
		CmcUser gu = this.getFullUserMap().get(username);			
    	return gu;
    }	
	
	public CmcUser getCmcUserFromDB(String username)
    {
		Key key = Key.builder().partitionValue(username).build();
		GetItemEnhancedRequest getItemEnhancedRequest = GetItemEnhancedRequest.builder().key(key).build();
		CmcUser retrievedCmcUser = CmcUsersTable.getItem(getItemEnhancedRequest);
		return retrievedCmcUser;
    }	
	
	public void deleteUser(String username) throws Exception
	{
		Key key = Key.builder().partitionValue(username).build();
		DeleteItemEnhancedRequest deleteItemEnhancedRequest = DeleteItemEnhancedRequest.builder().key(key).build();
		CmcUsersTable.deleteItem(deleteItemEnhancedRequest);
		
		logger.info("LoggedDBOperation: function-delete; table:CmcUsers; rows:1");
		
		CmcUser gu = new CmcUser();
		gu.setUserName(username);
		refreshListsAndMaps("delete", gu);	
	}
	
	public void addUser(CmcUser cmcUser) throws Exception
	{
		String encodedPW = Utils.getEncryptedPassword(cmcUser.getPassword());				
		cmcUser.setPassword(encodedPW);		
		
		PutItemEnhancedRequest<CmcUser> putItemEnhancedRequest = PutItemEnhancedRequest.builder(CmcUser.class).item(cmcUser).build();
		CmcUsersTable.putItem(putItemEnhancedRequest);
			
		logger.info("LoggedDBOperation: function-update; table:CmcUsers; rows:1");
					
		refreshListsAndMaps("add", cmcUser);	
	}
	
	public void updateUser(CmcUser cmcUser) throws Exception
	{
		deleteUser(cmcUser.getUserName());
		addUser(cmcUser);		
		refreshListsAndMaps("update", cmcUser);	
	}

	private void refreshListsAndMaps(String function, CmcUser CmcUser) 
	{
		if (function.equalsIgnoreCase("delete"))
		{
			this.getFullUserMap().remove(CmcUser.getUserName());	
		}
		else if (function.equalsIgnoreCase("add"))
		{
			this.getFullUserMap().put(CmcUser.getUserName(), CmcUser);	
		}
		else if (function.equalsIgnoreCase("update"))
		{
			this.getFullUserMap().remove(CmcUser.getUserName());	
			this.getFullUserMap().put(CmcUser.getUserName(), CmcUser);		
		}
		
	}
	
	public void updateRole(CmcUser gu)  throws Exception
	{
		updateUser(gu);		
		logger.debug("successfully reset role for user " + gu.getUserName());			
	}	

	public Map<String, CmcUser> getFullUserMap() 
	{
		return fullUserMap;
	}

	public void setFullUserMap(Map<String, CmcUser> fullUserMap) 
	{
		this.fullUserMap = fullUserMap;
	}

	public Map<String, CmcUser> getAdminUserMap() 
	{
		return adminUserMap;
	}

	public void setAdminUserMap(Map<String, CmcUser> adminUserMap) 
	{
		this.adminUserMap = adminUserMap;
	}

	public List<CmcUser> getFullCmcUsersList() {
		return fullCmcUsersList;
	}

	public void setFullCmcUsersList(List<CmcUser> fullCmcUsersList) {
		this.fullCmcUsersList = fullCmcUsersList;
	}
	
}
