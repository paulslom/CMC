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
import com.pas.beans.CmcMain;
import com.pas.beans.CmcSurvey;
import com.pas.beans.CmcSurveyQuestion;
import com.pas.dao.CmcSurveysDAO;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class CreateTableDynamoDB_CmcSurveyQuestionsIntellectualDisabilities
{	 
	private static Logger logger = LogManager.getLogger(CreateTableDynamoDB_CmcSurveyQuestionsIntellectualDisabilities.class);
	private static String AWS_TABLE_NAME = "cmcSurveyQuestions";
	
	public void loadTable(DynamoClients dynamoClients, InputStream inputStream) throws Exception 
	{
		DynamoDbTable<CmcSurveyQuestion> table = dynamoClients.getDynamoDbEnhancedClient().table(AWS_TABLE_NAME, TableSchema.fromBean(CmcSurveyQuestion.class));           
        loadTableData(table, inputStream, dynamoClients);				
	}
	   
    private static void loadTableData(DynamoDbTable<CmcSurveyQuestion> table, InputStream inputStream, DynamoClients dynamoClients) throws Exception
    {   
        // Insert data into the table
        logger.info("Inserting Intellectual disabilities data into the table:" + AWS_TABLE_NAME);
       
        String surveyID = "";
        CmcSurveysDAO cmcSurveysDAO = new CmcSurveysDAO(dynamoClients);
		List<CmcSurvey> tempList = cmcSurveysDAO.readAllSurveysFromDB(); 
    
        for (int i = 0; i < tempList.size(); i++) 
	    {
			CmcSurvey cmcSurvey = tempList.get(i);
			if (cmcSurvey.getCmcSurveyName().equalsIgnoreCase(CmcMain.Intellectual_Disabilities_Survey_Name))
			{
				surveyID = cmcSurvey.getCmcSurveyID();
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
            	gu.setCmcSurveyID(surveyID);
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
       
}