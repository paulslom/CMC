package com.pas.util;

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pas.dynamodb.CreateTableDynamoDB_CmcSurvey;
import com.pas.dynamodb.CreateTableDynamoDB_CmcSurveyQuestion;
import com.pas.dynamodb.DynamoClients;
import com.pas.dynamodb.DynamoUtil;

public class FileDataLoader 
{
	private static Logger logger = LogManager.getLogger(FileDataLoader.class);
    private static final boolean loadData = false; 
    //private static final boolean loadData = true;
  	
    private static String SURVEYS_JSONFILE = "/data/SurveysData.json";
    private static String SURVEYQUESTIONS_JSONFILE = "/data/SurveyQuestionsData.json";
	
    public boolean load() throws Exception 
    {
        if (!loadData) 
        {
            logger.info("Load data property is set to false.  Not loading dynamo tables from json files");
            return false;
        }
        
        DynamoClients dynamoClients = DynamoUtil.getDynamoClients();
        
        doTable(dynamoClients, SURVEYS_JSONFILE); 
        doTable(dynamoClients, SURVEYQUESTIONS_JSONFILE);
                
        return true;
    }
    
    private static void doTable(DynamoClients dynamoClients, String jsonFileName) throws Exception 
	{
    	InputStream inputStream = FileDataLoader.class.getResourceAsStream(jsonFileName);
		 		 
        if (jsonFileName.equalsIgnoreCase(SURVEYS_JSONFILE))
        {
        	CreateTableDynamoDB_CmcSurvey ct = new CreateTableDynamoDB_CmcSurvey();
        	ct.loadTable(dynamoClients, inputStream);		
        }
        
        if (jsonFileName.equalsIgnoreCase(SURVEYQUESTIONS_JSONFILE))
        {
        	CreateTableDynamoDB_CmcSurveyQuestion cl = new CreateTableDynamoDB_CmcSurveyQuestion();
        	cl.loadTable(dynamoClients, inputStream);		
        }
        
       
	}
}

