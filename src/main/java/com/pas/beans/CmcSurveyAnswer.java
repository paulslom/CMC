package com.pas.beans;

import java.io.Serializable;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class CmcSurveyAnswer implements Serializable
{
	private static final long serialVersionUID = 131158039169073163L;
	
	private String surveyAnswerID; // (Primary key - auto-generated guid)
	private String surveyQuestionID; //the ID of the question
	private String surveyID; //the ID of the question
	private String surveyAnswerDate;
	private String surveyAnswer;
	private String surveyAnswerComment;
	
	@DynamoDbPartitionKey
	public String getSurveyAnswerID() {
		return surveyAnswerID;
	}
	public void setSurveyAnswerID(String surveyAnswerID) {
		this.surveyAnswerID = surveyAnswerID;
	}
	public String getSurveyQuestionID() {
		return surveyQuestionID;
	}
	public void setSurveyQuestionID(String surveyQuestionID) {
		this.surveyQuestionID = surveyQuestionID;
	}
	public String getSurveyID() {
		return surveyID;
	}
	public void setSurveyID(String surveyID) {
		this.surveyID = surveyID;
	}
	public String getSurveyAnswerDate() {
		return surveyAnswerDate;
	}
	public void setSurveyAnswerDate(String surveyAnswerDate) {
		this.surveyAnswerDate = surveyAnswerDate;
	}
	public String getSurveyAnswer() {
		return surveyAnswer;
	}
	public void setSurveyAnswer(String surveyAnswer) {
		this.surveyAnswer = surveyAnswer;
	}
	public String getSurveyAnswerComment() {
		return surveyAnswerComment;
	}
	public void setSurveyAnswerComment(String surveyAnswerComment) {
		this.surveyAnswerComment = surveyAnswerComment;
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
