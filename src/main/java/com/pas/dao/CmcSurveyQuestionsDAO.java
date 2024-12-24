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

import com.pas.beans.CmcMain;
import com.pas.beans.CmcSurvey;
import com.pas.beans.CmcSurveyQuestion;
import com.pas.dynamodb.DynamoClients;
import com.pas.pojo.DisabilityRow;

import jakarta.inject.Inject;
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
	
	private static String physicalDisabilitiesSurveyID;
	private static String intellectualDisabilitiesSurveyID;
	private static String autismSpectrumDisorderSurveyID;
	
	private List<CmcSurveyQuestion> physicalDisabilitiesSection1QuestionsList = new ArrayList<>();
	private List<DisabilityRow> physicalDisabilitiesSection1QuestionsHorizontalList = new ArrayList<>();
	
	private List<CmcSurveyQuestion> physicalDisabilitiesSection2QuestionsList = new ArrayList<>();
	private List<DisabilityRow> physicalDisabilitiesSection2QuestionsHorizontalList = new ArrayList<>();
	
	private List<CmcSurveyQuestion> physicalDisabilitiesSection3QuestionsList = new ArrayList<>();
	
	private List<CmcSurveyQuestion> intellectualDisabilitiesSection1QuestionsList = new ArrayList<>();
	private List<DisabilityRow> intellectualDisabilitiesSection1QuestionsHorizontalList = new ArrayList<>();
	
	private List<CmcSurveyQuestion> intellectualDisabilitiesSection2QuestionsList = new ArrayList<>();
	
	private List<CmcSurveyQuestion> autismSpectrumSection1QuestionsList = new ArrayList<>();
	private List<CmcSurveyQuestion> autismSpectrumSection2QuestionsList = new ArrayList<>();
	
	private List<DisabilityRow> autismDisorderSection1QuestionsHorizontalList = new ArrayList<>();
	
	private static DynamoClients dynamoClients;
	private static DynamoDbTable<CmcSurveyQuestion> cmcSurveyQuestionsTable;
	private static final String AWS_TABLE_NAME = "cmcSurveyQuestions";
	
	@Inject CmcMain cmcMain;
	
	public CmcSurveyQuestionsDAO(DynamoClients dynamoClients2) 
	{
	    try 
	    {
	       dynamoClients = dynamoClients2;
	       cmcSurveyQuestionsTable = dynamoClients.getDynamoDbEnhancedClient().table(AWS_TABLE_NAME, TableSchema.fromBean(CmcSurveyQuestion.class));
	       
	       List<CmcSurvey> tempList = new ArrayList<>();
	       
	       if (cmcMain != null)
	       {
	    	   tempList = cmcMain.getFullSurveysList();		       
	       }
	       else //cmcMain not available yet, just read from the DAO directly
	       {
	    	   CmcSurveysDAO cmcSurveysDAO = new CmcSurveysDAO(dynamoClients);
	   		   tempList = cmcSurveysDAO.readAllSurveysFromDB(); 
	       }
	       
	       for (int i = 0; i < tempList.size(); i++) 
		   {
				CmcSurvey cmcSurvey = tempList.get(i);
				if (cmcSurvey.getCmcSurveyName().equalsIgnoreCase("Physical Disabilities"))
				{
					physicalDisabilitiesSurveyID = cmcSurvey.getCmcSurveyID();
				}
				else if (cmcSurvey.getCmcSurveyName().equalsIgnoreCase("Intellectual Disabilities"))
				{
					intellectualDisabilitiesSurveyID = cmcSurvey.getCmcSurveyID();
				}
				else if (cmcSurvey.getCmcSurveyName().equalsIgnoreCase("Autism Spectrum Disorder"))
				{
					autismSpectrumDisorderSurveyID =cmcSurvey.getCmcSurveyID();
				}
		   }
			
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
             
            if (cmcsq.getCmcSurveyID().equalsIgnoreCase(physicalDisabilitiesSurveyID) && cmcsq.getCmcSurveySection().equalsIgnoreCase("1"))
            {
            	this.getPhysicalDisabilitiesSection1QuestionsList().add(cmcsq);
            }
            else if (cmcsq.getCmcSurveyID().equalsIgnoreCase(physicalDisabilitiesSurveyID) && cmcsq.getCmcSurveySection().equalsIgnoreCase("2"))
            {
            	this.getPhysicalDisabilitiesSection2QuestionsList().add(cmcsq);
            }
            else if (cmcsq.getCmcSurveyID().equalsIgnoreCase(physicalDisabilitiesSurveyID) && cmcsq.getCmcSurveySection().equalsIgnoreCase("3"))
            {
            	if (!cmcsq.isCmcSurveyQuestionRenderInputs())
            	{
            		cmcsq.setCmcSurveyQuestionStyleClass("sectionSubTitle");
            	}
            	this.getPhysicalDisabilitiesSection3QuestionsList().add(cmcsq);
            }
            else if (cmcsq.getCmcSurveyID().equalsIgnoreCase(intellectualDisabilitiesSurveyID) && cmcsq.getCmcSurveySection().equalsIgnoreCase("1"))
            {
            	this.getIntellectualDisabilitiesSection1QuestionsList().add(cmcsq);
            }
            else if (cmcsq.getCmcSurveyID().equalsIgnoreCase(intellectualDisabilitiesSurveyID) && cmcsq.getCmcSurveySection().equalsIgnoreCase("2"))
            {
            	if (!cmcsq.isCmcSurveyQuestionRenderInputs())
            	{
            		cmcsq.setCmcSurveyQuestionStyleClass("sectionSubTitle");
            	}
            	this.getIntellectualDisabilitiesSection2QuestionsList().add(cmcsq);
            }
            else if (cmcsq.getCmcSurveyID().equalsIgnoreCase(autismSpectrumDisorderSurveyID) && cmcsq.getCmcSurveySection().equalsIgnoreCase("1"))
            {
            	this.getAutismSpectrumSection1QuestionsList().add(cmcsq);
            }
            else if (cmcsq.getCmcSurveyID().equalsIgnoreCase(autismSpectrumDisorderSurveyID) && cmcsq.getCmcSurveySection().equalsIgnoreCase("2"))
            {
            	this.getAutismSpectrumSection2QuestionsList().add(cmcsq);
            }
                       
          	this.getFullSurveyQuestionsMap().put(cmcsq.getCmcSurveyQuestionID(), cmcsq);			
        }
          	
		logger.info("LoggedDBOperation: function-inquiry; table:cmcSurveyQuestion; rows:" + this.getFullSurveyQuestionsMap().size());
		
		Collections.sort(this.getFullSurveyQuestionsList(), new CmcSurveyQuestion.SurveyQuestionComparator());
		
		Collections.sort(this.getPhysicalDisabilitiesSection1QuestionsList(), new CmcSurveyQuestion.SurveyQuestionComparator());
		Collections.sort(this.getPhysicalDisabilitiesSection2QuestionsList(), new CmcSurveyQuestion.SurveyQuestionComparator());
		Collections.sort(this.getPhysicalDisabilitiesSection3QuestionsList(), new CmcSurveyQuestion.SurveyQuestionComparator());
		
		Collections.sort(this.getIntellectualDisabilitiesSection1QuestionsList(), new CmcSurveyQuestion.SurveyQuestionComparator());
		Collections.sort(this.getIntellectualDisabilitiesSection2QuestionsList(), new CmcSurveyQuestion.SurveyQuestionComparator());
		
		Collections.sort(this.getAutismSpectrumSection1QuestionsList(), new CmcSurveyQuestion.SurveyQuestionComparator());
		Collections.sort(this.getAutismSpectrumSection2QuestionsList(), new CmcSurveyQuestion.SurveyQuestionComparator());
		
		setHorizontalOrientedLists();
		
		logger.info("exiting");		
	}
		
	private void setHorizontalOrientedLists()
	{
		List<DisabilityRow> tempListPhysSection1 = new ArrayList<>();
		List<DisabilityRow> tempListPhysSection2 = new ArrayList<>();		
		DisabilityRow physAvailableRow = new DisabilityRow();
		DisabilityRow physTriedRow = new DisabilityRow();
		DisabilityRow physSection1UseRow = new DisabilityRow();
		DisabilityRow physSection2UseRow = new DisabilityRow();		
		physAvailableRow.setCmcSurveyCategoryEnglish("Available");
		physAvailableRow.setAnswerRequired(true);
		physAvailableRow.setCmcSurveyCategorySpanish("Disponible");		
		physTriedRow.setCmcSurveyCategoryEnglish("Tried");
		physTriedRow.setCmcSurveyCategorySpanish("Intentó");		
		physSection1UseRow.setCmcSurveyCategoryEnglish("Use");
		physSection1UseRow.setCmcSurveyCategorySpanish("Usar");		
		physSection2UseRow.setCmcSurveyCategoryEnglish("Use");
		physSection2UseRow.setCmcSurveyCategorySpanish("Usar");
		tempListPhysSection1.add(physAvailableRow);
		tempListPhysSection1.add(physTriedRow);
		tempListPhysSection1.add(physSection1UseRow);		
		tempListPhysSection2.add(physSection2UseRow);
		this.setPhysicalDisabilitiesSection1QuestionsHorizontalList(tempListPhysSection1);
		this.setPhysicalDisabilitiesSection2QuestionsHorizontalList(tempListPhysSection2);
		
		List<DisabilityRow> tempListIntelSection1 = new ArrayList<>();
		DisabilityRow intelAvailableRow = new DisabilityRow();
		DisabilityRow intelTriedRow = new DisabilityRow();
		DisabilityRow intelUseRow = new DisabilityRow();		
		intelAvailableRow.setCmcSurveyCategoryEnglish("Available");
		intelAvailableRow.setAnswerRequired(true);
		intelAvailableRow.setCmcSurveyCategorySpanish("Disponible");		
		intelTriedRow.setCmcSurveyCategoryEnglish("Tried");
		intelTriedRow.setCmcSurveyCategorySpanish("Intentó");		
		intelUseRow.setCmcSurveyCategoryEnglish("Use");
		intelUseRow.setCmcSurveyCategorySpanish("Usar");		
		tempListIntelSection1.add(intelAvailableRow);
		tempListIntelSection1.add(intelTriedRow);
		tempListIntelSection1.add(intelUseRow);		
		this.setIntellectualDisabilitiesSection1QuestionsHorizontalList(tempListIntelSection1);
		
		List<DisabilityRow> tempListAutismSection1 = new ArrayList<>();			
		DisabilityRow autismAvailableRow = new DisabilityRow();
		DisabilityRow autismTriedRow = new DisabilityRow();
		DisabilityRow autismUseRow = new DisabilityRow();		
		autismAvailableRow.setCmcSurveyCategoryEnglish("Available");
		autismAvailableRow.setAnswerRequired(true);
		autismAvailableRow.setCmcSurveyCategorySpanish("Disponible");		
		autismTriedRow.setCmcSurveyCategoryEnglish("Tried");
		autismTriedRow.setCmcSurveyCategorySpanish("Intentó");		
		autismUseRow.setCmcSurveyCategoryEnglish("Use");
		autismUseRow.setCmcSurveyCategorySpanish("Usar");		
		tempListAutismSection1.add(autismAvailableRow);
		tempListAutismSection1.add(autismTriedRow);
		tempListAutismSection1.add(autismUseRow);		
		this.setAutismDisorderSection1QuestionsHorizontalList(tempListAutismSection1);
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

	public List<CmcSurveyQuestion> getPhysicalDisabilitiesSection1QuestionsList() {
		return physicalDisabilitiesSection1QuestionsList;
	}

	public void setPhysicalDisabilitiesSection1QuestionsList(
			List<CmcSurveyQuestion> physicalDisabilitiesSection1QuestionsList) {
		this.physicalDisabilitiesSection1QuestionsList = physicalDisabilitiesSection1QuestionsList;
	}

	public List<CmcSurveyQuestion> getPhysicalDisabilitiesSection2QuestionsList() {
		return physicalDisabilitiesSection2QuestionsList;
	}

	public void setPhysicalDisabilitiesSection2QuestionsList(
			List<CmcSurveyQuestion> physicalDisabilitiesSection2QuestionsList) {
		this.physicalDisabilitiesSection2QuestionsList = physicalDisabilitiesSection2QuestionsList;
	}

	public List<CmcSurveyQuestion> getPhysicalDisabilitiesSection3QuestionsList() {
		return physicalDisabilitiesSection3QuestionsList;
	}

	public void setPhysicalDisabilitiesSection3QuestionsList(
			List<CmcSurveyQuestion> physicalDisabilitiesSection3QuestionsList) {
		this.physicalDisabilitiesSection3QuestionsList = physicalDisabilitiesSection3QuestionsList;
	}

	public List<CmcSurveyQuestion> getIntellectualDisabilitiesSection1QuestionsList() {
		return intellectualDisabilitiesSection1QuestionsList;
	}

	public void setIntellectualDisabilitiesSection1QuestionsList(
			List<CmcSurveyQuestion> intellectualDisabilitiesSection1QuestionsList) {
		this.intellectualDisabilitiesSection1QuestionsList = intellectualDisabilitiesSection1QuestionsList;
	}

	public List<CmcSurveyQuestion> getIntellectualDisabilitiesSection2QuestionsList() {
		return intellectualDisabilitiesSection2QuestionsList;
	}

	public void setIntellectualDisabilitiesSection2QuestionsList(
			List<CmcSurveyQuestion> intellectualDisabilitiesSection2QuestionsList) {
		this.intellectualDisabilitiesSection2QuestionsList = intellectualDisabilitiesSection2QuestionsList;
	}

	public List<CmcSurveyQuestion> getAutismSpectrumSection1QuestionsList() {
		return autismSpectrumSection1QuestionsList;
	}

	public void setAutismSpectrumSection1QuestionsList(List<CmcSurveyQuestion> autismSpectrumSection1QuestionsList) {
		this.autismSpectrumSection1QuestionsList = autismSpectrumSection1QuestionsList;
	}

	public List<CmcSurveyQuestion> getAutismSpectrumSection2QuestionsList() {
		return autismSpectrumSection2QuestionsList;
	}

	public void setAutismSpectrumSection2QuestionsList(List<CmcSurveyQuestion> autismSpectrumSection2QuestionsList) {
		this.autismSpectrumSection2QuestionsList = autismSpectrumSection2QuestionsList;
	}

	public List<DisabilityRow> getPhysicalDisabilitiesSection1QuestionsHorizontalList() {
		return physicalDisabilitiesSection1QuestionsHorizontalList;
	}

	public void setPhysicalDisabilitiesSection1QuestionsHorizontalList(
			List<DisabilityRow> physicalDisabilitiesSection1QuestionsHorizontalList) {
		this.physicalDisabilitiesSection1QuestionsHorizontalList = physicalDisabilitiesSection1QuestionsHorizontalList;
	}

	public List<DisabilityRow> getPhysicalDisabilitiesSection2QuestionsHorizontalList() {
		return physicalDisabilitiesSection2QuestionsHorizontalList;
	}

	public void setPhysicalDisabilitiesSection2QuestionsHorizontalList(
			List<DisabilityRow> physicalDisabilitiesSection2QuestionsHorizontalList) {
		this.physicalDisabilitiesSection2QuestionsHorizontalList = physicalDisabilitiesSection2QuestionsHorizontalList;
	}

	public List<DisabilityRow> getIntellectualDisabilitiesSection1QuestionsHorizontalList() {
		return intellectualDisabilitiesSection1QuestionsHorizontalList;
	}

	public void setIntellectualDisabilitiesSection1QuestionsHorizontalList(
			List<DisabilityRow> intellectualDisabilitiesSection1QuestionsHorizontalList) {
		this.intellectualDisabilitiesSection1QuestionsHorizontalList = intellectualDisabilitiesSection1QuestionsHorizontalList;
	}

	public List<DisabilityRow> getAutismDisorderSection1QuestionsHorizontalList() {
		return autismDisorderSection1QuestionsHorizontalList;
	}

	public void setAutismDisorderSection1QuestionsHorizontalList(
			List<DisabilityRow> autismDisorderSection1QuestionsHorizontalList) {
		this.autismDisorderSection1QuestionsHorizontalList = autismDisorderSection1QuestionsHorizontalList;
	}

}
