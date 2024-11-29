package com.pas.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pas.beans.CmcSurvey;
import com.pas.dynamodb.DynamoClients;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class CmcSurveysDAO implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(CmcSurveysDAO.class);
	
	private List<CmcSurvey> fullSurveysList = new ArrayList<CmcSurvey>();		
	private Map<String,CmcSurvey> fullSurveysMap = new HashMap<>();
	private static DynamoClients dynamoClients;
	private static DynamoDbTable<CmcSurvey> cmcSurveysTable;
	private static final String AWS_TABLE_NAME = "cmcSurveys";
	
	public CmcSurveysDAO(DynamoClients dynamoClients2) 
	{
	   try 
	   {
	       dynamoClients = dynamoClients2;
	       cmcSurveysTable = dynamoClients.getDynamoDbEnhancedClient().table(AWS_TABLE_NAME, TableSchema.fromBean(CmcSurvey.class));
	   } 
	   catch (final Exception ex) 
	   {
	      logger.error("Got exception while initializing CmcSurveysDAO. Ex = " + ex.getMessage(), ex);
	   }	   
	}
	
	public void readAllSurveysFromDB() throws Exception
	{				
		Iterator<CmcSurvey> results = cmcSurveysTable.scan().items().iterator();
            
		int clientCount = 0;
		while (results.hasNext()) 
        {
			clientCount++;
			logger.info("iterating survey " + clientCount);
			CmcSurvey cgTest = results.next();						
            this.getFullSurveysList().add(cgTest);            
            this.getFullSurveysMap().put(cgTest.getCmcSurveyID(), cgTest);
        }
		
		logger.info("exiting");
		
	}

	public List<CmcSurvey> getFullSurveysList() {
		return fullSurveysList;
	}

	public void setFullSurveysList(List<CmcSurvey> fullSurveysList) {
		this.fullSurveysList = fullSurveysList;
	}

	public Map<String, CmcSurvey> getFullSurveysMap() {
		return fullSurveysMap;
	}

	public void setFullSurveysMap(Map<String, CmcSurvey> fullSurveysMap) {
		this.fullSurveysMap = fullSurveysMap;
	}
	
	
}
