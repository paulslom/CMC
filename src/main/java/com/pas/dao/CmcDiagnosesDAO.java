package com.pas.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pas.beans.CmcDiagnosis;
import com.pas.dynamodb.DynamoClients;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class CmcDiagnosesDAO implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(CmcDiagnosesDAO.class);
	
	private List<CmcDiagnosis> fullDiagnosesList = new ArrayList<CmcDiagnosis>();		
	private Map<String,CmcDiagnosis> fullDiagnosesMap = new HashMap<>();
	private static DynamoClients dynamoClients;
	private static DynamoDbTable<CmcDiagnosis> cmcDiagnosesTable;
	private static final String AWS_TABLE_NAME = "CmcDiagnoses";
		
	public CmcDiagnosesDAO(DynamoClients dynamoClients2) 
	{
	   try 
	   {
	       dynamoClients = dynamoClients2;
	       cmcDiagnosesTable = dynamoClients.getDynamoDbEnhancedClient().table(AWS_TABLE_NAME, TableSchema.fromBean(CmcDiagnosis.class));
	   } 
	   catch (final Exception ex) 
	   {
	      logger.error("Got exception while initializing CmcDiagnosesDAO. Ex = " + ex.getMessage(), ex);
	   }	   
	}
	
	public List<CmcDiagnosis> readAllDiagnosesFromDB() throws Exception
	{				
		Iterator<CmcDiagnosis> results = cmcDiagnosesTable.scan().items().iterator();
            
		int clientCount = 0;
		while (results.hasNext()) 
        {
			clientCount++;
			logger.debug("iterating diagnosis " + clientCount);
			CmcDiagnosis cmcDiagnosis = results.next();						
            this.getFullDiagnosesList().add(cmcDiagnosis);            
            this.getFullDiagnosesMap().put(cmcDiagnosis.getCmcDiagnosisId(), cmcDiagnosis);
        }
		
		logger.info("exiting");
		
		return this.getFullDiagnosesList();
		
	}

	public List<CmcDiagnosis> getFullDiagnosesList() {
		return fullDiagnosesList;
	}

	public void setFullDiagnosesList(List<CmcDiagnosis> fullDiagnosesList) {
		this.fullDiagnosesList = fullDiagnosesList;
	}

	public Map<String, CmcDiagnosis> getFullDiagnosesMap() {
		return fullDiagnosesMap;
	}

	public void setFullDiagnosesMap(Map<String, CmcDiagnosis> fullDiagnosesMap) {
		this.fullDiagnosesMap = fullDiagnosesMap;
	}
	
	
}
