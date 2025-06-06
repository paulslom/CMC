package com.pas.pojo;

import com.pas.beans.CmcSurveyQuestion;

//This is the key class that programs the results page!

public class QuestionSkip 
{
	boolean doPublicBus = true;
	boolean doCoachBus = true;
	boolean doTrainSubway = true;
	boolean doTaxi = true;
	boolean doEhailing = true;
	boolean doFerry = true;
	boolean doWalking = true;
	boolean doBikeScooter = true;
	
	public void setSkipQuestion(CmcSurveyQuestion cmcsq, String survey) 
	{
		if (survey.equalsIgnoreCase("physical"))
		{
			doPhysicalQuestions(cmcsq);
		}
		else if (survey.equalsIgnoreCase("intellectual"))
		{
			doIntellectualQuestions(cmcsq);
		}
		else if (survey.equalsIgnoreCase("autism"))
		{
			doAutismQuestions(cmcsq);
		}				
	}	

	private void doPhysicalQuestions(CmcSurveyQuestion cmcsq) 
	{
		if (cmcsq.getCmcSurveyQuestionEnglish().contains("1."))
		{
			doTaxi = false;
			doEhailing = false;
			doWalking = false;
			doBikeScooter = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("2."))
		{
			doTaxi = false;
			doEhailing = false;
			doCoachBus = false;
			doBikeScooter = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("3."))
		{
			//all questions relevant
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("4."))
		{
			doFerry = false;
			doWalking = false;
			doBikeScooter = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("5."))
		{
			doCoachBus = false;
			doTrainSubway = false;
			doTaxi = false;
			doEhailing = false;
			doFerry = false;	
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("6."))
		{
			doPublicBus = false;
			doCoachBus = false;
			doTrainSubway = false;
			doTaxi = false;
			doEhailing = false;
			doFerry = false;	
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("7."))
		{
			doPublicBus = false;
			doCoachBus = false;
			doTrainSubway = false;				
			doFerry = false;	
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("8."))
		{
			doPublicBus = false;
			doCoachBus = false;
			doTrainSubway = false;
			doFerry = false;	
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("9."))
		{
			doBikeScooter = false;
			doWalking = false;
		}
		if (cmcsq.getCmcSurveyQuestionEnglish().contains("10."))
		{
			doTaxi = false;
			doEhailing = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("11."))
		{
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("12."))
		{
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("13."))
		{
			//all questions relevant
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("14."))
		{
			doCoachBus = false;
			doTaxi = false;
			doEhailing = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("15."))
		{
			//all questions relevant
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("16."))
		{
			doTaxi = false;
			doEhailing = false;
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("17."))
		{
			doTaxi = false;
			doEhailing = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("18."))
		{
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("19."))
		{
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("20."))
		{
			//all questions relevant
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("21."))
		{
			//all questions relevant
		}			
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("22."))
		{
			doCoachBus = false;
			doTrainSubway = false;
			doEhailing = false;
			doFerry = false;	
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("23."))
		{
			doTrainSubway = false;
			doFerry = false;	
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("24."))
		{
			doPublicBus = false;
			doCoachBus = false;
			doTrainSubway = false;
			doFerry = false;	
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("25."))
		{
			doPublicBus = false;
			doCoachBus = false;
			doTrainSubway = false;
			doFerry = false;	
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("26."))
		{
			//all questions relevant
		}			
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("27."))
		{
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("28."))
		{
			doTaxi = false;
			doEhailing = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("29."))
		{
			doEhailing = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("30."))
		{
			//all questions relevant
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("31."))
		{
			//all questions relevant
		}
		
	}

	private void doIntellectualQuestions(CmcSurveyQuestion cmcsq) 
	{
		if (cmcsq.getCmcSurveyQuestionEnglish().contains("1."))
		{
			//all questions relevant
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("2."))
		{
			doEhailing = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("3."))
		{
			doTaxi = false;
			doEhailing = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("4."))
		{
			//all questions relevant
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("5."))
		{
			doPublicBus = false;
			doCoachBus = false;
			doTrainSubway = false;
			doFerry = false;	
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("6."))
		{
			doTaxi = false;
			doEhailing = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("7."))
		{
			//all questions relevant
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("8."))
		{
			doTaxi = false;
			doEhailing = false;
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("9."))
		{
			doTaxi = false;
			doEhailing = false;
		}
		if (cmcsq.getCmcSurveyQuestionEnglish().contains("10."))
		{
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("11."))
		{
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("12."))
		{
			doCoachBus = false;
			doTaxi = false;
			doEhailing = false;
			doFerry = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("13."))
		{
			doTaxi = false;
			doEhailing = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("14."))
		{
			doCoachBus = false;
			doTrainSubway = false;
			doTaxi = false;
			doEhailing = false;
			doFerry = false;	
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("15."))
		{
			doPublicBus = false;
			doCoachBus = false;
			doTrainSubway = false;
			doTaxi = false;
			doEhailing = false;
			doFerry = false;	
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("16."))
		{
			//all questions relevant
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("17."))
		{
			doCoachBus = false;
			doTrainSubway = false;
			doEhailing = false;
			doFerry = false;	
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("18."))
		{
			doTrainSubway = false;
			doFerry = false;	
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("19."))
		{
			doPublicBus = false;
			doCoachBus = false;
			doTrainSubway = false;
			doFerry = false;	
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("20."))
		{
			doPublicBus = false;
			doCoachBus = false;
			doTrainSubway = false;
			doFerry = false;	
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("21."))
		{
			//all questions relevant
		}			
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("22."))
		{
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("23."))
		{
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("24."))
		{
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("25."))
		{
			//all questions relevant
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("26."))
		{
			//all questions relevant
		}			
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("27."))
		{
			doTaxi = false;
			doEhailing = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("28."))
		{
			//all questions relevant
		}
	}
	
	private void doAutismQuestions(CmcSurveyQuestion cmcsq) 
	{		
		doCoachBus = false; //no longer a relevant question for Autism
		
		if (cmcsq.getCmcSurveyQuestionEnglish().contains("1."))
		{
			//all questions relevant
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("2."))
		{
			doTaxi = false;
			doEhailing = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("3."))
		{
			doPublicBus = false;
			doTrainSubway = false;
			doFerry = false;	
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("4."))
		{
			doPublicBus = false;
			doTrainSubway = false;
			doFerry = false;	
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("5."))
		{
			doTaxi = false;
			doEhailing = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("6."))
		{
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("7."))
		{
			doTaxi = false;
			doEhailing = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("8."))
		{
			doTaxi = false;
			doEhailing = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("9."))
		{
			doTaxi = false;
			doEhailing = false;
			doFerry = false;
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("10."))
		{
			doFerry = false;
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("11."))
		{
			doTaxi = false;
			doEhailing = false;
			doFerry = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("12."))
		{
			doTaxi = false;
			doEhailing = false;
			doFerry = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("13."))
		{
			doTrainSubway = false;
			doFerry = false;
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("14."))
		{
			//all question relevant
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("15."))
		{
			doTrainSubway = false;
			doFerry = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("16."))
		{
			//all questions relevant
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("17."))
		{
			doTaxi = false;
			doEhailing = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("18."))
		{
			doTrainSubway = false;
			doFerry = false;
			doBikeScooter = false;
			doWalking = false;
			doTaxi = false;
			doEhailing = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("19."))
		{
			//all questions relevant
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("20."))
		{
			doTaxi = false;
			doEhailing = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("21."))
		{
			doEhailing = false;
			doBikeScooter = false;
			doWalking = false;
		}			
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("22."))
		{
			doPublicBus = false;
			doTrainSubway = false;
			doFerry = false;
			doBikeScooter = false;
			doWalking = false;			
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("23."))
		{
			doBikeScooter = false;
			doWalking = false;
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("24."))
		{
			//all questions relevant
		}
		else if (cmcsq.getCmcSurveyQuestionEnglish().contains("25."))
		{
			//all questions relevant
		}	
	
	}
	
	public boolean isDoPublicBus() {
		return doPublicBus;
	}
	public void setDoPublicBus(boolean doPublicBus) {
		this.doPublicBus = doPublicBus;
	}
	public boolean isDoCoachBus() {
		return doCoachBus;
	}
	public void setDoCoachBus(boolean doCoachBus) {
		this.doCoachBus = doCoachBus;
	}
	public boolean isDoTrainSubway() {
		return doTrainSubway;
	}
	public void setDoTrainSubway(boolean doTrainSubway) {
		this.doTrainSubway = doTrainSubway;
	}
	public boolean isDoTaxi() {
		return doTaxi;
	}
	public void setDoTaxi(boolean doTaxi) {
		this.doTaxi = doTaxi;
	}
	public boolean isDoEhailing() {
		return doEhailing;
	}
	public void setDoEhailing(boolean doEhailing) {
		this.doEhailing = doEhailing;
	}
	public boolean isDoFerry() {
		return doFerry;
	}
	public void setDoFerry(boolean doFerry) {
		this.doFerry = doFerry;
	}
	public boolean isDoWalking() {
		return doWalking;
	}
	public void setDoWalking(boolean doWalking) {
		this.doWalking = doWalking;
	}
	public boolean isDoBikeScooter() {
		return doBikeScooter;
	}
	public void setDoBikeScooter(boolean doBikeScooter) {
		this.doBikeScooter = doBikeScooter;
	}
	
}
