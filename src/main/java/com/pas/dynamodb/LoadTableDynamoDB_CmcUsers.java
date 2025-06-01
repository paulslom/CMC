package com.pas.dynamodb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pas.beans.CmcUser;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class LoadTableDynamoDB_CmcUsers
{
	private static Logger logger = LogManager.getLogger(LoadTableDynamoDB_CmcUsers.class);
	private static String AWS_TABLE_NAME = "CmcUsers";
	
	public static void main(String[] args) throws Exception
    { 
    	logger.debug("**********  START of program ***********");   	
    	
    	 try 
         {
    		 DynamoClients dynamoClients = DynamoUtil.getDynamoClients();
    
    		 loadTable(dynamoClients);
	          	
			 logger.debug("**********  END of program ***********");
         }
    	 catch (Exception e)
    	 {
    		 logger.error("Exception: " + e.getMessage(), e);
    	 }
		System.exit(1);
	}
   
    private static void loadTable(DynamoClients dynamoClients) throws Exception 
    {
        //Grab a handle to the table in DynamoDB
        DynamoDbTable<CmcUser> CmcUserTable = dynamoClients.getDynamoDbEnhancedClient().table(AWS_TABLE_NAME, TableSchema.fromBean(CmcUser.class));

        // Insert data into the table
    	logger.info("Inserting data into the table:" + AWS_TABLE_NAME);
         
       	CmcUser cmcUser = new CmcUser();
       	cmcUser.setUserName("paulslom"); 
	  	cmcUser.setPassword("$2a$10$FZ9v1Gd7V3npcnhE5uCLJukoy5NZw1Mc2LXXumh5z0RmFNtY/Hgbe");
	  	cmcUser.setEmailAddress("paulslomkowski@yahoo.com");
	  	cmcUser.setUserRole("ADMIN");
	  	cmcUser.setFirstName("Paul");
	  	cmcUser.setLastName("Slomkowski");
	  	cmcUser.setUsageReason("UsageReason1");
	  	cmcUser.setTotalSiteVisits(7);
		CmcUserTable.putItem(cmcUser); 
		
		cmcUser = new CmcUser();
       	cmcUser.setUserName("tuser"); 
	  	cmcUser.setPassword("$2a$10$vfupVQRGcBfWLH3ZvPvoleEk/zegX8ikAUDglEW/swYTAJT82.p3m");
	  	cmcUser.setEmailAddress("tuserpw@google.com");
	  	cmcUser.setUserRole("USER");
	  	cmcUser.setFirstName("test");
	  	cmcUser.setLastName("testuser");
	  	cmcUser.setUsageReason("UsageReason2");
	  	cmcUser.setTotalSiteVisits(2);
		CmcUserTable.putItem(cmcUser);
	}
   
}