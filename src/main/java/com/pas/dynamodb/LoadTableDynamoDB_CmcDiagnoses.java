package com.pas.dynamodb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pas.beans.CmcDiagnosis;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class LoadTableDynamoDB_CmcDiagnoses
{
	private static Logger logger = LogManager.getLogger(LoadTableDynamoDB_CmcDiagnoses.class);
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
    	 //Grab a handle to the table in DynamoDB
        DynamoDbTable<CmcDiagnosis> cmcDiagnosisTable = dynamoClients.getDynamoDbEnhancedClient().table(AWS_TABLE_NAME, TableSchema.fromBean(CmcDiagnosis.class));     

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
	  	cmcDiagnosis.setCmcDiagnosis("Level 1 - needing support");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
		
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("18"); 
	  	cmcDiagnosis.setCmcSurveyId("3");
	  	cmcDiagnosis.setCmcSurveyName("Autism");
	  	cmcDiagnosis.setCmcDiagnosis("Level 2 – needing moderate support");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
	  	
	  	cmcDiagnosis = new CmcDiagnosis();
       	cmcDiagnosis.setCmcDiagnosisId("19"); 
	  	cmcDiagnosis.setCmcSurveyId("3");
	  	cmcDiagnosis.setCmcSurveyName("Autism");
	  	cmcDiagnosis.setCmcDiagnosis("Level 3 – needing substantial support");
	  	cmcDiagnosisTable.putItem(cmcDiagnosis); 
	  	
	  	
	}
      
}