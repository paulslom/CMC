package com.pas.beans;

import java.io.Serializable;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class CmcDiagnosis implements Serializable
{
	private static final long serialVersionUID = 131158039169073163L;
	
	private String cmcDiagnosisId;
	private String cmcSurveyId;
	private String cmcSurveyName;
	private String cmcDiagnosis;	
	
	@Override
	public String toString() 
	{
		String myString = "diagnosis ID: " + cmcDiagnosisId + " surveyId: " + cmcSurveyId + " survey name: " + cmcSurveyName + " diagnosis " + cmcDiagnosis;
		return myString;
	}

	@DynamoDbPartitionKey
	public String getCmcDiagnosisId() {
		return cmcDiagnosisId;
	}


	public void setCmcDiagnosisId(String cmcDiagnosisId) {
		this.cmcDiagnosisId = cmcDiagnosisId;
	}


	public String getCmcSurveyId() {
		return cmcSurveyId;
	}


	public void setCmcSurveyId(String cmcSurveyId) {
		this.cmcSurveyId = cmcSurveyId;
	}


	public String getCmcSurveyName() {
		return cmcSurveyName;
	}


	public void setCmcSurveyName(String cmcSurveyName) {
		this.cmcSurveyName = cmcSurveyName;
	}


	public String getCmcDiagnosis() {
		return cmcDiagnosis;
	}


	public void setCmcDiagnosis(String cmcDiagnosis) {
		this.cmcDiagnosis = cmcDiagnosis;
	}

	
	
	
}
