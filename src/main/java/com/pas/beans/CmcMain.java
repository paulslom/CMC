package com.pas.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.selectoneradio.SelectOneRadio;

import com.pas.dao.CmcSurveyAnswersDAO;
import com.pas.dao.CmcSurveyQuestionsDAO;
import com.pas.dao.CmcSurveysDAO;
import com.pas.dynamodb.DynamoClients;
import com.pas.dynamodb.DynamoUtil;
import com.pas.pojo.DisabilityRow;

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
	
	public static String GREEN_STYLECLASS = "menuGreen";
	public static String RED_STYLECLASS = "menuRed";
	public static String YELLOW_STYLECLASS = "menuYellow";
		
	private static String Site_Title_English = "Mobility Match";
	private static String Site_Title_Spanish = "Mobility Match";
	
	private static String Site_SubTitle_English = "Connecting You with Best Choices of Community Mobility";
	private static String Site_SubTitle_Spanish = "Lo conectamos con las mejores opciones de movilidad comunitaria a través";

	public static String Physical_Disabilities_Survey_Name = "Physical Disabilities";
	public static String Intellectual_Disabilities_Survey_Name = "Intellectual Disabilities";
	public static String Autism_Disorder_Survey_Name = "Autism Spectrum Disorder";
	
	private int siteLanguage = ENGLISH;
		
	private boolean siteInEnglish = true;
	
	private boolean renderPhysicalResults = false;
	private boolean renderIntellectualResults = false;
	private boolean renderAutismResults = false;
	
	private static String Performs_Independently_Dropdown_Value = "1";
	private static String Potentially_Able_with_Training_Dropdown_Value = "2";
	private static String Unable_to_Meet_Demands_Dropdown_Value = "3";
	
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
	private String siteSubTitle;
	
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
		si = new SelectItem(Performs_Independently_Dropdown_Value,"Performs Independently");
		skillLevelEnglishList.add(si);
		si = new SelectItem(Potentially_Able_with_Training_Dropdown_Value,"Potentially Able with Training");
		skillLevelEnglishList.add(si);
		si = new SelectItem(Unable_to_Meet_Demands_Dropdown_Value,"Unable to Meet Demands");
		skillLevelEnglishList.add(si);
		
		si = new SelectItem("","Seleccionar");
		skillLevelSpanishList.add(si);
		si = new SelectItem(Performs_Independently_Dropdown_Value,"Lo realiza de manera independiente");
		skillLevelSpanishList.add(si);
		si = new SelectItem(Potentially_Able_with_Training_Dropdown_Value,"Lo puede realizar con entrenamiento");
		skillLevelSpanishList.add(si);
		si = new SelectItem(Unable_to_Meet_Demands_Dropdown_Value,"No puede cumplir con las demandas");
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
	
	public void highlightDropdown(AjaxBehaviorEvent event)
	{ 	
		//logger.info("highlighting dropdown");
	
		SelectOneMenu selectOneMenu = (SelectOneMenu)event.getSource();
		String selectedOption = (String)selectOneMenu.getValue();
		
		String clientID = selectOneMenu.getClientId();
		
		if (clientID.contains("physForm") && clientID.contains("physicalDisabilitiesSection1TableID"))
		{
			for (int i = 0; i < this.getPhysicalDisabilitiesSection1QuestionsHorizontalList().size(); i++)
			{
				DisabilityRow dsr = this.getPhysicalDisabilitiesSection1QuestionsHorizontalList().get(i);				
				setDsrStyle(dsr, selectedOption, clientID, i);				
			}
		}
		else if (clientID.contains("physForm") && clientID.contains("physicalDisabilitiesSection2TableID"))
		{
			for (int i = 0; i < this.getPhysicalDisabilitiesSection2QuestionsHorizontalList().size(); i++)
			{
				DisabilityRow dsr = this.getPhysicalDisabilitiesSection2QuestionsHorizontalList().get(i);
				setDsrStyle(dsr, selectedOption, clientID, i);			
			}
		}
		else if (clientID.contains("physForm") && clientID.contains("physicalDisabilitiesSection3TableID"))
		{
			for (int i = 0; i < this.getPhysicalDisabilitiesSection3QuestionsList().size(); i++)
			{
				CmcSurveyQuestion cmcsq = this.getPhysicalDisabilitiesSection3QuestionsList().get(i);
				
				if (clientID.contains(":" + i +":"))
				{
					if (selectedOption.equalsIgnoreCase(Performs_Independently_Dropdown_Value))  
					{ 
						cmcsq.setAnswerStyle(GREEN_STYLECLASS);	
					}
					else if (selectedOption.equalsIgnoreCase(Unable_to_Meet_Demands_Dropdown_Value))  
					{
						cmcsq.setAnswerStyle(RED_STYLECLASS);	
					}
					else if (selectedOption.equalsIgnoreCase(Potentially_Able_with_Training_Dropdown_Value))  
					{
						cmcsq.setAnswerStyle(YELLOW_STYLECLASS);	
					}
					else
					{
						cmcsq.setAnswerStyle("");	
					}
					break;
				}
			}
		}
		else if (clientID.contains("intelForm") && clientID.contains("intellectualDisabilitiesSection1TableID"))
		{
			for (int i = 0; i < this.getIntellectualDisabilitiesSection1QuestionsHorizontalList().size(); i++)
			{
				DisabilityRow dsr = this.getIntellectualDisabilitiesSection1QuestionsHorizontalList().get(i);	
				setDsrStyle(dsr, selectedOption, clientID, i);		
			}
		}
		else if (clientID.contains("intelForm") && clientID.contains("intellectualDisabilitiesSection2TableID"))
		{
			for (int i = 0; i < this.getIntellectualDisabilitiesSection2QuestionsList().size(); i++)
			{
				CmcSurveyQuestion cmcsq = this.getIntellectualDisabilitiesSection2QuestionsList().get(i);
				
				if (clientID.contains(":" + i +":"))
				{
					if (selectedOption.equalsIgnoreCase(Performs_Independently_Dropdown_Value))  
					{ 
						cmcsq.setAnswerStyle(GREEN_STYLECLASS);	
					}
					else if (selectedOption.equalsIgnoreCase(Unable_to_Meet_Demands_Dropdown_Value))  
					{
						cmcsq.setAnswerStyle(RED_STYLECLASS);	
					}
					else if (selectedOption.equalsIgnoreCase(Potentially_Able_with_Training_Dropdown_Value))  
					{
						cmcsq.setAnswerStyle(YELLOW_STYLECLASS);	
					}
					else
					{
						cmcsq.setAnswerStyle("");	
					}
					break;
				}
			}
		}
		else if (clientID.contains("autismForm") && clientID.contains("autismDisorderSection1TableID"))
		{
			for (int i = 0; i < this.getAutismDisorderSection1QuestionsHorizontalList().size(); i++)
			{
				DisabilityRow dsr = this.getAutismDisorderSection1QuestionsHorizontalList().get(i);
				setDsrStyle(dsr, selectedOption, clientID, i);		
			}
		}
		else if (clientID.contains("autismForm") && clientID.contains("autismDisorderSection2TableID"))
		{
			for (int i = 0; i < this.getAutismDisorderSection2QuestionsList().size(); i++)
			{
				CmcSurveyQuestion cmcsq = this.getAutismDisorderSection2QuestionsList().get(i);
				
				if (clientID.contains(":" + i +":"))
				{
					if (selectedOption.equalsIgnoreCase(Performs_Independently_Dropdown_Value))  
					{ 
						cmcsq.setAnswerStyle(GREEN_STYLECLASS);	
					}
					else if (selectedOption.equalsIgnoreCase(Unable_to_Meet_Demands_Dropdown_Value))  
					{
						cmcsq.setAnswerStyle(RED_STYLECLASS);	
					}
					else if (selectedOption.equalsIgnoreCase(Potentially_Able_with_Training_Dropdown_Value))  
					{
						cmcsq.setAnswerStyle(YELLOW_STYLECLASS);	
					}
					else
					{
						cmcsq.setAnswerStyle("");	
					}
					break;
				}
			}
		}
						
    }
	
	private void setDsrStyle(DisabilityRow dsr, String selectedOption, String clientID, int listIndex) 
	{
		boolean isCorrectRow = determineIfCorrectRow(clientID, listIndex);
		
		if (selectedOption.equalsIgnoreCase("Yes"))  
		{ 
			if (clientID.contains("PublicBusAnswerID") && isCorrectRow)
			{
				dsr.setPublicBusAnswerStyle(GREEN_STYLECLASS);	
			}
			else if (clientID.contains("CityBusAnswerID") && isCorrectRow)
			{
				dsr.setCoachBusAnswerStyle(GREEN_STYLECLASS);					
			}
			else if (clientID.contains("TrolleyAnswerID") && isCorrectRow)
			{
				dsr.setTrainSubwayAnswerStyle(GREEN_STYLECLASS);					
			}
			else if (clientID.contains("TaxiAnswerID") && isCorrectRow)
			{
				dsr.setTaxiAnswerStyle(GREEN_STYLECLASS);					
			}
			else if (clientID.contains("UberAnswerID") && isCorrectRow)
			{
				dsr.setEhailingAnswerStyle(GREEN_STYLECLASS);					
			}
			else if (clientID.contains("FerryAnswerID") && isCorrectRow)
			{
				dsr.setFerryAnswerStyle(GREEN_STYLECLASS);					
			}
			else if (clientID.contains("WalkAnswerID") && isCorrectRow)
			{
				dsr.setWalkAnswerStyle(GREEN_STYLECLASS);					
			}
			else if (clientID.contains("BikeAnswerID") && isCorrectRow)
			{
				dsr.setBikeAnswerStyle(GREEN_STYLECLASS);					
			}
			else if (clientID.contains("OtherAnswerID") && isCorrectRow)
			{
				dsr.setOtherAnswerStyle(GREEN_STYLECLASS);					
			}
			else if (clientID.contains("PowerWheelchair"))
			{
				dsr.setPowerwheelchairAnswerStyle(GREEN_STYLECLASS);					
			}
			else if (clientID.contains("ManualWheelchair"))
			{
				dsr.setManualwheelchairAnswerStyle(GREEN_STYLECLASS);					
			}
			else if (clientID.contains("Rollator"))
			{
				dsr.setRollatorAnswerStyle(GREEN_STYLECLASS);					
			}
			else if (clientID.contains("Prosthetics"))
			{
				dsr.setProstheticsAnswerStyle(GREEN_STYLECLASS);					
			}
			else if (clientID.contains("Walker"))
			{
				dsr.setWalkerAnswerStyle(GREEN_STYLECLASS);					
			}
			else if (clientID.contains("Crutchescane"))
			{
				dsr.setCrutchescaneAnswerStyle(GREEN_STYLECLASS);					
			}
			else if (clientID.contains("Scooter"))
			{
				dsr.setScooterAnswerStyle(GREEN_STYLECLASS);					
			}
			
		}
		else if (selectedOption.equalsIgnoreCase("No"))  
		{
			if (clientID.contains("PublicBusAnswerID") && isCorrectRow)
			{
				dsr.setPublicBusAnswerStyle(RED_STYLECLASS);	
			}
			else if (clientID.contains("CityBusAnswerID") && isCorrectRow)
			{
				dsr.setCoachBusAnswerStyle(RED_STYLECLASS);					
			}
			else if (clientID.contains("TrolleyAnswerID") && isCorrectRow)
			{
				dsr.setTrainSubwayAnswerStyle(RED_STYLECLASS);					
			}
			else if (clientID.contains("TaxiAnswerID") && isCorrectRow)
			{
				dsr.setTaxiAnswerStyle(RED_STYLECLASS);					
			}
			else if (clientID.contains("UberAnswerID") && isCorrectRow)
			{
				dsr.setEhailingAnswerStyle(RED_STYLECLASS);					
			}
			else if (clientID.contains("FerryAnswerID") && isCorrectRow)
			{
				dsr.setFerryAnswerStyle(RED_STYLECLASS);					
			}
			else if (clientID.contains("WalkAnswerID") && isCorrectRow)
			{
				dsr.setWalkAnswerStyle(RED_STYLECLASS);					
			}
			else if (clientID.contains("BikeAnswerID") && isCorrectRow)
			{
				dsr.setBikeAnswerStyle(RED_STYLECLASS);					
			}
			else if (clientID.contains("OtherAnswerID") && isCorrectRow)
			{
				dsr.setOtherAnswerStyle(RED_STYLECLASS);					
			}
			else if (clientID.contains("PowerWheelchair"))
			{
				dsr.setPowerwheelchairAnswerStyle(RED_STYLECLASS);					
			}
			else if (clientID.contains("ManualWheelchair"))
			{
				dsr.setManualwheelchairAnswerStyle(RED_STYLECLASS);					
			}
			else if (clientID.contains("Rollator"))
			{
				dsr.setRollatorAnswerStyle(RED_STYLECLASS);					
			}
			else if (clientID.contains("Prosthetics"))
			{
				dsr.setProstheticsAnswerStyle(RED_STYLECLASS);					
			}
			else if (clientID.contains("Walker"))
			{
				dsr.setWalkerAnswerStyle(RED_STYLECLASS);					
			}
			else if (clientID.contains("Crutchescane"))
			{
				dsr.setCrutchescaneAnswerStyle(RED_STYLECLASS);					
			}
			else if (clientID.contains("Scooter"))
			{
				dsr.setScooterAnswerStyle(RED_STYLECLASS);					
			}
        }		
		else //reset to nothing selected style
		{
			if (clientID.contains("PublicBusAnswerID") && isCorrectRow)
			{
				dsr.setPublicBusAnswerStyle("");	
			}
			else if (clientID.contains("CityBusAnswerID") && isCorrectRow)
			{
				dsr.setCoachBusAnswerStyle("");					
			}
			else if (clientID.contains("TrolleyAnswerID") && isCorrectRow)
			{
				dsr.setTrainSubwayAnswerStyle("");					
			}
			else if (clientID.contains("TaxiAnswerID") && isCorrectRow)
			{
				dsr.setTaxiAnswerStyle("");					
			}
			else if (clientID.contains("UberAnswerID") && isCorrectRow)
			{
				dsr.setEhailingAnswerStyle("");					
			}
			else if (clientID.contains("FerryAnswerID") && isCorrectRow)
			{
				dsr.setFerryAnswerStyle("");					
			}
			else if (clientID.contains("WalkAnswerID") && isCorrectRow)
			{
				dsr.setWalkAnswerStyle("");					
			}
			else if (clientID.contains("BikeAnswerID") && isCorrectRow)
			{
				dsr.setBikeAnswerStyle("");					
			}
			else if (clientID.contains("OtherAnswerID") && isCorrectRow)
			{
				dsr.setOtherAnswerStyle("");					
			}
			else if (clientID.contains("PowerWheelchair"))
			{
				dsr.setPowerwheelchairAnswerStyle("");					
			}
			else if (clientID.contains("ManualWheelchair"))
			{
				dsr.setManualwheelchairAnswerStyle("");					
			}
			else if (clientID.contains("Rollator"))
			{
				dsr.setRollatorAnswerStyle("");					
			}
			else if (clientID.contains("Prosthetics"))
			{
				dsr.setProstheticsAnswerStyle("");					
			}
			else if (clientID.contains("Walker"))
			{
				dsr.setWalkerAnswerStyle("");					
			}
			else if (clientID.contains("Crutchescane"))
			{
				dsr.setCrutchescaneAnswerStyle("");					
			}
			else if (clientID.contains("Scooter"))
			{
				dsr.setScooterAnswerStyle("");					
			}
		}
		
	}

	private boolean determineIfCorrectRow(String clientID, int listIndex) 
	{
		boolean correctRow = false;
		int totalRows = 3;
		
		for (int i = 0; i < totalRows; i++) 
		{
			String currentRow = ":" + i + ":";
				
			if (clientID.contains(currentRow))
			{
				if (listIndex == i)
				{
					correctRow = true;
				}
			}
		}
		
		return correctRow;
	}

	private void changeToSpanish()
	{
		this.setSiteLanguage(SPANISH);
		this.setSiteInEnglish(false);
		this.setSiteTitle(Site_Title_Spanish);
		this.setSiteSubTitle(Site_SubTitle_Spanish);
		
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
		this.setSiteSubTitle(Site_SubTitle_English);
		
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
		
	public String submitPhysicalDisabilityAnswers()
	{
		logger.info("entering submitPhysicalDisabilityAnswers");
		
		try
		{
			if (validateAnswersUpperSection(this.getPhysicalDisabilitiesSection1QuestionsHorizontalList())
			||  validateAnswersLowerSection(this.getPhysicalDisabilitiesSection3QuestionsList())) //will be true if errors
			{
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Please answer all questions",null);
		        FacesContext.getCurrentInstance().addMessage(null, msg);    
				return "";
			}
			
			for (int i = 0; i < this.getPhysicalDisabilitiesSection1QuestionsHorizontalList().size(); i++) 
		    {
				DisabilityRow dr = this.getPhysicalDisabilitiesSection1QuestionsHorizontalList().get(i);
				writeAnswersFromDisabilityRow(dr, "phys1");			
		    }
			
			for (int i = 0; i < this.getPhysicalDisabilitiesSection2QuestionsHorizontalList().size(); i++) 
		    {
				DisabilityRow dr = this.getPhysicalDisabilitiesSection2QuestionsHorizontalList().get(i);
				writeAnswersFromDisabilityRow(dr, "phys2");			
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
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"All Physical Disabilities answers successfully submitted",null);
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
	        
	        this.setRenderPhysicalResults(true);
	        this.setRenderIntellectualResults(false);
	        this.setRenderAutismResults(false);
		}
		catch (Exception e)
		{
			logger.error("Exception in submitPhysicalDisabilityAnswers: " + e.getMessage(), e);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Exception in submitPhysicalDisabilityAnswers: " + e.getMessage(),null);
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
	        return "";
		}
		
        //FacesContext.getCurrentInstance().getExternalContext().redirect("/results.xhtml");
		
		return "/results.xhtml";
	}	
	
	public String submitIntellectualDisabilityAnswers() throws Exception
	{
		logger.info("entering submitPhysicalDisabilityAnswers");
		
		try
		{
			if (validateAnswersUpperSection(this.getIntellectualDisabilitiesSection1QuestionsHorizontalList())
			||  validateAnswersLowerSection(this.getIntellectualDisabilitiesSection2QuestionsList())) //will be true if errors
			{
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Please answer all questions",null);
		        FacesContext.getCurrentInstance().addMessage(null, msg);    
				return "";
			}
			
			for (int i = 0; i < this.getIntellectualDisabilitiesSection1QuestionsHorizontalList().size(); i++) 
		    {
				DisabilityRow dr = this.getIntellectualDisabilitiesSection1QuestionsHorizontalList().get(i);
				writeAnswersFromDisabilityRow(dr, "intel");		
		    }
			
			for (int i = 0; i < this.getIntellectualDisabilitiesSection2QuestionsList().size(); i++) 
		    {
				CmcSurveyQuestion cmcSurveyQuestion = this.getIntellectualDisabilitiesSection2QuestionsList().get(i);
				CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();
				cmcSurveyAnswer.setSurveyQuestionID(cmcSurveyQuestion.getCmcSurveyQuestionID());
				cmcSurveyAnswer.setSurveyID(cmcSurveyQuestion.getCmcSurveyID());
				cmcSurveyAnswer.setSurveyAnswer(cmcSurveyQuestion.getAnswer());
				cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);
		    }
					
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"All Intellectual disabilities answers successfully submitted",null);
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
	        
	        this.setRenderPhysicalResults(false);
	        this.setRenderIntellectualResults(true);
	        this.setRenderAutismResults(false);
		}
		catch (Exception e)
		{
			logger.error("Exception in submitIntellectualDisabilityAnswers: " + e.getMessage(), e);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Exception in submitIntellectualDisabilityAnswers: " + e.getMessage(),null);
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
	        return "";
		}
		
        //FacesContext.getCurrentInstance().getExternalContext().redirect("/results.xhtml");
		
		return "/results.xhtml";
	}
	
	public String submitAutismSpectrumDisorderAnswers() throws Exception
	{
		logger.info("entering submitAutismSpectrumDisorderAnswers");
		
		try
		{
			if (validateAnswersUpperSection(this.getAutismDisorderSection1QuestionsHorizontalList())
			||  validateAnswersLowerSection(this.getAutismDisorderSection2QuestionsList())) //will be true if errors
			{
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Please answer all questions",null);
		        FacesContext.getCurrentInstance().addMessage(null, msg);    
				return "";
			}
			
			for (int i = 0; i < this.getAutismDisorderSection1QuestionsHorizontalList().size(); i++) 
		    {
				DisabilityRow dr = this.getAutismDisorderSection1QuestionsHorizontalList().get(i);
				writeAnswersFromDisabilityRow(dr, "autism");	
			}
			
			for (int i = 0; i < this.getAutismDisorderSection2QuestionsList().size(); i++) 
		    {
				CmcSurveyQuestion cmcSurveyQuestion = this.getAutismDisorderSection2QuestionsList().get(i);
				CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();
				cmcSurveyAnswer.setSurveyQuestionID(cmcSurveyQuestion.getCmcSurveyQuestionID());
				cmcSurveyAnswer.setSurveyID(cmcSurveyQuestion.getCmcSurveyID());
				cmcSurveyAnswer.setSurveyAnswer(cmcSurveyQuestion.getAnswer());
				cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);
		    }
					
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"All Autism Spectrum Disorder answers successfully submitted",null);
	        FacesContext.getCurrentInstance().addMessage(null, msg);   
	        
	        this.setRenderPhysicalResults(false);
	        this.setRenderIntellectualResults(false);
	        this.setRenderAutismResults(true);
		}
		catch (Exception e)
		{
			logger.error("Exception in submitAutismDisorderAnswers: " + e.getMessage(), e);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Exception in submitAutismDisorderAnswers: " + e.getMessage(),null);
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
	        return "";
		}
		
        //FacesContext.getCurrentInstance().getExternalContext().redirect("/results.xhtml");
		
		return "/results.xhtml";
	}
	
	private void writeAnswersFromDisabilityRow(DisabilityRow dr, String whichSurvey) throws Exception 
	{		
		if (whichSurvey.equalsIgnoreCase("phys2"))
		{
			if (dr.getPowerwheelchairAnswer() != null && dr.getPowerwheelchairAnswer().trim().length() > 0)
			{
				CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();		
				cmcSurveyAnswer.setSurveyID(dr.getCmcSurveyID());
				cmcSurveyAnswer.setSurveyAnswer(dr.getPowerwheelchairAnswer());
				cmcSurveyAnswer.setSurveyQuestionID(dr.getPowerwheelchairQuestionID());
				cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);		
			}
			
			if (dr.getManualwheelchairAnswer() != null && dr.getManualwheelchairAnswer().trim().length() > 0)
			{
				CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();		
				cmcSurveyAnswer.setSurveyID(dr.getCmcSurveyID());
				cmcSurveyAnswer.setSurveyAnswer(dr.getManualwheelchairAnswer());
				cmcSurveyAnswer.setSurveyQuestionID(dr.getManualwheelchairQuestionID());
				cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);		
			}
			
			if (dr.getRollatorAnswer() != null && dr.getRollatorAnswer().trim().length() > 0)
			{
				CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();		
				cmcSurveyAnswer.setSurveyID(dr.getCmcSurveyID());
				cmcSurveyAnswer.setSurveyAnswer(dr.getRollatorAnswer());
				cmcSurveyAnswer.setSurveyQuestionID(dr.getRollatorAnswerQuestionID());
				cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);		
			}
			
			if (dr.getProstheticsAnswer() != null && dr.getProstheticsAnswer().trim().length() > 0)
			{
				CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();		
				cmcSurveyAnswer.setSurveyID(dr.getCmcSurveyID());
				cmcSurveyAnswer.setSurveyAnswer(dr.getProstheticsAnswer());
				cmcSurveyAnswer.setSurveyQuestionID(dr.getProstheticsAnswerQuestionID());
				cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);		
			}
			
			if (dr.getWalkerAnswer() != null && dr.getWalkerAnswer().trim().length() > 0)
			{
				CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();		
				cmcSurveyAnswer.setSurveyID(dr.getCmcSurveyID());
				cmcSurveyAnswer.setSurveyAnswer(dr.getWalkerAnswer());
				cmcSurveyAnswer.setSurveyQuestionID(dr.getWalkerAnswerQuestionID());
				cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);		
			}
			
			if (dr.getCrutchescaneAnswer() != null && dr.getCrutchescaneAnswer().trim().length() > 0)
			{
				CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();		
				cmcSurveyAnswer.setSurveyID(dr.getCmcSurveyID());
				cmcSurveyAnswer.setSurveyAnswer(dr.getCrutchescaneAnswer());
				cmcSurveyAnswer.setSurveyQuestionID(dr.getCrutchescaneAnswerQuestionID());
				cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);		
			}
			
			if (dr.getScooterAnswer() != null && dr.getScooterAnswer().trim().length() > 0)
			{
				CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();		
				cmcSurveyAnswer.setSurveyID(dr.getCmcSurveyID());
				cmcSurveyAnswer.setSurveyAnswer(dr.getScooterAnswer());
				cmcSurveyAnswer.setSurveyQuestionID(dr.getScooterAnswerQuestionID());
				cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);		
			}
			
			if (dr.getOtherAnswer() != null && dr.getOtherAnswer().trim().length() > 0)
			{
				CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();		
				cmcSurveyAnswer.setSurveyID(dr.getCmcSurveyID());
				cmcSurveyAnswer.setSurveyAnswer(dr.getOtherAnswer());
				cmcSurveyAnswer.setSurveyQuestionID(dr.getOtherAnswerQuestionID());
				cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);		
			}
		}
		else //phys1, intel, autism
		{
			if (dr.getPublicBusAnswer() != null && dr.getPublicBusAnswer().trim().length() > 0)
			{
				CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();		
				cmcSurveyAnswer.setSurveyID(dr.getCmcSurveyID());
				cmcSurveyAnswer.setSurveyAnswer(dr.getPublicBusAnswer());
				cmcSurveyAnswer.setSurveyQuestionID(dr.getPublicBusAnswerQuestionID());
				cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);		
			}
			
			if (dr.getCoachBusAnswer() != null && dr.getCoachBusAnswer().trim().length() > 0)
			{
				CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();		
				cmcSurveyAnswer.setSurveyID(dr.getCmcSurveyID());
				cmcSurveyAnswer.setSurveyAnswer(dr.getCoachBusAnswer());
				cmcSurveyAnswer.setSurveyQuestionID(dr.getCoachBusAnswerQuestionID());
				cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);		
			}
			
			if (dr.getTrainSubwayAnswer() != null && dr.getTrainSubwayAnswer().trim().length() > 0)
			{
				CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();		
				cmcSurveyAnswer.setSurveyID(dr.getCmcSurveyID());
				cmcSurveyAnswer.setSurveyAnswer(dr.getTrainSubwayAnswer());
				cmcSurveyAnswer.setSurveyQuestionID(dr.getTrainSubwayAnswerQuestionID());
				cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);		
			}
			
			if (dr.getTaxiAnswer() != null && dr.getTaxiAnswer().trim().length() > 0)
			{
				CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();		
				cmcSurveyAnswer.setSurveyID(dr.getCmcSurveyID());
				cmcSurveyAnswer.setSurveyAnswer(dr.getTaxiAnswer());
				cmcSurveyAnswer.setSurveyQuestionID(dr.getTaxiAnswerQuestionID());
				cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);		
			}
			
			if (dr.getEhailingAnswer() != null && dr.getEhailingAnswer().trim().length() > 0)
			{
				CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();		
				cmcSurveyAnswer.setSurveyID(dr.getCmcSurveyID());
				cmcSurveyAnswer.setSurveyAnswer(dr.getEhailingAnswer());
				cmcSurveyAnswer.setSurveyQuestionID(dr.getEhailingAnswerQuestionID());
				cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);		
			}
			
			if (dr.getFerryAnswer() != null && dr.getFerryAnswer().trim().length() > 0)
			{
				CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();		
				cmcSurveyAnswer.setSurveyID(dr.getCmcSurveyID());
				cmcSurveyAnswer.setSurveyAnswer(dr.getFerryAnswer());
				cmcSurveyAnswer.setSurveyQuestionID(dr.getFerryAnswerQuestionID());
				cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);		
			}
			
			if (dr.getWalkAnswer() != null && dr.getWalkAnswer().trim().length() > 0)
			{
				CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();		
				cmcSurveyAnswer.setSurveyID(dr.getCmcSurveyID());
				cmcSurveyAnswer.setSurveyAnswer(dr.getWalkAnswer());
				cmcSurveyAnswer.setSurveyQuestionID(dr.getWalkAnswerQuestionID());
				cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);		
			}
			
			if (dr.getBikeAnswer() != null && dr.getBikeAnswer().trim().length() > 0)
			{
				CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();		
				cmcSurveyAnswer.setSurveyID(dr.getCmcSurveyID());
				cmcSurveyAnswer.setSurveyAnswer(dr.getBikeAnswer());
				cmcSurveyAnswer.setSurveyQuestionID(dr.getBikeAnswerQuestionID());
				cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);		
			}
			
			if (dr.getOtherAnswer() != null && dr.getOtherAnswer().trim().length() > 0)
			{
				CmcSurveyAnswer cmcSurveyAnswer = new CmcSurveyAnswer();		
				cmcSurveyAnswer.setSurveyID(dr.getCmcSurveyID());
				cmcSurveyAnswer.setSurveyAnswer(dr.getOtherAnswer());
				cmcSurveyAnswer.setSurveyQuestionID(dr.getOtherAnswerQuestionID());
				cmcSurveyAnswersDAO.addSurveyAnswer(cmcSurveyAnswer);		
			}
			
		}
		
		
	}

	private boolean validateAnswersUpperSection(List<DisabilityRow> tempList)
	{
		boolean errorFound = false;
		
		for (int i = 0; i < tempList.size(); i++) 
	    {
			DisabilityRow dr = tempList.get(i);
			
			if (dr.getCmcSurveyCategoryEnglish().equalsIgnoreCase("Available"))
			{
				if (dr.getPublicBusAnswer() == null || dr.getPublicBusAnswer().trim().length() == 0)
				{
					errorFound = true;
				}	
				if (dr.getCoachBusAnswer() == null || dr.getCoachBusAnswer().trim().length() == 0)
				{
					errorFound = true;
				}	
				if (dr.getTrainSubwayAnswer() == null || dr.getTrainSubwayAnswer().trim().length() == 0)
				{
					errorFound = true;
				}	
				if (dr.getTaxiAnswer() == null || dr.getTaxiAnswer().trim().length() == 0)
				{
					errorFound = true;
				}	
				if (dr.getEhailingAnswer() == null || dr.getEhailingAnswer().trim().length() == 0)
				{
					errorFound = true;
				}	
				if (dr.getFerryAnswer() == null || dr.getFerryAnswer().trim().length() == 0)
				{
					errorFound = true;
				}	
				if (dr.getWalkAnswer() == null || dr.getWalkAnswer().trim().length() == 0)
				{
					errorFound = true;
				}	
				if (dr.getBikeAnswer() == null || dr.getBikeAnswer().trim().length() == 0)
				{
					errorFound = true;
				}	
				if (dr.getOtherAnswer() == null || dr.getOtherAnswer().trim().length() == 0)
				{
					errorFound = true;
				}	
			}
					
	    }
		
		return errorFound;				
	}
	
	private boolean validateAnswersLowerSection(List<CmcSurveyQuestion> tempList)
	{
		boolean errorFound = false;
		
		for (int i = 0; i < tempList.size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = tempList.get(i);
			
			if (cmcSurveyQuestion.getAnswer() == null || cmcSurveyQuestion.getAnswer().trim().length() == 0)
			{
				errorFound = true;
			}			
	    }
		
		return errorFound;				
	}
	              
	public String clearAllPhysicalDisabilityAnswers()
	{
		logger.info("entering clearAllPhysicalDisabilityAnswers");
		
		for (int i = 0; i < this.getPhysicalDisabilitiesSection1QuestionsHorizontalList().size(); i++) 
	    {
			DisabilityRow dr = this.getPhysicalDisabilitiesSection1QuestionsHorizontalList().get(i);
			clearDisabilityRow(dr);			
	    }
		
		for (int i = 0; i < this.getPhysicalDisabilitiesSection2QuestionsHorizontalList().size(); i++) 
	    {
			DisabilityRow dr = this.getPhysicalDisabilitiesSection2QuestionsHorizontalList().get(i);
			clearDisabilityRow(dr);		
	    }
		
		for (int i = 0; i < this.getPhysicalDisabilitiesSection3QuestionsList().size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = this.getPhysicalDisabilitiesSection3QuestionsList().get(i);
			cmcSurveyQuestion.setAnswer("Select");
			cmcSurveyQuestion.setAnswerStyle("");
	    }
		
		return "";
	}

	public String clearAllIntellectualDisabilityAnswers()
	{
		logger.info("entering clearAllIntellectualDisabilityAnswers");
		
		for (int i = 0; i < this.getIntellectualDisabilitiesSection1QuestionsHorizontalList().size(); i++) 
	    {
			DisabilityRow dr = this.getIntellectualDisabilitiesSection1QuestionsHorizontalList().get(i);
			clearDisabilityRow(dr);			
	    }
				
		for (int i = 0; i < this.getIntellectualDisabilitiesSection2QuestionsList().size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = this.getIntellectualDisabilitiesSection2QuestionsList().get(i);
			cmcSurveyQuestion.setAnswer("Select");
			cmcSurveyQuestion.setAnswerStyle("");
	    }
		
		return "";
	}
	
	public String clearAllAutismSpectrumDisorderAnswers()
	{
		logger.info("entering clearAllAutismSpectrumDisorderAnswers");
		
		for (int i = 0; i < this.getAutismDisorderSection1QuestionsHorizontalList().size(); i++) 
	    {
			DisabilityRow dr = this.getAutismDisorderSection1QuestionsHorizontalList().get(i);
			clearDisabilityRow(dr);			
	    }
		
		for (int i = 0; i < this.getAutismDisorderSection2QuestionsList().size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = this.getAutismDisorderSection2QuestionsList().get(i);
			cmcSurveyQuestion.setAnswer("Select");
			cmcSurveyQuestion.setAnswerStyle("");
	    }		
		
		return "";
	}
	
	public String defaultAllPhysicalDisabilityAnswers()
	{
		logger.info("entering defaultAllPhysicalDisabilityAnswers");
		
		for (int i = 0; i < this.getPhysicalDisabilitiesSection1QuestionsHorizontalList().size(); i++) 
	    {
			DisabilityRow dr = this.getPhysicalDisabilitiesSection1QuestionsHorizontalList().get(i);
			defaultDisabilityRow(dr);			
	    }
		
		for (int i = 0; i < this.getPhysicalDisabilitiesSection2QuestionsHorizontalList().size(); i++) 
	    {
			DisabilityRow dr = this.getPhysicalDisabilitiesSection2QuestionsHorizontalList().get(i);
			defaultDisabilityRow(dr);		
	    }
		
		for (int i = 0; i < this.getPhysicalDisabilitiesSection3QuestionsList().size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = this.getPhysicalDisabilitiesSection3QuestionsList().get(i);
			cmcSurveyQuestion.setAnswer(Performs_Independently_Dropdown_Value);
			cmcSurveyQuestion.setAnswerStyle(GREEN_STYLECLASS);
	    }
		
		return "";
		
	}
	
	public String defaultAllIntellectualDisabilityAnswers()
	{
		logger.info("entering defaultAllIntellectualDisabilityAnswers");
		
		for (int i = 0; i < this.getIntellectualDisabilitiesSection1QuestionsHorizontalList().size(); i++) 
	    {
			DisabilityRow dr = this.getIntellectualDisabilitiesSection1QuestionsHorizontalList().get(i);
			defaultDisabilityRow(dr);			
	    }
		
		for (int i = 0; i < this.getIntellectualDisabilitiesSection2QuestionsList().size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = this.getIntellectualDisabilitiesSection2QuestionsList().get(i);
			cmcSurveyQuestion.setAnswer(Performs_Independently_Dropdown_Value);
			cmcSurveyQuestion.setAnswerStyle(GREEN_STYLECLASS);
	    }
		
		return "";		
	}
	
	public String defaultAllAutismSpectrumDisorderAnswers()
	{
		logger.info("entering defaultAllAllAutismSpectrumDisorderAnswers");
		
		for (int i = 0; i < this.getAutismDisorderSection1QuestionsHorizontalList().size(); i++) 
	    {
			DisabilityRow dr = this.getAutismDisorderSection1QuestionsHorizontalList().get(i);
			defaultDisabilityRow(dr);			
	    }
		
		for (int i = 0; i < this.getAutismDisorderSection2QuestionsList().size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = this.getAutismDisorderSection2QuestionsList().get(i);
			cmcSurveyQuestion.setAnswer(Performs_Independently_Dropdown_Value);
			cmcSurveyQuestion.setAnswerStyle(GREEN_STYLECLASS);
	    }
		
		return "";		
	}
	
	private void clearDisabilityRow(DisabilityRow dr) 
	{
		dr.setPublicBusAnswer(""); 
		dr.setPublicBusAnswerStyle(""); 
		dr.setCoachBusAnswer(""); 
		dr.setCoachBusAnswerStyle(""); 
		dr.setTrainSubwayAnswer(""); 
		dr.setTrainSubwayAnswerStyle(""); 
		dr.setTaxiAnswer(""); 
		dr.setTaxiAnswerStyle(""); 
		dr.setEhailingAnswer(""); 
		dr.setEhailingAnswerStyle(""); 
		dr.setFerryAnswer(""); 
		dr.setFerryAnswerStyle(""); 
		dr.setWalkAnswer(""); 
		dr.setWalkAnswerStyle(""); 
		dr.setBikeAnswer(""); 
		dr.setBikeAnswerStyle(""); 
		dr.setOtherAnswer(""); 
		dr.setOtherAnswerStyle(""); 
		dr.setPowerwheelchairAnswer(""); 
		dr.setManualwheelchairAnswer(""); 
		dr.setRollatorAnswer(""); 
		dr.setProstheticsAnswer(""); 
		dr.setWalkerAnswer(""); 
		dr.setCrutchescaneAnswer(""); 
		dr.setScooterAnswer(""); 
		dr.setPowerwheelchairAnswerStyle(""); 
		dr.setManualwheelchairAnswerStyle(""); 
		dr.setRollatorAnswerStyle(""); 
		dr.setProstheticsAnswerStyle(""); 
		dr.setWalkerAnswerStyle(""); 
		dr.setCrutchescaneAnswerStyle(""); 
		dr.setScooterAnswerStyle(""); 		
	}
	
	private void defaultDisabilityRow(DisabilityRow dr) 
	{
		dr.setPublicBusAnswer("Yes"); 
		dr.setPublicBusAnswerStyle(GREEN_STYLECLASS); 
		dr.setCoachBusAnswer("Yes"); 
		dr.setCoachBusAnswerStyle(GREEN_STYLECLASS); 
		dr.setTrainSubwayAnswer("Yes"); 
		dr.setTrainSubwayAnswerStyle(GREEN_STYLECLASS); 
		dr.setTaxiAnswer("Yes"); 
		dr.setTaxiAnswerStyle(GREEN_STYLECLASS); 
		dr.setEhailingAnswer("Yes"); 
		dr.setEhailingAnswerStyle(GREEN_STYLECLASS); 
		dr.setFerryAnswer("Yes"); 
		dr.setFerryAnswerStyle(GREEN_STYLECLASS); 
		dr.setWalkAnswer("Yes"); 
		dr.setWalkAnswerStyle(GREEN_STYLECLASS); 
		dr.setBikeAnswer("Yes"); 
		dr.setBikeAnswerStyle(GREEN_STYLECLASS); 
		dr.setOtherAnswer("Yes"); 
		dr.setOtherAnswerStyle(GREEN_STYLECLASS); 
		dr.setPowerwheelchairAnswer("Yes"); 
		dr.setManualwheelchairAnswer(""); 
		dr.setRollatorAnswer("Yes"); 
		dr.setProstheticsAnswer(""); 
		dr.setWalkerAnswer("Yes"); 
		dr.setCrutchescaneAnswer(""); 
		dr.setScooterAnswer("Yes"); 
		dr.setPowerwheelchairAnswerStyle(GREEN_STYLECLASS); 
		dr.setManualwheelchairAnswerStyle(GREEN_STYLECLASS); 
		dr.setRollatorAnswerStyle(GREEN_STYLECLASS); 
		dr.setProstheticsAnswerStyle(GREEN_STYLECLASS); 
		dr.setWalkerAnswerStyle(GREEN_STYLECLASS); 
		dr.setCrutchescaneAnswerStyle(GREEN_STYLECLASS); 
		dr.setScooterAnswerStyle(GREEN_STYLECLASS); 		
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
	
	public List<DisabilityRow> getPhysicalDisabilitiesSection1QuestionsHorizontalList() 
	{
		return cmcSurveyQuestionsDAO.getPhysicalDisabilitiesSection1QuestionsHorizontalList();
	}
	
	public List<DisabilityRow> getIntellectualDisabilitiesSection1QuestionsHorizontalList() 
	{
		return cmcSurveyQuestionsDAO.getIntellectualDisabilitiesSection1QuestionsHorizontalList();
	}	
	
	public List<DisabilityRow> getAutismDisorderSection1QuestionsHorizontalList() 
	{
		return cmcSurveyQuestionsDAO.getAutismDisorderSection1QuestionsHorizontalList();
	}
	
	public List<CmcSurveyQuestion> getPhysicalDisabilitiesSection2QuestionsList() 
	{
		return cmcSurveyQuestionsDAO.getPhysicalDisabilitiesSection2QuestionsList();
	}
	
	public List<DisabilityRow> getPhysicalDisabilitiesSection2QuestionsHorizontalList() 
	{
		return cmcSurveyQuestionsDAO.getPhysicalDisabilitiesSection2QuestionsHorizontalList();
	}
	
	public List<CmcSurveyQuestion> getIntellectualDisabilitiesSection1QuestionsList() 
	{
		return cmcSurveyQuestionsDAO.getIntellectualDisabilitiesSection1QuestionsList();
	}
	
	public List<CmcSurveyQuestion> getIntellectualDisabilitiesSection2QuestionsList() 
	{
		return cmcSurveyQuestionsDAO.getIntellectualDisabilitiesSection2QuestionsList();
	}
	
	public List<CmcSurveyQuestion> getAutismDisorderSection1QuestionsList() 
	{
		return cmcSurveyQuestionsDAO.getAutismSpectrumSection1QuestionsList();
	}
	
	public List<CmcSurveyQuestion> getAutismDisorderSection2QuestionsList() 
	{
		return cmcSurveyQuestionsDAO.getAutismSpectrumSection2QuestionsList();
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

	public String getSiteSubTitle() {
		return siteSubTitle;
	}

	public void setSiteSubTitle(String siteSubTitle) {
		this.siteSubTitle = siteSubTitle;
	}

	public boolean isRenderPhysicalResults() {
		return renderPhysicalResults;
	}

	public void setRenderPhysicalResults(boolean renderPhysicalResults) {
		this.renderPhysicalResults = renderPhysicalResults;
	}

	public boolean isRenderAutismResults() {
		return renderAutismResults;
	}

	public void setRenderAutismResults(boolean renderAutismResults) {
		this.renderAutismResults = renderAutismResults;
	}

	public boolean isRenderIntellectualResults() {
		return renderIntellectualResults;
	}

	public void setRenderIntellectualResults(boolean renderIntellectualResults) {
		this.renderIntellectualResults = renderIntellectualResults;
	}
		
}
