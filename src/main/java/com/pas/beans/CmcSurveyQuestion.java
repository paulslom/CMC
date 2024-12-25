package com.pas.beans;

import java.io.Serializable;
import java.util.Comparator;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class CmcSurveyQuestion implements Serializable
{
	private static final long serialVersionUID = 131158039169073163L;
	
	private String cmcSurveyQuestionID;
	private String cmcSurveyID;
	private String cmcSurveySection;
	private String cmcSurveyCategory;
	private Integer cmcSurveyQuestionNumber;
	private String cmcSurveyQuestionEnglish;
	private String cmcSurveyQuestionSpanish;
	private String cmcSurveyQuestionStyleClass = null;
	private boolean cmcSurveyQuestionRenderInputs = true;
	private String answer;
	private String answerComment;
	private String answerStyle;
	
	@Override
	public String toString() 
	{
		String myString = "Survey Question ID: " + cmcSurveyQuestionID + " Survey Question in English: " + cmcSurveyQuestionEnglish;
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
	
	public Integer getCmcSurveyQuestionNumber() {
		return cmcSurveyQuestionNumber;
	}

	public void setCmcSurveyQuestionNumber(Integer cmcSurveyQuestionNumber) {
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
			return cmcSurveyQuestion1.getCmcSurveyQuestionNumber().compareTo(cmcSurveyQuestion2.getCmcSurveyQuestionNumber());
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
