package com.pas.beans;

import java.io.Serializable;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class CmcSurvey implements Serializable
{
	private static final long serialVersionUID = 131158039169073163L;
	
	private String cmcSurveyID;
	private String cmcSurveyName;
	private String cmcSurveyNameSpanish;
	private String cmcSurveyPageTitleEnglish;
	private String cmcSurveyPageTitleSpanish;
	private String cmcSurveyPageSubTitleEnglish;
	private String cmcSurveyPageSubTitleSpanish;
	
	@Override
	public String toString() 
	{
		String myString = "Survey ID: " + cmcSurveyID + " Survey Name: " + cmcSurveyName;
		return myString;
	}
	
	@DynamoDbPartitionKey
	public String getCmcSurveyID() {
		return cmcSurveyID;
	}
	public void setCmcSurveyID(String cmcSurveyID) {
		this.cmcSurveyID = cmcSurveyID;
	}
	public String getCmcSurveyName() {
		return cmcSurveyName;
	}
	public void setCmcSurveyName(String cmcSurveyName) {
		this.cmcSurveyName = cmcSurveyName;
	}

	public String getCmcSurveyNameSpanish() {
		return cmcSurveyNameSpanish;
	}

	public void setCmcSurveyNameSpanish(String cmcSurveyNameSpanish) {
		this.cmcSurveyNameSpanish = cmcSurveyNameSpanish;
	}

	public String getCmcSurveyPageTitleEnglish() {
		return cmcSurveyPageTitleEnglish;
	}

	public void setCmcSurveyPageTitleEnglish(String cmcSurveyPageTitleEnglish) {
		this.cmcSurveyPageTitleEnglish = cmcSurveyPageTitleEnglish;
	}

	public String getCmcSurveyPageTitleSpanish() {
		return cmcSurveyPageTitleSpanish;
	}

	public void setCmcSurveyPageTitleSpanish(String cmcSurveyPageTitleSpanish) {
		this.cmcSurveyPageTitleSpanish = cmcSurveyPageTitleSpanish;
	}

	public String getCmcSurveyPageSubTitleEnglish() {
		return cmcSurveyPageSubTitleEnglish;
	}

	public void setCmcSurveyPageSubTitleEnglish(String cmcSurveyPageSubTitleEnglish) {
		this.cmcSurveyPageSubTitleEnglish = cmcSurveyPageSubTitleEnglish;
	}

	public String getCmcSurveyPageSubTitleSpanish() {
		return cmcSurveyPageSubTitleSpanish;
	}

	public void setCmcSurveyPageSubTitleSpanish(String cmcSurveyPageSubTitleSpanish) {
		this.cmcSurveyPageSubTitleSpanish = cmcSurveyPageSubTitleSpanish;
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
