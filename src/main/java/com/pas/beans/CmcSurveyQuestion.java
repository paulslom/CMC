package com.pas.beans;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class CmcSurveyQuestion implements Serializable
{
	private static final long serialVersionUID = 131158039169073163L;
	
	private static Logger logger = LogManager.getLogger(CmcSurveyQuestion.class);
	
	private String cmcSurveyQuestionID;
	private String cmcSurveyID;
	private String cmcSurveySection;
	private String cmcSurveyCategory;
	private String cmcSurveyQuestionNumber;
	private String cmcSurveyQuestionEnglish;
	private String cmcSurveyQuestionSpanish;
	private String cmcSurveyQuestionStyleClass = null;
	private boolean cmcSurveyQuestionRenderInputs = true;
	private String answer;
	private String answerComment;
	private String answerStyle;
	
	static int ITEMS_ARE_EQUAL = 0;
	
	@Override
	public String toString() 
	{
		String myString = "Question: " + cmcSurveyQuestionEnglish;
		return myString;
	}
	
	@DynamoDbPartitionKey
	public String getCmcSurveyQuestionID() {
		return cmcSurveyQuestionID;
	}
	public void setCmcSurveyQuestionID(String cmcSurveyQuestionID) {
		this.cmcSurveyQuestionID = cmcSurveyQuestionID;
	}
	public String getCmcSurveyID() {
		return cmcSurveyID;
	}
	public void setCmcSurveyID(String cmcSurveyID) {
		this.cmcSurveyID = cmcSurveyID;
	}
	
	public String getCmcSurveySection() {
		return cmcSurveySection;
	}
	public void setCmcSurveySection(String cmcSurveySection) {
		this.cmcSurveySection = cmcSurveySection;
	}
	
	public String getCmcSurveyQuestionNumber() {
		return cmcSurveyQuestionNumber;
	}

	public void setCmcSurveyQuestionNumber(String cmcSurveyQuestionNumber) {
		this.cmcSurveyQuestionNumber = cmcSurveyQuestionNumber;
	}

	public String getCmcSurveyQuestionEnglish() {
		return cmcSurveyQuestionEnglish;
	}

	public void setCmcSurveyQuestionEnglish(String cmcSurveyQuestionEnglish) {
		this.cmcSurveyQuestionEnglish = cmcSurveyQuestionEnglish;
	}

	public String getCmcSurveyQuestionSpanish() {
		return cmcSurveyQuestionSpanish;
	}

	public void setCmcSurveyQuestionSpanish(String cmcSurveyQuestionSpanish) {
		this.cmcSurveyQuestionSpanish = cmcSurveyQuestionSpanish;
	}

	public static class SurveyQuestionComparator implements Comparator<CmcSurveyQuestion> 
	{
		public int compare(CmcSurveyQuestion cmcSurveyQuestion1, CmcSurveyQuestion cmcSurveyQuestion2)
		{
			try
			{
				String[] question1Parts = cmcSurveyQuestion1.getCmcSurveyQuestionNumber().split("\\.");
				String[] question2Parts = cmcSurveyQuestion2.getCmcSurveyQuestionNumber().split("\\.");
				
				Integer question1Node1 = Integer.parseInt(question1Parts[0]);
				Integer question1Node2 = Integer.parseInt(question1Parts[1]);
				Integer question1Node3 = Integer.parseInt(question1Parts[2]);
				
				Integer question2Node1 = Integer.parseInt(question2Parts[0]);
				Integer question2Node2 = Integer.parseInt(question2Parts[1]);
				Integer question2Node3 = Integer.parseInt(question2Parts[2]);
				int node1Compare = question1Node1.compareTo(question2Node1);
				
				if (node1Compare != ITEMS_ARE_EQUAL) 
				{
					return node1Compare; 
				}
				
				int node2Compare = question1Node2.compareTo(question2Node2);
				
				if (node2Compare != ITEMS_ARE_EQUAL) 
				{
					return node2Compare; 
				}
				
				return question1Node3.compareTo(question2Node3);
			}
			catch (Exception e)
			{
				logger.info(e.getMessage(), e);
			}
			
			//if we get this far we have an issue
			return -1;
			
		}		
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getAnswerComment() {
		return answerComment;
	}

	public void setAnswerComment(String answerComment) {
		this.answerComment = answerComment;
	}

	public String getAnswerStyle() {
		return answerStyle;
	}

	public void setAnswerStyle(String answerStyle) {
		this.answerStyle = answerStyle;
	}

	public boolean isCmcSurveyQuestionRenderInputs() {
		return cmcSurveyQuestionRenderInputs;
	}

	public void setCmcSurveyQuestionRenderInputs(boolean cmcSurveyQuestionRenderInputs) {
		this.cmcSurveyQuestionRenderInputs = cmcSurveyQuestionRenderInputs;
	}

	public String getCmcSurveyQuestionStyleClass() {
		return cmcSurveyQuestionStyleClass;
	}

	public void setCmcSurveyQuestionStyleClass(String cmcSurveyQuestionStyleClass) {
		this.cmcSurveyQuestionStyleClass = cmcSurveyQuestionStyleClass;
	}

	public String getCmcSurveyCategory() {
		return cmcSurveyCategory;
	}

	public void setCmcSurveyCategory(String cmcSurveyCategory) {
		this.cmcSurveyCategory = cmcSurveyCategory;
	}
		
	/*
	 * @DynamoDbPartitionKey is the primary key
	 * @DynamoDbSortKey sorts on something
	 * @DynamoDbSecondaryPartitionKey is a GSI (Global Secondary Index.  Example:
	 *     @DynamoDbSecondaryPartitionKey(indexNames = "customers_by_name")
	 * @DynamoDbSecondarySortKey
	 *     // Defines an LSI (customers_by_date) with a sort key of 'createdDate' and also declares the 
           // same attribute as a sort key for the GSI named 'customers_by_name'
           @DynamoDbSecondarySortKey(indexNames = {"customers_by_date", "customers_by_name"})
	 */
	
}
