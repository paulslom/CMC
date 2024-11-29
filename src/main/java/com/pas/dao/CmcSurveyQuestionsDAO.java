package com.pas.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pas.beans.CmcSurveyQuestion;
import com.pas.dynamodb.DynamoClients;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;

public class CmcSurveyQuestionsDAO implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(CmcSurveyQuestionsDAO.class);
	
	private Map<String,CmcSurveyQuestion> fullSurveyQuestionsMap = new HashMap<>();
	private List<CmcSurveyQuestion> fullSurveyQuestionsList = new ArrayList<>();
	private static DynamoClients dynamoClients;
	private static DynamoDbTable<CmcSurveyQuestion> cmcSurveyQuestionsTable;
	private static final String AWS_TABLE_NAME = "cmcSurveyQuestions";
	
	public CmcSurveyQuestionsDAO(DynamoClients dynamoClients2) 
	{
	   try 
	   {
	       dynamoClients = dynamoClients2;
	       cmcSurveyQuestionsTable = dynamoClients.getDynamoDbEnhancedClient().table(AWS_TABLE_NAME, TableSchema.fromBean(CmcSurveyQuestion.class));
	   } 
	   catch (final Exception ex) 
	   {
	      logger.error("Got exception while initializing CmcSurveyQuestionsDAO. Ex = " + ex.getMessage(), ex);
	   }	   
	}
	
	public void readAllSurveyQuestionsFromDB() throws Exception
	{				
		Iterator<CmcSurveyQuestion> results = cmcSurveyQuestionsTable.scan().items().iterator();
            
        while (results.hasNext()) 
        {
            CmcSurveyQuestion cmcsq = results.next(); 
            this.getFullSurveyQuestionsList().add(cmcsq);
          	this.getFullSurveyQuestionsMap().put(cmcsq.getCmcSurveyQuestionID(), cmcsq);			
        }
          	
		logger.info("LoggedDBOperation: function-inquiry; table:cmcSurveyQuestion; rows:" + this.getFullSurveyQuestionsMap().size());
		
		Collections.sort(this.getFullSurveyQuestionsList(), new CmcSurveyQuestion.SurveyQuestionComparator());
		
		//this loop only for debugging purposes
		/*
		for (Map.Entry<String, CamGroupUser> entry : this.getFullSurveyQuestionsMap().entrySet()) 
		{
		    String key = entry.getKey();
		    CamGroupUser User = entry.getValue();

		    logger.info("Key = " + key + ", value = " + User.getUserName());
		}
		*/
		
		logger.info("exiting");
		
	}
		
	public CmcSurveyQuestion getCmcSurveyQuestion(String cmcsq)
    {	    	
		CmcSurveyQuestion gu = this.getFullSurveyQuestionsMap().get(cmcsq);			
    	return gu;
    }	
	
	private void deleteCmcSurveyQuestion(String cmcsqid) throws Exception
	{
		Key key = Key.builder().partitionValue(cmcsqid).build();
		DeleteItemEnhancedRequest deleteItemEnhancedRequest = DeleteItemEnhancedRequest.builder().key(key).build();
		cmcSurveyQuestionsTable.deleteItem(deleteItemEnhancedRequest);
		
		logger.info("LoggedDBOperation: function-delete; table:cmcSurveyQuestion; rows:1");
		
		CmcSurveyQuestion gu = new CmcSurveyQuestion();
		gu.setCmcSurveyQuestionID(cmcsqid);
		refreshListsAndMaps("delete", gu);	
	}
	
	public void addCmcSurveyQuestion(CmcSurveyQuestion cmcsq) throws Exception
	{
		PutItemEnhancedRequest<CmcSurveyQuestion> putItemEnhancedRequest = PutItemEnhancedRequest.builder(CmcSurveyQuestion.class).item(cmcsq).build();
		cmcSurveyQuestionsTable.putItem(putItemEnhancedRequest);
			
		logger.info("LoggedDBOperation: function-update; table:cmcSurveyQuestionsTable; rows:1");
					
		refreshListsAndMaps("add", cmcsq);	
	}
	
	public void updateCmcSurveyQuestion(CmcSurveyQuestion cmcsq) throws Exception
	{
		deleteCmcSurveyQuestion(cmcsq.getCmcSurveyID());
		addCmcSurveyQuestion(cmcsq);		
		refreshListsAndMaps("update", cmcsq);	
	}

	private void refreshListsAndMaps(String function, CmcSurveyQuestion cmcsq) 
	{
		if (function.equalsIgnoreCase("delete"))
		{
			this.getFullSurveyQuestionsMap().remove(cmcsq.getCmcSurveyID());	
		}
		else if (function.equalsIgnoreCase("add"))
		{
			this.getFullSurveyQuestionsMap().put(cmcsq.getCmcSurveyID(), cmcsq);	
		}
		else if (function.equalsIgnoreCase("update"))
		{
			this.getFullSurveyQuestionsMap().remove(cmcsq.getCmcSurveyID());	
			this.getFullSurveyQuestionsMap().put(cmcsq.getCmcSurveyID(), cmcsq);		
		}
		
	}

	public Map<String, CmcSurveyQuestion> getFullSurveyQuestionsMap() {
		return fullSurveyQuestionsMap;
	}

	public void setFullSurveyQuestionsMap(Map<String, CmcSurveyQuestion> fullSurveyQuestionsMap) {
		this.fullSurveyQuestionsMap = fullSurveyQuestionsMap;
	}

	public List<CmcSurveyQuestion> getFullSurveyQuestionsList() {
		return fullSurveyQuestionsList;
	}

	public void setFullSurveyQuestionsList(List<CmcSurveyQuestion> fullSurveyQuestionsList) {
		this.fullSurveyQuestionsList = fullSurveyQuestionsList;
	}
	
}
