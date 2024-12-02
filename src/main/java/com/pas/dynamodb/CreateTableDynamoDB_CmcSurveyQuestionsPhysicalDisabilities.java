package com.pas.dynamodb;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.pas.beans.CmcSurvey;
import com.pas.beans.CmcSurveyQuestion;
import com.pas.dao.CmcSurveysDAO;

import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

public class CreateTableDynamoDB_CmcSurveyQuestionsPhysicalDisabilities
{	 
	private static Logger logger = LogManager.getLogger(CreateTableDynamoDB_CmcSurveyQuestionsPhysicalDisabilities.class);
	private static String AWS_TABLE_NAME = "cmcSurveyQuestions";
	
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
        DynamoDbTable<CmcSurveyQuestion> table = createTable(dynamoClients.getDynamoDbEnhancedClient(), dynamoClients.getDdbClient());           
        loadTableData(table, inputStream, dynamoClients);				
	}
	    
    private static void deleteTable(DynamoDbEnhancedClient ddbEnhancedClient) throws Exception
    {
    	DynamoDbTable<CmcSurveyQuestion> table = ddbEnhancedClient.table(AWS_TABLE_NAME, TableSchema.fromBean(CmcSurveyQuestion.class));
        table.deleteTable();        
	}
   
    private static void loadTableData(DynamoDbTable<CmcSurveyQuestion> table, InputStream inputStream, DynamoClients dynamoClients) throws Exception
    {   
        // Insert data into the table
        logger.info("Inserting data into the table:" + AWS_TABLE_NAME);
       
        String physicalDisabilitiesSurveyID = "";
        CmcSurveysDAO cmcSurveysDAO = new CmcSurveysDAO(dynamoClients);
		List<CmcSurvey> tempList = cmcSurveysDAO.readAllSurveysFromDB(); 
    
        for (int i = 0; i < tempList.size(); i++) 
	    {
			CmcSurvey cmcSurvey = tempList.get(i);
			if (cmcSurvey.getCmcSurveyName().equalsIgnoreCase("Physical Disabilities"))
			{
				physicalDisabilitiesSurveyID = cmcSurvey.getCmcSurveyID();
				break;
			}			
	    }
    
        List<CmcSurveyQuestion> cmcSurveyQuestionList = readFromFileAndConvert(inputStream); 
        
        if (cmcSurveyQuestionList == null)
        {
        	logger.error("cam group client list from json file is Empty - can't do anything more so exiting");
        }
        else
        {
        	for (int i = 0; i < cmcSurveyQuestionList.size(); i++) 
    		{
            	CmcSurveyQuestion gu = cmcSurveyQuestionList.get(i);
            	gu.setCmcSurveyQuestionID(UUID.randomUUID().toString());
            	gu.setCmcSurveyID(physicalDisabilitiesSurveyID);
                table.putItem(gu);                
    		}             
        }
	}
    
    private static List<CmcSurveyQuestion> readFromFileAndConvert(InputStream inputStream) 
    {
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        CmcSurveyQuestion[] array = new Gson().fromJson(reader, CmcSurveyQuestion[].class);
       	List<CmcSurveyQuestion> list = Arrays.asList(array);
       	return list;        
    }
    
    private static DynamoDbTable<CmcSurveyQuestion> createTable(DynamoDbEnhancedClient ddbEnhancedClient, DynamoDbClient ddbClient) 
    {
    	// Create the DynamoDB table.  If it exists, it'll throw an exception
    	DynamoDbTable<CmcSurveyQuestion> table = ddbEnhancedClient.table(AWS_TABLE_NAME, TableSchema.fromBean(CmcSurveyQuestion.class));
        try
        {
	        table.createTable(builder -> builder.build());
        }
        catch (ResourceInUseException riue)
        {
        	logger.info("survey question Table already exists! " + riue.getMessage());
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
        
        return table;
    }

	
   
}