package com.pas.beans;

import java.io.Serializable;
import java.util.Comparator;

import org.springframework.stereotype.Component;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Named("pc_CmcSurveyQuestion")
@Component
@DynamoDbBean
@SessionScoped
public class CmcSurveyQuestion implements Serializable
{
	private static final long serialVersionUID = 131158039169073163L;
	
	private String cmcSurveyQuestionID;
	private String cmcSurveyID;
	private String cmcSurveySection;
	private String cmcSurveySectionTitleEnglish;
	private String cmcSurveySectionTitleSpanish;
	private String cmcSurveyCategoryEnglish;
	private String cmcSurveyCategorySpanish;
	private Integer cmcSurveyQuestionNumber;
	private String cmcSurveyQuestionEnglish;
	private String cmcSurveyQuestionSpanish;
	private String cmcSurveyQuestionType;
	
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
	public String getCmcSurveySectionTitleEnglish() {
		return cmcSurveySectionTitleEnglish;
	}
	public void setCmcSurveySectionTitleEnglish(String cmcSurveySectionTitleEnglish) {
		this.cmcSurveySectionTitleEnglish = cmcSurveySectionTitleEnglish;
	}
	public String getCmcSurveySectionTitleSpanish() {
		return cmcSurveySectionTitleSpanish;
	}
	public void setCmcSurveySectionTitleSpanish(String cmcSurveySectionTitleSpanish) {
		this.cmcSurveySectionTitleSpanish = cmcSurveySectionTitleSpanish;
	}
	public String getCmcSurveyCategoryEnglish() {
		return cmcSurveyCategoryEnglish;
	}
	public void setCmcSurveyCategoryEnglish(String cmcSurveyCategoryEnglish) {
		this.cmcSurveyCategoryEnglish = cmcSurveyCategoryEnglish;
	}
	public String getCmcSurveyCategorySpanish() {
		return cmcSurveyCategorySpanish;
	}
	public void setCmcSurveyCategorySpanish(String cmcSurveyCategorySpanish) {
		this.cmcSurveyCategorySpanish = cmcSurveyCategorySpanish;
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

	public String getCmcSurveyQuestionType() {
		return cmcSurveyQuestionType;
	}

	public void setCmcSurveyQuestionType(String cmcSurveyQuestionType) {
		this.cmcSurveyQuestionType = cmcSurveyQuestionType;
	}	

	public static class SurveyQuestionComparator implements Comparator<CmcSurveyQuestion> 
	{
		public int compare(CmcSurveyQuestion cmcSurveyQuestion1, CmcSurveyQuestion cmcSurveyQuestion2)
		{
			return cmcSurveyQuestion1.getCmcSurveyQuestionNumber().compareTo(cmcSurveyQuestion2.getCmcSurveyQuestionNumber());
		}		
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
