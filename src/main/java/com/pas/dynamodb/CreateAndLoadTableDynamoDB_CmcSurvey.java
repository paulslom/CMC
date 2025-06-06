package com.pas.dynamodb;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.pas.beans.CmcSurvey;

import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

public class CreateAndLoadTableDynamoDB_CmcSurvey
{
	private static Logger logger = LogManager.getLogger(CreateAndLoadTableDynamoDB_CmcSurvey.class);
	private static String AWS_TABLE_NAME = "cmcSurveys";
	
    public void loadTable(DynamoClients dynamoClients, InputStream inputStream) throws Exception 
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
        
        // Create a table in DynamoDB Local
        DynamoDbTable<CmcSurvey> cmcSurveyTable = createTable(dynamoClients.getDynamoDbEnhancedClient(), dynamoClients.getDdbClient());           

        loadTableData(cmcSurveyTable, inputStream);        
    }
    
    private static void deleteTable(DynamoDbEnhancedClient ddbEnhancedClient) throws Exception
    {
    	DynamoDbTable<CmcSurvey> cmcSurveysTable = ddbEnhancedClient.table(AWS_TABLE_NAME, TableSchema.fromBean(CmcSurvey.class));
       	cmcSurveysTable.deleteTable();		
	}
   
    private void loadTableData(DynamoDbTable<CmcSurvey> cmcSurveyTable, InputStream inputStream) throws Exception
    {   
        // Insert data into the table
    	logger.info("Inserting data into the table:" + AWS_TABLE_NAME);
         
        List<CmcSurvey> cmcSurveyList = readFromFileAndConvert(inputStream);
        
        if (cmcSurveyList == null)
        {
        	logger.error("cam group user list from json file is Empty - can't do anything more so exiting");
        }
        else
        {
        	Integer surveyIDCounter = 1;
        	
        	for (int i = 0; i < cmcSurveyList.size(); i++) 
    		{
            	CmcSurvey cs = cmcSurveyList.get(i); 
            	cs.setCmcSurveyID(String.valueOf(surveyIDCounter));
                cmcSurveyTable.putItem(cs);
                surveyIDCounter++;
    		}
        }
        
	}
    
    private List<CmcSurvey> readFromFileAndConvert(InputStream inputStream) throws Exception 
    {
    	Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        CmcSurvey[] cmcSurveyArray = new Gson().fromJson(reader, CmcSurvey[].class);
        List<CmcSurvey> cmcSurveyList = Arrays.asList(cmcSurveyArray);
        return cmcSurveyList;        
    }
    
    private static DynamoDbTable<CmcSurvey> createTable(DynamoDbEnhancedClient ddbEnhancedClient, DynamoDbClient ddbClient) 
    {
        DynamoDbTable<CmcSurvey> cmcSurveysTable = ddbEnhancedClient.table(AWS_TABLE_NAME, TableSchema.fromBean(CmcSurvey.class));
        
        // Create the DynamoDB table.  If it exists, it'll throw an exception
        
        try
        {
	        cmcSurveysTable.createTable(builder -> builder.build());
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
        
        return cmcSurveysTable;
    }
   
}