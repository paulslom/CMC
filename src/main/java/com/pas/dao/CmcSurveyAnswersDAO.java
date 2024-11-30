package com.pas.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pas.beans.CmcMain;
import com.pas.beans.CmcSurveyAnswer;
import com.pas.dynamodb.DynamoClients;

import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class CmcSurveyAnswersDAO implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(CmcSurveyAnswersDAO.class);
	
	private List<CmcSurveyAnswer> fullSurveyAnswersList = new ArrayList<CmcSurveyAnswer>();		

	private static DynamoClients dynamoClients;
	private static DynamoDbTable<CmcSurveyAnswer> cmcSurveyAnswersTable;
	private static final String AWS_TABLE_NAME = "cmcSurveyAnswer";
	
	@Inject CmcMain cmcMain;
	
	public CmcSurveyAnswersDAO(DynamoClients dynamoClients2) 
	{
	   try 
	   {
	       dynamoClients = dynamoClients2;
	       cmcSurveyAnswersTable = dynamoClients.getDynamoDbEnhancedClient().table(AWS_TABLE_NAME, TableSchema.fromBean(CmcSurveyAnswer.class));
	   } 
	   catch (final Exception ex) 
	   {
	      logger.error("Got exception while initializing CmcSurveyAnswersDAO. Ex = " + ex.getMessage(), ex);
	   }	   
	}
	
	public void readAllSurveyAnswersFromDB() throws Exception
	{				
		Iterator<CmcSurveyAnswer> results = cmcSurveyAnswersTable.scan().items().iterator();
            
		int testimonialCount = 0;
		while (results.hasNext()) 
        {
			testimonialCount++;
			logger.info("iterating cmcSurveyAnswers " + testimonialCount);
			CmcSurveyAnswer cgTest = results.next();						
            this.getFullSurveyAnswersList().add(cgTest);			
        }
		
		logger.info("exiting");
		
	}

	public List<CmcSurveyAnswer> getFullSurveyAnswersList() {
		return fullSurveyAnswersList;
	}

	public void setFullSurveyAnswersList(List<CmcSurveyAnswer> fullSurveyAnswersList) {
		this.fullSurveyAnswersList = fullSurveyAnswersList;
	}
	
}
