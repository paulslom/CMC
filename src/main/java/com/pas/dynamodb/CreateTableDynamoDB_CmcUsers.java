package com.pas.dynamodb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pas.beans.CmcUser;

import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

public class CreateTableDynamoDB_CmcUsers
{
	private static Logger logger = LogManager.getLogger(CreateTableDynamoDB_CmcUsers.class);
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
        //Delete the table in DynamoDB Local if it exists.  If not, just catch the exception and move on
        try
        {
        	deleteTable(dynamoClients.getDynamoDbEnhancedClient());
        }
        catch (Exception e)
        {
        	logger.info(e.getMessage());
        }
        
        // Create a table in DynamoDB
        DynamoDbTable<CmcUser> CmcUserTable = createTable(dynamoClients.getDynamoDbEnhancedClient(), dynamoClients.getDdbClient());           

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
   
    private static DynamoDbTable<CmcUser> createTable(DynamoDbEnhancedClient ddbEnhancedClient, DynamoDbClient ddbClient) 
    {
        DynamoDbTable<CmcUser> teamTable = ddbEnhancedClient.table(AWS_TABLE_NAME, TableSchema.fromBean(CmcUser.class));
        
        // Create the DynamoDB table.  If it exists, it'll throw an exception
        
        try
        {
	        teamTable.createTable(builder -> builder.build());
        }
        catch (ResourceInUseException riue)
        {
        	logger.info("Table already exists! " + riue.getMessage());
        	throw riue;
        }
        // The 'dynamoDbClient' instance that's passed to the builder for the DynamoDbWaiter is the same instance
        // that was passed to the builder of the DynamoDbEnhancedClient instance used to create the 'customerDynamoDbTable'.
        // This means that the same Region that was configured on the standard 'dynamoDbClient' instance is used for all service clients.
        
        try (DynamoDbWaiter waiter = DynamoDbWaiter.builder().client(ddbClient).build()) // DynamoDbWaiter is Autocloseable
        { 
            ResponseOrException<DescribeTableResponse> response = waiter
                    .waitUntilTableExists(builder -> builder.tableName(AWS_TABLE_NAME).build())
                    .matched();
            
            response.response().orElseThrow(
                    () -> new RuntimeException(AWS_TABLE_NAME + " was not created."));
            
            // The actual error can be inspected in response.exception()
            logger.info(AWS_TABLE_NAME + " table was created.");
        }        
        
        return teamTable;
    }    
    
    private static void deleteTable(DynamoDbEnhancedClient ddbEnhancedClient) throws Exception
    {
    	DynamoDbTable<CmcUser> teamTable = ddbEnhancedClient.table(AWS_TABLE_NAME, TableSchema.fromBean(CmcUser.class));
       	teamTable.deleteTable();		
	}
}