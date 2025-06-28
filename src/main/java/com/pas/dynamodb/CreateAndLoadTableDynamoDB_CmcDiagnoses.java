package com.pas.dynamodb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pas.beans.CmcDiagnosis;

import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

public class CreateAndLoadTableDynamoDB_CmcDiagnoses
{
	private static Logger logger = LogManager.getLogger(CreateAndLoadTableDynamoDB_CmcDiagnoses.class);
	private static String AWS_TABLE_NAME = "CmcDiagnoses";
	
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
        DynamoDbTable<CmcDiagnosis> cmcDiagnosisTable = createTable(dynamoClients.getDynamoDbEnhancedClient(), dynamoClients.getDdbClient());           

        // Insert data into the table
    	logger.info("Inserting data into the table:" + AWS_TABLE_NAME);
    
    	//Specific to Physical Disability
       	CmcDiagnosis cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("1"); 
	  	cmcDiagnosis.setCmcSurveyId("1");
	  	cmcDiagnosis.setCmcSurveyName("Physical Disabilities");
	  	cmcDiagnosis.setCmcDiagnosis("Cerebral palsy");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
		
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("2"); 
	  	cmcDiagnosis.setCmcSurveyId("1");
	  	cmcDiagnosis.setCmcSurveyName("Physical Disabilities");
	  	cmcDiagnosis.setCmcDiagnosis("Spina bifida");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
	  	
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("3"); 
	  	cmcDiagnosis.setCmcSurveyId("1");
	  	cmcDiagnosis.setCmcSurveyName("Physical Disabilities");
	  	cmcDiagnosis.setCmcDiagnosis("Spinal cord injury");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
	  	
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("4"); 
	  	cmcDiagnosis.setCmcSurveyId("1");
	  	cmcDiagnosis.setCmcSurveyName("Physical Disabilities");
	  	cmcDiagnosis.setCmcDiagnosis("Muscular dystrophy");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
	  	
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("5"); 
	  	cmcDiagnosis.setCmcSurveyId("1");
	  	cmcDiagnosis.setCmcSurveyName("Physical Disabilities");
	  	cmcDiagnosis.setCmcDiagnosis("Limb abnormality/amputation");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
	  	
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("6"); 
	  	cmcDiagnosis.setCmcSurveyId("1");
	  	cmcDiagnosis.setCmcSurveyName("Physical Disabilities");
	  	cmcDiagnosis.setCmcDiagnosis("Acquired head injury");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
		  	
	    //Specific to Intellectual Disability
       	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("7"); 
	  	cmcDiagnosis.setCmcSurveyId("2");
	  	cmcDiagnosis.setCmcSurveyName("Intellectual Disabilities");
	  	cmcDiagnosis.setCmcDiagnosis("Intellectual disability only");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
		
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("8"); 
	  	cmcDiagnosis.setCmcSurveyId("2");
	  	cmcDiagnosis.setCmcSurveyName("Intellectual Disabilities");
	  	cmcDiagnosis.setCmcDiagnosis("Attention deficit hyperactivity disorder");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
	  	
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("9"); 
	  	cmcDiagnosis.setCmcSurveyId("2");
	  	cmcDiagnosis.setCmcSurveyName("Intellectual Disabilities");
	  	cmcDiagnosis.setCmcDiagnosis("Down syndrome");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
	  	
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("10"); 
	  	cmcDiagnosis.setCmcSurveyId("2");
	  	cmcDiagnosis.setCmcSurveyName("Intellectual Disabilities");
	  	cmcDiagnosis.setCmcDiagnosis("Fragile X");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
	  	
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("11"); 
	  	cmcDiagnosis.setCmcSurveyId("2");
	  	cmcDiagnosis.setCmcSurveyName("Intellectual Disabilities");
	  	cmcDiagnosis.setCmcDiagnosis("Communication disorders (expressive and receptive language delays)");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
	  	
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("12"); 
	  	cmcDiagnosis.setCmcSurveyId("2");
	  	cmcDiagnosis.setCmcSurveyName("Intellectual Disabilities");
	  	cmcDiagnosis.setCmcDiagnosis("Hearing difficulties");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
	  	
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("13"); 
	  	cmcDiagnosis.setCmcSurveyId("2");
	  	cmcDiagnosis.setCmcSurveyName("Intellectual Disabilities");
	  	cmcDiagnosis.setCmcDiagnosis("Vision difficulties");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
	  	
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("14"); 
	  	cmcDiagnosis.setCmcSurveyId("2");
	  	cmcDiagnosis.setCmcSurveyName("Intellectual Disabilities");
	  	cmcDiagnosis.setCmcDiagnosis("Anxiety disorder");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
	  	
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("15"); 
	  	cmcDiagnosis.setCmcSurveyId("2");
	  	cmcDiagnosis.setCmcSurveyName("Intellectual Disabilities");
	  	cmcDiagnosis.setCmcDiagnosis("Epilepsy");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
	  	
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("16"); 
	  	cmcDiagnosis.setCmcSurveyId("2");
	  	cmcDiagnosis.setCmcSurveyName("Intellectual Disabilities");
	  	cmcDiagnosis.setCmcDiagnosis("Behavioral disorders (conduct; oppositional defiant)");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
		  	
	    //Specific to Autism
       	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("17"); 
	  	cmcDiagnosis.setCmcSurveyId("3");
	  	cmcDiagnosis.setCmcSurveyName("Autism");
	  	cmcDiagnosis.setCmcDiagnosis("Autism/Autism spectrum disorder only");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
		
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("18"); 
	  	cmcDiagnosis.setCmcSurveyId("3");
	  	cmcDiagnosis.setCmcSurveyName("Autism");
	  	cmcDiagnosis.setCmcDiagnosis("ADHD/attention deficit and/or hyperactivity disorder");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
	  	
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("19"); 
	  	cmcDiagnosis.setCmcSurveyId("3");
	  	cmcDiagnosis.setCmcSurveyName("Autism");
	  	cmcDiagnosis.setCmcDiagnosis("Communication disorders (expressive and receptive language delays)");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
	  	
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("20"); 
	  	cmcDiagnosis.setCmcSurveyId("3");
	  	cmcDiagnosis.setCmcSurveyName("Autism");
	  	cmcDiagnosis.setCmcDiagnosis("Anxiety");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
	  	
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("21"); 
	  	cmcDiagnosis.setCmcSurveyId("3");
	  	cmcDiagnosis.setCmcSurveyName("Autism");
	  	cmcDiagnosis.setCmcDiagnosis("Depression");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
	  	
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("22"); 
	  	cmcDiagnosis.setCmcSurveyId("3");
	  	cmcDiagnosis.setCmcSurveyName("Autism");
	  	cmcDiagnosis.setCmcDiagnosis("Obsessive/Compulsion disorder");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
	  	
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("23"); 
	  	cmcDiagnosis.setCmcSurveyId("3");
	  	cmcDiagnosis.setCmcSurveyName("Autism");
	  	cmcDiagnosis.setCmcDiagnosis("Learning disabilities");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
	}
   
    private static DynamoDbTable<CmcDiagnosis> createTable(DynamoDbEnhancedClient ddbEnhancedClient, DynamoDbClient ddbClient) 
    {
        DynamoDbTable<CmcDiagnosis> teamTable = ddbEnhancedClient.table(AWS_TABLE_NAME, TableSchema.fromBean(CmcDiagnosis.class));
        
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
    	DynamoDbTable<CmcDiagnosis> teamTable = ddbEnhancedClient.table(AWS_TABLE_NAME, TableSchema.fromBean(CmcDiagnosis.class));
       	teamTable.deleteTable();		
	}
}