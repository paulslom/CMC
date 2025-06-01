package com.pas.dynamodb;

import java.io.File;
import java.io.FileInputStream;
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

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class LoadTableDynamoDB_CmcSurvey
{
	private static Logger logger = LogManager.getLogger(LoadTableDynamoDB_CmcSurvey.class);
	private static String AWS_TABLE_NAME = "cmcSurveys";
	private static String SURVEYS_JSONFILE = "SurveysData.json";

	public static void main(String[] args) throws Exception
    { 
    	logger.info("**********  START of program ***********");   	
    	
    	 try 
         {
    		 DynamoClients dynamoClients = DynamoUtil.getDynamoClients();
    
    		 loadTable(dynamoClients);
	          	
			 logger.info("**********  END of program ***********");
         }
    	 catch (Exception e)
    	 {
    		 logger.error("Exception: " + e.getMessage(), e);
    	 }
		System.exit(1);
	}
	
    public static void loadTable(DynamoClients dynamoClients) throws Exception 
    {
        //Grab a handle to the table in DynamoDB
        DynamoDbTable<CmcSurvey> cmcSurveyTable = dynamoClients.getDynamoDbEnhancedClient().table(AWS_TABLE_NAME, TableSchema.fromBean(CmcSurvey.class));     

        String jsonFilePath = "C:\\Paul\\GitHub\\CMC\\src\\main\\resources\\data\\" + SURVEYS_JSONFILE;
        File jsonFile = new File(jsonFilePath);          
        
        InputStream inputStream = new FileInputStream(jsonFile);
   
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
    
    private static List<CmcSurvey> readFromFileAndConvert(InputStream inputStream) throws Exception 
    {
    	Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        CmcSurvey[] cmcSurveyArray = new Gson().fromJson(reader, CmcSurvey[].class);
        List<CmcSurvey> cmcSurveyList = Arrays.asList(cmcSurveyArray);
        return cmcSurveyList;        
    }
      
}