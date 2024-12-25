package com.pas.pojo;

import java.io.Serializable;

public class DisabilityRow implements Serializable
{
	private static final long serialVersionUID = 131158039169073163L;
	
	private String cmcSurveyCategoryEnglish;
	private String cmcSurveyCategorySpanish;
	private String cmcSurveyID;
	
	private String publicBusAnswer;
	private String publicBusAnswerStyle;	
	private String publicBusAnswerQuestionID;
	
	private String coachBusAnswer;
	private String coachBusAnswerStyle;
	private String coachBusAnswerQuestionID;
	
	private String trainSubwayAnswer;
	private String trainSubwayAnswerStyle;
	private String trainSubwayAnswerQuestionID;
	
	private String taxiAnswer;
	private String taxiAnswerStyle;
	private String taxiAnswerQuestionID;
	
	private String ehailingAnswer;
	private String ehailingAnswerStyle;
	private String ehailingAnswerQuestionID;
	
	private String ferryAnswer;
	private String ferryAnswerStyle;
	private String ferryAnswerQuestionID;
	
	private String walkAnswer;
	private String walkAnswerStyle;
	private String walkAnswerQuestionID;
	
	private String bikeAnswer;
	private String bikeAnswerStyle;
	private String bikeAnswerQuestionID;
	
	private String otherAnswer;
	private String otherAnswerStyle;
	private String otherAnswerQuestionID;
	
	private String powerwheelchairAnswer;
	private String powerwheelchairQuestionID;
	private String powerwheelchairAnswerStyle;
	
	private String manualwheelchairAnswer;
	private String manualwheelchairQuestionID;
	private String manualwheelchairAnswerStyle;
	
	private String rollatorAnswer;
	private String rollatorAnswerQuestionID;
	private String rollatorAnswerStyle;
	
	private String prostheticsAnswer;
	private String prostheticsAnswerQuestionID;
	private String prostheticsAnswerStyle;
	
	private String walkerAnswer;
	private String walkerAnswerQuestionID;
	private String walkerAnswerStyle;
	
	private String crutchescaneAnswer;
	private String crutchescaneAnswerQuestionID;
	private String crutchescaneAnswerStyle;
	
	private String scooterAnswer;
	private String scooterAnswerQuestionID;
	private String scooterAnswerStyle;
	
	private boolean answerRequired = false;	
	
