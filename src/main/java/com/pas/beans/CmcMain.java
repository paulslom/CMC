package com.pas.beans;

import java.io.Serializable;
import java.util.ArrayList;
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
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.model.SelectItem;
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
	private static String Site_Title_Spanish = "Mobility Match: lo conectamos con las mejores opciones de movilidad comunitaria a través de terapeutas ocupacionales";
	
	public static String Physical_Disabilities_Survey_Name = "Physical Disabilities";
	public static String Intellectual_Disabilities_Survey_Name = "Intellectual Disabilities";
	public static String Autism_Disorder_Survey_Name = "Autism Spectrum Disorder";
	
	private int siteLanguage = ENGLISH;
		
	private boolean siteInEnglish = true;
	
	private static String Physical_Disabilities_Menu_Title_English;
	private static String Physical_Disabilities_Page_Title_English;
	private static String Physical_Disabilities_Page_SubTitle_English;
	private static String Physical_Disabilities_Menu_Title_Spanish;
	private static String Physical_Disabilities_Page_Title_Spanish;	
	private static String Physical_Disabilities_Page_SubTitle_Spanish;
	
	private static String Intellectual_Disabilities_Menu_Title_English;
	private static String Intellectual_Disabilities_Page_Title_English;
	private static String Intellectual_Disabilities_Page_SubTitle_English;
	private static String Intellectual_Disabilities_Menu_Title_Spanish;
	private static String Intellectual_Disabilities_Page_Title_Spanish;	
	private static String Intellectual_Disabilities_Page_SubTitle_Spanish;
	
	private static String Autism_Disorder_Menu_Title_English;
	private static String Autism_Disorder_Page_Title_English;
	private static String Autism_Disorder_Page_SubTitle_English;
	private static String Autism_Disorder_Menu_Title_Spanish;
	private static String Autism_Disorder_Page_Title_Spanish;	
	private static String Autism_Disorder_Page_SubTitle_Spanish;
	
	private List<SelectItem> yesNoEnglishList = new ArrayList<>();
	private List<SelectItem> yesNoSpanishList = new ArrayList<>();
	
	private List<SelectItem> skillLevelEnglishList = new ArrayList<>();
	private List<SelectItem> skillLevelSpanishList = new ArrayList<>();
	
	private String siteTitle;
	
	private boolean renderDefaultAnswers = true;
	
	private String physicalDisabilitiesMenuTitle;
	private String physicalDisabilitiesPageTitle;
	private String physicalDisabilitiesPageSubTitle;
	
	private String intellectualDisabilitiesMenuTitle;
	private String intellectualDisabilitiesPageTitle;
	private String intellectualDisabilitiesPageSubTitle;
	
	private String autismDisorderMenuTitle;
	private String autismDisorderPageTitle;
	private String autismDisorderPageSubTitle;
	
	private CmcSurveyQuestionsDAO cmcSurveyQuestionsDAO;
	private CmcSurveysDAO cmcSurveysDAO;
	private CmcSurveyAnswersDAO cmcSurveyAnswersDAO;
		
	public CmcMain() 
	{
		logger.info("Entering CmcMain constructor.  Should only be here ONE time");	
		logger.info("CmcMain id is: " + this.getId());
		logger.info("Setting default to English");
		
		SelectItem si = new SelectItem("","Select");
		yesNoEnglishList.add(si);
		si = new SelectItem("Yes","Yes");
		yesNoEnglishList.add(si);
		si = new SelectItem("No","No");
		yesNoEnglishList.add(si);
		
		si = new SelectItem("","Seleccionar");
		yesNoSpanishList.add(si);
		si = new SelectItem("Yes","Si");
		yesNoSpanishList.add(si);
		si = new SelectItem("No","No");
		yesNoSpanishList.add(si);
		
		si = new SelectItem("","Select");
		skillLevelEnglishList.add(si);
		si = new SelectItem("1","Performs Independently");
		skillLevelEnglishList.add(si);
		si = new SelectItem("2","Potentially Able with Training");
		skillLevelEnglishList.add(si);
		si = new SelectItem("3","Unable to Meet Demands");
		skillLevelEnglishList.add(si);
		
		si = new SelectItem("","Seleccionar");
		skillLevelSpanishList.add(si);
		si = new SelectItem("1","Lo realiza de manera independiente");
		skillLevelSpanishList.add(si);
		si = new SelectItem("2","Lo puede realizar con entrenamiento");
		skillLevelSpanishList.add(si);
		si = new SelectItem("3","No puede cumplir con las demandas");
		skillLevelSpanishList.add(si);
		
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
			
			changeToEnglish();
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
		
		this.setPhysicalDisabilitiesMenuTitle(Physical_Disabilities_Menu_Title_Spanish);
		this.setPhysicalDisabilitiesPageTitle(Physical_Disabilities_Page_Title_Spanish);
		this.setPhysicalDisabilitiesPageSubTitle(Physical_Disabilities_Page_SubTitle_Spanish);
		
		this.setIntellectualDisabilitiesMenuTitle(Intellectual_Disabilities_Menu_Title_Spanish);
		this.setIntellectualDisabilitiesPageTitle(Intellectual_Disabilities_Page_Title_Spanish);
		this.setIntellectualDisabilitiesPageSubTitle(Intellectual_Disabilities_Page_SubTitle_Spanish);
		
		this.setAutismDisorderMenuTitle(Autism_Disorder_Menu_Title_Spanish);
		this.setAutismDisorderPageTitle(Autism_Disorder_Page_Title_Spanish);
		this.setAutismDisorderPageSubTitle(Autism_Disorder_Page_SubTitle_Spanish);
		
		logger.info("Site language set to Spanish");
	}
	
	private void changeToEnglish()
	{
		this.setSiteLanguage(ENGLISH);
		this.setSiteInEnglish(true);
		this.setSiteTitle(Site_Title_English);
		
		this.setPhysicalDisabilitiesMenuTitle(Physical_Disabilities_Menu_Title_English);
		this.setPhysicalDisabilitiesPageTitle(Physical_Disabilities_Page_Title_English);
		this.setPhysicalDisabilitiesPageSubTitle(Physical_Disabilities_Page_SubTitle_English);
		
		this.setIntellectualDisabilitiesMenuTitle(Intellectual_Disabilities_Menu_Title_English);
		this.setIntellectualDisabilitiesPageTitle(Intellectual_Disabilities_Page_Title_English);
		this.setIntellectualDisabilitiesPageSubTitle(Intellectual_Disabilities_Page_SubTitle_English);
		
		this.setAutismDisorderMenuTitle(Autism_Disorder_Menu_Title_English);
		this.setAutismDisorderPageTitle(Autism_Disorder_Page_Title_English);
		this.setAutismDisorderPageSubTitle(Autism_Disorder_Page_SubTitle_English);
		
		logger.info("Site language set to English");
	}
		
	public String submitPhysicalDisabilityAnswers() throws Exception
	{
		logger.info("entering submitPhysicalDisabilityAnswers");
		
		if (validateAnswers(this.getPhysicalDisabilitiesSection1QuestionsList()) 
		||  validateAnswers(this.getPhysicalDisabilitiesSection2QuestionsList())
		||  validateAnswers(this.getPhysicalDisabilitiesSection3QuestionsList())) //will be true if errors
		{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Please answer all questions",null);
	        FacesContext.getCurrentInstance().addMessage(null, msg);    
			return "";
		}
		
		for (int i = 0; i < this.getPhysicalDisabilitiesSection1QuestionsList().size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = this.getPhysicalDisabilitiesSection1QuestionsList().get(i);
			CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();
			cmcSurveyAnswer.setSurveyQuestionID(cmcSurveyQuestion.getCmcSurveyQuestionID());
			cmcSurveyAnswer.setSurveyID(cmcSurveyQuestion.getCmcSurveyID());
			cmcSurveyAnswer.setSurveyAnswer(cmcSurveyQuestion.getAnswer());
			cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);
	    }
		
		for (int i = 0; i < this.getPhysicalDisabilitiesSection2QuestionsList().size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = this.getPhysicalDisabilitiesSection2QuestionsList().get(i);
			CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();
			cmcSurveyAnswer.setSurveyQuestionID(cmcSurveyQuestion.getCmcSurveyQuestionID());
			cmcSurveyAnswer.setSurveyID(cmcSurveyQuestion.getCmcSurveyID());
			cmcSurveyAnswer.setSurveyAnswer(cmcSurveyQuestion.getAnswer());
			cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);
	    }
		
		for (int i = 0; i < this.getPhysicalDisabilitiesSection3QuestionsList().size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = this.getPhysicalDisabilitiesSection3QuestionsList().get(i);
			CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();
			cmcSurveyAnswer.setSurveyQuestionID(cmcSurveyQuestion.getCmcSurveyQuestionID());
			cmcSurveyAnswer.setSurveyID(cmcSurveyQuestion.getCmcSurveyID());
			cmcSurveyAnswer.setSurveyAnswer(cmcSurveyQuestion.getAnswer());
			cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);
	    }
		
		return "";
	}
	
	private boolean validateAnswers(List<CmcSurveyQuestion> tempList)
	{
		boolean errorFound = false;
		
		for (int i = 0; i < tempList.size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = tempList.get(i);
			if (cmcSurveyQuestion.getAnswer() == null || cmcSurveyQuestion.getAnswer().equalsIgnoreCase("Select"))
			{
				errorFound = true;
			}			
	    }
		
		return errorFound;				
	}
	              
	public String clearAllPhysicalDisabilityAnswers()
	{
		logger.info("entering clearAllPhysicalDisabilityAnswers");
		
		for (int i = 0; i < this.getPhysicalDisabilitiesSection1QuestionsList().size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = this.getPhysicalDisabilitiesSection1QuestionsList().get(i);
			cmcSurveyQuestion.setAnswer("Select");
	    }
		
		for (int i = 0; i < this.getPhysicalDisabilitiesSection2QuestionsList().size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = this.getPhysicalDisabilitiesSection2QuestionsList().get(i);
			cmcSurveyQuestion.setAnswer("Select");
	    }
		
		for (int i = 0; i < this.getPhysicalDisabilitiesSection3QuestionsList().size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = this.getPhysicalDisabilitiesSection3QuestionsList().get(i);
			cmcSurveyQuestion.setAnswer("Select");
	    }
		
		return "";
	}
	
	public String defaultAllPhysicalDisabilityAnswers()
	{
		logger.info("entering defaultAllPhysicalDisabilityAnswers");
		
		for (int i = 0; i < this.getPhysicalDisabilitiesSection1QuestionsList().size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = this.getPhysicalDisabilitiesSection1QuestionsList().get(i);
			cmcSurveyQuestion.setAnswer("Yes");
	    }
		
		for (int i = 0; i < this.getPhysicalDisabilitiesSection2QuestionsList().size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = this.getPhysicalDisabilitiesSection2QuestionsList().get(i);
			cmcSurveyQuestion.setAnswer("No");
	    }
		
		for (int i = 0; i < this.getPhysicalDisabilitiesSection3QuestionsList().size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = this.getPhysicalDisabilitiesSection3QuestionsList().get(i);
			cmcSurveyQuestion.setAnswer("2");
	    }
		
		return "";
		
	}
	
	public void loadCmcSurveys(DynamoClients dynamoClients)  throws Exception
	{
		logger.info("entering loadCmcSurveys");
		cmcSurveysDAO = new CmcSurveysDAO(dynamoClients);
		List<CmcSurvey> tempList = cmcSurveysDAO.readAllSurveysFromDB(); 
		logger.info("Cmc Surveys read in. List size = " + cmcSurveysDAO.getFullSurveysList().size());
		
		for (int i = 0; i < tempList.size(); i++) 
	    {
			CmcSurvey cmcSurvey = tempList.get(i);
			
			if (cmcSurvey.getCmcSurveyName().equalsIgnoreCase(Physical_Disabilities_Survey_Name))
			{
				Physical_Disabilities_Menu_Title_English = cmcSurvey.getCmcSurveyName();
				Physical_Disabilities_Page_Title_English = cmcSurvey.getCmcSurveyPageTitleEnglish();
				Physical_Disabilities_Page_SubTitle_English = cmcSurvey.getCmcSurveyPageSubTitleEnglish();
				Physical_Disabilities_Menu_Title_Spanish = cmcSurvey.getCmcSurveyNameSpanish();
				Physical_Disabilities_Page_Title_Spanish = cmcSurvey.getCmcSurveyPageTitleSpanish();
				Physical_Disabilities_Page_SubTitle_Spanish = cmcSurvey.getCmcSurveyPageSubTitleSpanish();				
			}
			else if (cmcSurvey.getCmcSurveyName().equalsIgnoreCase(Intellectual_Disabilities_Survey_Name))
			{
				Intellectual_Disabilities_Menu_Title_English = cmcSurvey.getCmcSurveyName();
				Intellectual_Disabilities_Page_Title_English = cmcSurvey.getCmcSurveyPageTitleEnglish();
				Intellectual_Disabilities_Page_SubTitle_English = cmcSurvey.getCmcSurveyPageSubTitleEnglish();
				Intellectual_Disabilities_Menu_Title_Spanish = cmcSurvey.getCmcSurveyNameSpanish();
				Intellectual_Disabilities_Page_Title_Spanish = cmcSurvey.getCmcSurveyPageTitleSpanish();
				Intellectual_Disabilities_Page_SubTitle_Spanish = cmcSurvey.getCmcSurveyPageSubTitleSpanish();		
			}
			else if (cmcSurvey.getCmcSurveyName().equalsIgnoreCase(Autism_Disorder_Survey_Name))
			{
				Autism_Disorder_Menu_Title_English = cmcSurvey.getCmcSurveyName();
				Autism_Disorder_Page_Title_English = cmcSurvey.getCmcSurveyPageTitleEnglish();
				Autism_Disorder_Page_SubTitle_English = cmcSurvey.getCmcSurveyPageSubTitleEnglish();
				Autism_Disorder_Menu_Title_Spanish = cmcSurvey.getCmcSurveyNameSpanish();
				Autism_Disorder_Page_Title_Spanish = cmcSurvey.getCmcSurveyPageTitleSpanish();
				Autism_Disorder_Page_SubTitle_Spanish = cmcSurvey.getCmcSurveyPageSubTitleSpanish();		
			}
	    }
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

	public String getPhysicalDisabilitiesPageSubTitle() {
		return physicalDisabilitiesPageSubTitle;
	}

	public void setPhysicalDisabilitiesPageSubTitle(String physicalDisabilitiesPageSubTitle) {
		this.physicalDisabilitiesPageSubTitle = physicalDisabilitiesPageSubTitle;
	}

	public String getPhysicalDisabilitiesMenuTitle() {
		return physicalDisabilitiesMenuTitle;
	}

	public void setPhysicalDisabilitiesMenuTitle(String physicalDisabilitiesMenuTitle) {
		this.physicalDisabilitiesMenuTitle = physicalDisabilitiesMenuTitle;
	}

	public String getIntellectualDisabilitiesMenuTitle() {
		return intellectualDisabilitiesMenuTitle;
	}

	public void setIntellectualDisabilitiesMenuTitle(String intellectualDisabilitiesMenuTitle) {
		this.intellectualDisabilitiesMenuTitle = intellectualDisabilitiesMenuTitle;
	}

	public String getIntellectualDisabilitiesPageSubTitle() {
		return intellectualDisabilitiesPageSubTitle;
	}

	public void setIntellectualDisabilitiesPageSubTitle(String intellectualDisabilitiesPageSubTitle) {
		this.intellectualDisabilitiesPageSubTitle = intellectualDisabilitiesPageSubTitle;
	}

	public String getAutismDisorderMenuTitle() {
		return autismDisorderMenuTitle;
	}

	public void setAutismDisorderMenuTitle(String autismDisorderMenuTitle) {
		this.autismDisorderMenuTitle = autismDisorderMenuTitle;
	}

	public String getAutismDisorderPageSubTitle() {
		return autismDisorderPageSubTitle;
	}

	public void setAutismDisorderPageSubTitle(String autismDisorderPageSubTitle) {
		this.autismDisorderPageSubTitle = autismDisorderPageSubTitle;
	}

	public List<SelectItem> getYesNoEnglishList() {
		return yesNoEnglishList;
	}

	public void setYesNoEnglishList(List<SelectItem> yesNoEnglishList) {
		this.yesNoEnglishList = yesNoEnglishList;
	}

	public List<SelectItem> getYesNoSpanishList() {
		return yesNoSpanishList;
	}

	public void setYesNoSpanishList(List<SelectItem> yesNoSpanishList) {
		this.yesNoSpanishList = yesNoSpanishList;
	}

	public List<SelectItem> getSkillLevelEnglishList() {
		return skillLevelEnglishList;
	}

	public void setSkillLevelEnglishList(List<SelectItem> skillLevelEnglishList) {
		this.skillLevelEnglishList = skillLevelEnglishList;
	}

	public List<SelectItem> getSkillLevelSpanishList() {
		return skillLevelSpanishList;
	}

	public void setSkillLevelSpanishList(List<SelectItem> skillLevelSpanishList) {
		this.skillLevelSpanishList = skillLevelSpanishList;
	}

	public boolean isRenderDefaultAnswers() {
		return renderDefaultAnswers;
	}

	public void setRenderDefaultAnswers(boolean renderDefaultAnswers) {
		this.renderDefaultAnswers = renderDefaultAnswers;
	}
		
}
