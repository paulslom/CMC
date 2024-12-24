package com.pas.pojo;

import java.io.Serializable;

public class DisabilityRow implements Serializable
{
	private static final long serialVersionUID = 131158039169073163L;
	
	private String cmcSurveyQuestionID;
	private String cmcSurveyCategoryEnglish;
	private String cmcSurveyCategorySpanish;
	private String publicBusAnswer;
	private String publicBusAnswerStyle;
	private String coachBusAnswer;
	private String coachBusAnswerStyle;
	private String trainSubwayAnswer;
	private String trainSubwayAnswerStyle;
	private String taxiAnswer;
	private String taxiAnswerStyle;
	private String ehailingAnswer;
	private String ehailingAnswerStyle;
	private String ferryAnswer;
	private String ferryAnswerStyle;
	private String walkAnswer;
	private String walkAnswerStyle;
	private String bikeAnswer;
	private String bikeAnswerStyle;
	private String otherAnswer;
	private String otherAnswerStyle;
	private String powerwheelchairAnswer;
	private String manualwheelchairAnswer;
	private String rollatorAnswer;
	private String prostheticsAnswer;
	private String walkerAnswer;
	private String crutchescaneAnswer;
	private String scooterAnswer;
	private String powerwheelchairAnswerStyle;
	private String manualwheelchairAnswerStyle;
	private String rollatorAnswerStyle;
	private String prostheticsAnswerStyle;
	private String walkerAnswerStyle;
	private String crutchescaneAnswerStyle;
	private String scooterAnswerStyle;
	
	private boolean answerRequired = false;	
	
	@Override
	public String toString() 
	{
		String myString = "Survey Question ID: " + cmcSurveyQuestionID;
		return myString;
	}

	public String getCmcSurveyQuestionID() {
		return cmcSurveyQuestionID;
	}

	public void setCmcSurveyQuestionID(String cmcSurveyQuestionID) {
		this.cmcSurveyQuestionID = cmcSurveyQuestionID;
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
	
	
	
}