	@Override
	public String toString() 
	{
		String myString = "Survey ID: " + cmcSurveyID + "publicBusAnswer Question ID: " + publicBusAnswerQuestionID;
		return myString;
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

	public String getPublicBusAnswer() {
		return publicBusAnswer;
	}

	public void setPublicBusAnswer(String publicBusAnswer) {
		this.publicBusAnswer = publicBusAnswer;
	}

	public String getCoachBusAnswer() {
		return coachBusAnswer;
	}

	public void setCoachBusAnswer(String coachBusAnswer) {
		this.coachBusAnswer = coachBusAnswer;
	}

	public String getTrainSubwayAnswer() {
		return trainSubwayAnswer;
	}

	public void setTrainSubwayAnswer(String trainSubwayAnswer) {
		this.trainSubwayAnswer = trainSubwayAnswer;
	}

	public String getTaxiAnswer() {
		return taxiAnswer;
	}

	public void setTaxiAnswer(String taxiAnswer) {
		this.taxiAnswer = taxiAnswer;
	}

	public String getEhailingAnswer() {
		return ehailingAnswer;
	}

	public void setEhailingAnswer(String ehailingAnswer) {
		this.ehailingAnswer = ehailingAnswer;
	}

	public String getFerryAnswer() {
		return ferryAnswer;
	}

	public void setFerryAnswer(String ferryAnswer) {
		this.ferryAnswer = ferryAnswer;
	}

	public String getWalkAnswer() {
		return walkAnswer;
	}

	public void setWalkAnswer(String walkAnswer) {
		this.walkAnswer = walkAnswer;
	}

	public String getBikeAnswer() {
		return bikeAnswer;
	}

	public void setBikeAnswer(String bikeAnswer) {
		this.bikeAnswer = bikeAnswer;
	}

	public String getOtherAnswer() {
		return otherAnswer;
	}

	public void setOtherAnswer(String otherAnswer) {
		this.otherAnswer = otherAnswer;
	}

	public String getPublicBusAnswerStyle() {
		return publicBusAnswerStyle;
	}

	public void setPublicBusAnswerStyle(String publicBusAnswerStyle) {
		this.publicBusAnswerStyle = publicBusAnswerStyle;
	}

	public String getCoachBusAnswerStyle() {
		return coachBusAnswerStyle;
	}

	public void setCoachBusAnswerStyle(String coachBusAnswerStyle) {
		this.coachBusAnswerStyle = coachBusAnswerStyle;
	}

	public String getTrainSubwayAnswerStyle() {
		return trainSubwayAnswerStyle;
	}

	public void setTrainSubwayAnswerStyle(String trainSubwayAnswerStyle) {
		this.trainSubwayAnswerStyle = trainSubwayAnswerStyle;
	}

	public String getTaxiAnswerStyle() {
		return taxiAnswerStyle;
	}

	public void setTaxiAnswerStyle(String taxiAnswerStyle) {
		this.taxiAnswerStyle = taxiAnswerStyle;
	}

	public String getEhailingAnswerStyle() {
		return ehailingAnswerStyle;
	}

	public void setEhailingAnswerStyle(String ehailingAnswerStyle) {
		this.ehailingAnswerStyle = ehailingAnswerStyle;
	}

	public String getFerryAnswerStyle() {
		return ferryAnswerStyle;
	}

	public void setFerryAnswerStyle(String ferryAnswerStyle) {
		this.ferryAnswerStyle = ferryAnswerStyle;
	}

	public String getWalkAnswerStyle() {
		return walkAnswerStyle;
	}

	public void setWalkAnswerStyle(String walkAnswerStyle) {
		this.walkAnswerStyle = walkAnswerStyle;
	}

	public String getBikeAnswerStyle() {
		return bikeAnswerStyle;
	}

	public void setBikeAnswerStyle(String bikeAnswerStyle) {
		this.bikeAnswerStyle = bikeAnswerStyle;
	}

	public String getOtherAnswerStyle() {
		return otherAnswerStyle;
	}

	public void setOtherAnswerStyle(String otherAnswerStyle) {
		this.otherAnswerStyle = otherAnswerStyle;
	}

	public boolean isAnswerRequired() {
		return answerRequired;
	}

	public void setAnswerRequired(boolean answerRequired) {
		this.answerRequired = answerRequired;
	}

	public String getPowerwheelchairAnswer() {
		return powerwheelchairAnswer;
	}

	public void setPowerwheelchairAnswer(String powerwheelchairAnswer) {
		this.powerwheelchairAnswer = powerwheelchairAnswer;
	}

	public String getManualwheelchairAnswer() {
		return manualwheelchairAnswer;
	}

	public void setManualwheelchairAnswer(String manualwheelchairAnswer) {
		this.manualwheelchairAnswer = manualwheelchairAnswer;
	}

	public String getRollatorAnswer() {
		return rollatorAnswer;
	}

	public void setRollatorAnswer(String rollatorAnswer) {
		this.rollatorAnswer = rollatorAnswer;
	}

	public String getProstheticsAnswer() {
		return prostheticsAnswer;
	}

	public void setProstheticsAnswer(String prostheticsAnswer) {
		this.prostheticsAnswer = prostheticsAnswer;
	}

	public String getWalkerAnswer() {
		return walkerAnswer;
	}

	public void setWalkerAnswer(String walkerAnswer) {
		this.walkerAnswer = walkerAnswer;
	}

	public String getCrutchescaneAnswer() {
		return crutchescaneAnswer;
	}

	public void setCrutchescaneAnswer(String crutchescaneAnswer) {
		this.crutchescaneAnswer = crutchescaneAnswer;
	}

	public String getScooterAnswer() {
		return scooterAnswer;
	}

	public void setScooterAnswer(String scooterAnswer) {
		this.scooterAnswer = scooterAnswer;
	}

	public String getPowerwheelchairAnswerStyle() {
		return powerwheelchairAnswerStyle;
	}

	public void setPowerwheelchairAnswerStyle(String powerwheelchairAnswerStyle) {
		this.powerwheelchairAnswerStyle = powerwheelchairAnswerStyle;
	}

	public String getManualwheelchairAnswerStyle() {
		return manualwheelchairAnswerStyle;
	}

	public void setManualwheelchairAnswerStyle(String manualwheelchairAnswerStyle) {
		this.manualwheelchairAnswerStyle = manualwheelchairAnswerStyle;
	}

	public String getRollatorAnswerStyle() {
		return rollatorAnswerStyle;
	}

	public void setRollatorAnswerStyle(String rollatorAnswerStyle) {
		this.rollatorAnswerStyle = rollatorAnswerStyle;
	}

	public String getProstheticsAnswerStyle() {
		return prostheticsAnswerStyle;
	}

	public void setProstheticsAnswerStyle(String prostheticsAnswerStyle) {
		this.prostheticsAnswerStyle = prostheticsAnswerStyle;
	}

	public String getWalkerAnswerStyle() {
		return walkerAnswerStyle;
	}

	public void setWalkerAnswerStyle(String walkerAnswerStyle) {
		this.walkerAnswerStyle = walkerAnswerStyle;
	}

	public String getCrutchescaneAnswerStyle() {
		return crutchescaneAnswerStyle;
	}

	public void setCrutchescaneAnswerStyle(String crutchescaneAnswerStyle) {
		this.crutchescaneAnswerStyle = crutchescaneAnswerStyle;
	}

	public String getScooterAnswerStyle() {
		return scooterAnswerStyle;
	}

	public void setScooterAnswerStyle(String scooterAnswerStyle) {
		this.scooterAnswerStyle = scooterAnswerStyle;
	}

	public String getPublicBusAnswerQuestionID() {
		return publicBusAnswerQuestionID;
	}

	public void setPublicBusAnswerQuestionID(String publicBusAnswerQuestionID) {
		this.publicBusAnswerQuestionID = publicBusAnswerQuestionID;
	}

	public String getCoachBusAnswerQuestionID() {
		return coachBusAnswerQuestionID;
	}

	public void setCoachBusAnswerQuestionID(String coachBusAnswerQuestionID) {
		this.coachBusAnswerQuestionID = coachBusAnswerQuestionID;
	}

	public String getTrainSubwayAnswerQuestionID() {
		return trainSubwayAnswerQuestionID;
	}

	public void setTrainSubwayAnswerQuestionID(String trainSubwayAnswerQuestionID) {
		this.trainSubwayAnswerQuestionID = trainSubwayAnswerQuestionID;
	}

	public String getTaxiAnswerQuestionID() {
		return taxiAnswerQuestionID;
	}

	public void setTaxiAnswerQuestionID(String taxiAnswerQuestionID) {
		this.taxiAnswerQuestionID = taxiAnswerQuestionID;
	}

	public String getEhailingAnswerQuestionID() {
		return ehailingAnswerQuestionID;
	}

	public void setEhailingAnswerQuestionID(String ehailingAnswerQuestionID) {
		this.ehailingAnswerQuestionID = ehailingAnswerQuestionID;
	}

	public String getFerryAnswerQuestionID() {
		return ferryAnswerQuestionID;
	}

	public void setFerryAnswerQuestionID(String ferryAnswerQuestionID) {
		this.ferryAnswerQuestionID = ferryAnswerQuestionID;
	}

	public String getWalkAnswerQuestionID() {
		return walkAnswerQuestionID;
	}

	public void setWalkAnswerQuestionID(String walkAnswerQuestionID) {
		this.walkAnswerQuestionID = walkAnswerQuestionID;
	}
	
	public String getBikeAnswerQuestionID() {
		return bikeAnswerQuestionID;
	}

	public void setBikeAnswerQuestionID(String bikeAnswerQuestionID) {
		this.bikeAnswerQuestionID = bikeAnswerQuestionID;
	}

	public String getOtherAnswerQuestionID() {
		return otherAnswerQuestionID;
	}

	public void setOtherAnswerQuestionID(String otherAnswerQuestionID) {
		this.otherAnswerQuestionID = otherAnswerQuestionID;
	}

	public String getPowerwheelchairQuestionID() {
		return powerwheelchairQuestionID;
	}

	public void setPowerwheelchairQuestionID(String powerwheelchairQuestionID) {
		this.powerwheelchairQuestionID = powerwheelchairQuestionID;
	}

	public String getManualwheelchairQuestionID() {
		return manualwheelchairQuestionID;
	}

	public void setManualwheelchairQuestionID(String manualwheelchairQuestionID) {
		this.manualwheelchairQuestionID = manualwheelchairQuestionID;
	}

	public String getRollatorAnswerQuestionID() {
		return rollatorAnswerQuestionID;
	}

	public void setRollatorAnswerQuestionID(String rollatorAnswerQuestionID) {
		this.rollatorAnswerQuestionID = rollatorAnswerQuestionID;
	}

	public String getProstheticsAnswerQuestionID() {
		return prostheticsAnswerQuestionID;
	}

	public void setProstheticsAnswerQuestionID(String prostheticsAnswerQuestionID) {
		this.prostheticsAnswerQuestionID = prostheticsAnswerQuestionID;
	}

	public String getWalkerAnswerQuestionID() {
		return walkerAnswerQuestionID;
	}

	public void setWalkerAnswerQuestionID(String walkerAnswerQuestionID) {
		this.walkerAnswerQuestionID = walkerAnswerQuestionID;
	}

	public String getCrutchescaneAnswerQuestionID() {
		return crutchescaneAnswerQuestionID;
	}

	public void setCrutchescaneAnswerQuestionID(String crutchescaneAnswerQuestionID) {
		this.crutchescaneAnswerQuestionID = crutchescaneAnswerQuestionID;
	}

	public String getScooterAnswerQuestionID() {
		return scooterAnswerQuestionID;
	}

	public void setScooterAnswerQuestionID(String scooterAnswerQuestionID) {
		this.scooterAnswerQuestionID = scooterAnswerQuestionID;
	}

	public String getCmcSurveyID() {
		return cmcSurveyID;
	}

	public void setCmcSurveyID(String cmcSurveyID) {
		this.cmcSurveyID = cmcSurveyID;
	}
	
}
