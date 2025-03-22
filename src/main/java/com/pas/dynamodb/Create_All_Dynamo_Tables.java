package com.pas.dynamodb;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Create_All_Dynamo_Tables
{
	private static Logger logger = LogManager.getLogger(Create_All_Dynamo_Tables.class); //log4j for Logging 
	
	private static String SURVEYS_JSONFILE = "SurveysData.json";
	private static String SURVEYQUESTIONS_PHYSICAL_DISABILITIES_JSONFILE = "SurveyQuestionsPhysicalDisabilitiesData.json";
	private static String SURVEYQUESTIONS_INTELLECTUAL_DISABILITIES_JSONFILE = "SurveyQuestionsIntellectualDisabilitiesData.json";
	private static String SURVEYQUESTIONS_AUTISM_DISORDER_JSONFILE = "SurveyQuestionsAutismDisorderData.json";
		
    public static void main(String[] args) throws Exception
    { 
    	logger.debug("**********  START of program ***********");   	
    	
    	 try 
         {
    		 DynamoClients dynamoClients = DynamoUtil.getDynamoClients();
    
    		 doTable(dynamoClients, SURVEYS_JSONFILE);
    		 doTable(dynamoClients, SURVEYQUESTIONS_PHYSICAL_DISABILITIES_JSONFILE);
    		 doTable(dynamoClients, SURVEYQUESTIONS_INTELLECTUAL_DISABILITIES_JSONFILE);
    		 doTable(dynamoClients, SURVEYQUESTIONS_AUTISM_DISORDER_JSONFILE);
    		 doTable(dynamoClients, "SurveyAnswers");
    	     
			 logger.debug("**********  END of program ***********");
         }
    	 catch (Exception e)
    	 {
    		 logger.error("Exception in Create_All_Dynamo_Tables " + e.getMessage(), e);
    	 }
		System.exit(1);
	}

    private static void doTable(DynamoClients dynamoClients, String jsonFileName) throws Exception 
	{
		String jsonFilePath = "C:\\Paul\\GitHub\\CMC\\src\\main\\resources\\data\\" + jsonFileName;
        File jsonFile = new File(jsonFilePath);          
        
        InputStream inputStream = null;
        
        if (!jsonFileName.equalsIgnoreCase("SurveyAnswers"))
        {
        	inputStream = new FileInputStream(jsonFile);
        }
    		 
        if (jsonFileName.equalsIgnoreCase(SURVEYS_JSONFILE))
        {
        	CreateTableDynamoDB_CmcSurvey ct = new CreateTableDynamoDB_CmcSurvey();
        	ct.loadTable(dynamoClients, inputStream);		
        }
        
        if (jsonFileName.equalsIgnoreCase(SURVEYQUESTIONS_PHYSICAL_DISABILITIES_JSONFILE))
        {
        	CreateTableDynamoDB_CmcSurveyQuestionsPhysicalDisabilities cl = new CreateTableDynamoDB_CmcSurveyQuestionsPhysicalDisabilities();
        	cl.loadTable(dynamoClients, inputStream);		
        }
        
        if (jsonFileName.equalsIgnoreCase(SURVEYQUESTIONS_INTELLECTUAL_DISABILITIES_JSONFILE))
        {
        	CreateTableDynamoDB_CmcSurveyQuestionsIntellectualDisabilities cl = new CreateTableDynamoDB_CmcSurveyQuestionsIntellectualDisabilities();
        	cl.loadTable(dynamoClients, inputStream);		
        }
        
        if (jsonFileName.equalsIgnoreCase(SURVEYQUESTIONS_AUTISM_DISORDER_JSONFILE))
        {
        	CreateTableDynamoDB_CmcSurveyQuestionsAutismDisorder cl = new CreateTableDynamoDB_CmcSurveyQuestionsAutismDisorder();
        	cl.loadTable(dynamoClients, inputStream);		
        }
        
        if (jsonFileName.equalsIgnoreCase("SurveyAnswers"))
        {
        	CreateTableDynamoDB_CmcSurveyAnswer tst = new CreateTableDynamoDB_CmcSurveyAnswer();
        	tst.loadTable(dynamoClients, inputStream);		
        }
        
        
	}
		
}