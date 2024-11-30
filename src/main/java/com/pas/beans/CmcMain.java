package com.pas.beans;

import java.io.Serializable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.component.selectoneradio.SelectOneRadio;

import com.pas.dao.CmcSurveyAnswersDAO;
import com.pas.dao.CmcSurveyQuestionsDAO;
import com.pas.dao.CmcSurveysDAO;
import com.pas.dynamodb.DynamoClients;
import com.pas.dynamodb.DynamoUtil;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Named;

@Named("pc_CmcMain")
@SessionScoped
public class CmcMain implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(CmcMain.class);	
		
	private final double id = Math.random();	
	
	private static int ENGLISH = 1;
	private static int SPANISH = 2;
	
	private static String Site_Title_English = "Mobility Match: Connecting you with Best Choices of Community Mobility by Occupational Therapists";
	private static String Site_Title_Spanish = "Spanish version of Mobility Match Title";
	private static String Physical_Disablities_Page_Title_Spanish = "Habilidades de Movilidad";
	private static String Physical_Disablities_Page_Title_English = "Physical Disabilities";
	private static String Intellectual_Disablities_Page_Title_Spanish = "Habilidades de Intelectual";
	private static String Intellectual_Disablities_Page_Title_English = "Intellectual Disabilities";
	private static String Autism_Disorder_Page_Title_Spanish = "Trastorno del Autismo";
	private static String Autism_Disorder_Page_Title_English = "Autism Spectrum Disorder";
	
	private int siteLanguage = ENGLISH;
		
	private boolean siteInEnglish = true;
	private String siteTitle;
	private String physicalDisabilitiesPageTitle;
	private String intellectualDisabilitiesPageTitle;
	private String autismDisorderPageTitle;
	
	private CmcSurveyQuestionsDAO cmcSurveyQuestionsDAO;
	private CmcSurveysDAO cmcSurveysDAO;
	private CmcSurveyAnswersDAO cmcSurveyAnswersDAO;
	
	public CmcMain() 
	{
		logger.info("Entering CmcMain constructor.  Should only be here ONE time");	
		logger.info("CmcMain id is: " + this.getId());
		logger.info("Setting default to English");
		
		//changeToEnglish();
		changeToSpanish();
		
		try 
		{
			//this gets populated at app startup, no need to do it again when someone logs in.
			if (cmcSurveysDAO == null || cmcSurveysDAO.getFullSurveysMap().isEmpty())
			{
				DynamoClients dynamoClients = DynamoUtil.getDynamoClients();
				
				loadCmcSurveys(dynamoClients);
				loadCmcSurveyQuestions(dynamoClients);
				loadCmcSurveyAnswers(dynamoClients);
			}	
		} 
		catch (Exception e) 
		{
			logger.error(e.getMessage(), e);
		}		
	}

	public void valueChangeLanguageRadio(AjaxBehaviorEvent event)
	{ 	
		logger.info("Changing site language");
	
		SelectOneRadio selectOneRadio = (SelectOneRadio)event.getSource();
		Integer selectedOption = (Integer)selectOneRadio.getValue();
		
		if (selectedOption == ENGLISH)  
		{ 			
			changeToEnglish();
		}
		else //means Spanish chosen
		{			
			changeToSpanish();
        }		
		
    }
	
	private void changeToSpanish()
	{
		this.setSiteLanguage(SPANISH);
		this.setSiteInEnglish(false);
		this.setSiteTitle(Site_Title_Spanish);
		this.setPhysicalDisabilitiesPageTitle(Physical_Disablities_Page_Title_Spanish);
		this.setAutismDisorderPageTitle(Autism_Disorder_Page_Title_Spanish);
		this.setIntellectualDisabilitiesPageTitle(Intellectual_Disablities_Page_Title_Spanish);
		logger.info("Site language set to Spanish");
	}
	
	private void changeToEnglish()
	{
		this.setSiteLanguage(ENGLISH);
		this.setSiteInEnglish(true);
		this.setSiteTitle(Site_Title_English);
		this.setPhysicalDisabilitiesPageTitle(Physical_Disablities_Page_Title_English);
		this.setAutismDisorderPageTitle(Autism_Disorder_Page_Title_English);
		this.setIntellectualDisabilitiesPageTitle(Intellectual_Disablities_Page_Title_English);
		logger.info("Site language set to English");
	}
	
	public void loadCmcSurveys(DynamoClients dynamoClients)  throws Exception
	{
		logger.info("entering loadCmcSurveys");
		cmcSurveysDAO = new CmcSurveysDAO(dynamoClients);
		cmcSurveysDAO.readAllSurveysFromDB(); 
		logger.info("Cmc Surveys read in. List size = " + cmcSurveysDAO.getFullSurveysList().size());		
    }
	
	public void loadCmcSurveyQuestions(DynamoClients dynamoClients)  throws Exception
	{
		logger.info("entering loadCmcSurveyQuestions");
		cmcSurveyQuestionsDAO = new CmcSurveyQuestionsDAO(dynamoClients);
		cmcSurveyQuestionsDAO.readAllSurveyQuestionsFromDB(); 
		logger.info("Cmc Survey Questions read in. List size = " + cmcSurveyQuestionsDAO.getFullSurveyQuestionsList().size());		
    }
	
	public void loadCmcSurveyAnswers(DynamoClients dynamoClients)  throws Exception
	{
		logger.info("entering loadCmcSurveyAnswers");		
		cmcSurveyAnswersDAO = new CmcSurveyAnswersDAO(dynamoClients);
		cmcSurveyAnswersDAO.readAllSurveyAnswersFromDB();			
		logger.info("Cmc Survey Answers read in. List size = " + cmcSurveyAnswersDAO.getFullSurveyAnswersList().size());		
    }
	
	public double getId() {
		return id;
	}

	public CmcSurveyQuestionsDAO getCmcSurveyQuestionsDAO() 
	{
		return cmcSurveyQuestionsDAO;
	}

	public void setCmcSurveyQuestionsDAO(CmcSurveyQuestionsDAO cmcSurveyQuestionsDAO) 
	{
		this.cmcSurveyQuestionsDAO = cmcSurveyQuestionsDAO;
	}

	public CmcSurveysDAO getCmcSurveysDAO() 
	{
		return cmcSurveysDAO;
	}

	public void setCmcSurveysDAO(CmcSurveysDAO cmcSurveysDAO) 
	{
		this.cmcSurveysDAO = cmcSurveysDAO;
	}

	public CmcSurveyAnswersDAO getCmcSurveyAnswersDAO() 
	{
		return cmcSurveyAnswersDAO;
	}

	public void setCmcSurveyAnswersDAO(CmcSurveyAnswersDAO cmcSurveyAnswersDAO) 
	{
		this.cmcSurveyAnswersDAO = cmcSurveyAnswersDAO;
	}
	
	public List<CmcSurvey> getFullSurveysList() 
	{
		return cmcSurveysDAO.getFullSurveysList();
	}
	
	public List<CmcSurveyQuestion> getFullSurveyQuestionsList() 
	{
		return cmcSurveyQuestionsDAO.getFullSurveyQuestionsList();
	}
	
	public List<CmcSurveyQuestion> getPhysicalDisabilitiesSection1QuestionsList() 
	{
		return cmcSurveyQuestionsDAO.getPhysicalDisabilitiesSection1QuestionsList();
	}
	
	public List<CmcSurveyQuestion> getPhysicalDisabilitiesSection2QuestionsList() 
	{
		return cmcSurveyQuestionsDAO.getPhysicalDisabilitiesSection2QuestionsList();
	}
	
	public List<CmcSurveyQuestion> getPhysicalDisabilitiesSection3QuestionsList() 
	{
		return cmcSurveyQuestionsDAO.getPhysicalDisabilitiesSection3QuestionsList();
	}

	public int getSiteLanguage() {
		return siteLanguage;
	}

	public void setSiteLanguage(int siteLanguage) {
		this.siteLanguage = siteLanguage;
	}

	public boolean isSiteInEnglish() {
		return siteInEnglish;
	}

	public void setSiteInEnglish(boolean siteInEnglish) {
		this.siteInEnglish = siteInEnglish;
	}

	public String getPhysicalDisabilitiesPageTitle() {
		return physicalDisabilitiesPageTitle;
	}

	public void setPhysicalDisabilitiesPageTitle(String physicalDisabilitiesPageTitle) {
		this.physicalDisabilitiesPageTitle = physicalDisabilitiesPageTitle;
	}

	public String getIntellectualDisabilitiesPageTitle() {
		return intellectualDisabilitiesPageTitle;
	}

	public void setIntellectualDisabilitiesPageTitle(String intellectualDisabilitiesPageTitle) {
		this.intellectualDisabilitiesPageTitle = intellectualDisabilitiesPageTitle;
	}

	public String getAutismDisorderPageTitle() {
		return autismDisorderPageTitle;
	}

	public void setAutismDisorderPageTitle(String autismDisorderPageTitle) {
		this.autismDisorderPageTitle = autismDisorderPageTitle;
	}

	public String getSiteTitle() {
		return siteTitle;
	}

	public void setSiteTitle(String siteTitle) {
		this.siteTitle = siteTitle;
	}
		
}
