package com.pas.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.selectoneradio.SelectOneRadio;
import org.primefaces.model.ResponsiveOption;
import org.primefaces.util.ComponentUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.pas.dao.CmcDiagnosesDAO;
import com.pas.dao.CmcSurveyAnswersDAO;
import com.pas.dao.CmcSurveyQuestionsDAO;
import com.pas.dao.CmcSurveysDAO;
import com.pas.dao.CmcUsersDAO;
import com.pas.dynamodb.DynamoClients;
import com.pas.dynamodb.DynamoUtil;
import com.pas.pojo.DisabilityRow;
import com.pas.pojo.Photo;
import com.pas.pojo.QuestionSkip;
import com.pas.pojo.ResultsRow;
import com.pas.services.PhotoService;
import com.pas.util.Utils;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIColumn;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.ValueHolder;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.context.ExternalContext;

@Named("pc_CmcMain")
@ApplicationScoped
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
	
	private static String Site_SubTitle_English = "Connecting You with Choices of Community Mobility";
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
	
	private List<SelectItem> agesList = new ArrayList<>();
	private List<SelectItem> physicalDiagnosesList = new ArrayList<>();
	private List<SelectItem> intellectualDiagnosesList = new ArrayList<>();
	private List<SelectItem> autismDiagnosesList = new ArrayList<>();
	private List<SelectItem> livingEnvironmentList = new ArrayList<>();
	private List<SelectItem> usageReasonList = new ArrayList<>();
	
	private List<CmcUser> siteVisitsList = new ArrayList<>();
	
	private String siteTitle;
	private String siteSubTitle;
	
	private boolean renderDefaultAnswers = true;
	
	private List<ResultsRow> resultsList = new ArrayList<>();
	
	private String physicalDisabilitiesMenuTitle;
	private String physicalDisabilitiesPageTitle;
	private String physicalDisabilitiesPageSubTitle;
	
	private String intellectualDisabilitiesMenuTitle;
	private String intellectualDisabilitiesPageTitle;
	private String intellectualDisabilitiesPageSubTitle;
	
	private String autismDisorderMenuTitle;
	private String autismDisorderPageTitle;
	private String autismDisorderPageSubTitle;
	
	private String resultsPageTitle = "Pauls Results";
	
	private static String HTML_CRLF = "<br><br>";
	private static String PDF_CRLF = "\r\n\r\n";
	
	private CmcUsersDAO cmcUsersDAO;
	private CmcSurveyQuestionsDAO cmcSurveyQuestionsDAO;
	private CmcSurveysDAO cmcSurveysDAO;
	private CmcSurveyAnswersDAO cmcSurveyAnswersDAO;
	private CmcDiagnosesDAO cmcDiagnosesDAO;
	
	private CmcUser currentUser = new CmcUser();
	
	private List<Photo> photosList;
    private List<ResponsiveOption> responsiveOptions1;
    
    private String surveyClientAge;
	private String surveyClientDiagnosis;
	private String surveyClientLivingEnvironment;
	private String surveyClientPrimaryGoalOfUse;
	
    @Inject
    private PhotoService service;
    
	public void onStart(@Observes @Initialized(ApplicationScoped.class) Object pointless) 
	{
		logger.info("Entering CmcMain onStart method.  Should only be here ONE time");
		logger.info("CmcMain id is: " + this.getId());
		logger.info("Setting default to English");
		
		yesNoEnglishList.clear();
		SelectItem si = new SelectItem("","Select");
		yesNoEnglishList.add(si);
		si = new SelectItem("Yes","Yes");
		yesNoEnglishList.add(si);
		si = new SelectItem("No","No");
		yesNoEnglishList.add(si);
		
		yesNoSpanishList.clear();
		si = new SelectItem("","Seleccionar");
		yesNoSpanishList.add(si);
		si = new SelectItem("Yes","Si");
		yesNoSpanishList.add(si);
		si = new SelectItem("No","No");
		yesNoSpanishList.add(si);
		
		skillLevelEnglishList.clear();
		si = new SelectItem("","Select");
		skillLevelEnglishList.add(si);
		si = new SelectItem(Performs_Independently_Dropdown_Value,"Performs Independently");
		skillLevelEnglishList.add(si);
		si = new SelectItem(Potentially_Able_with_Training_Dropdown_Value,"Potentially Able with Training");
		skillLevelEnglishList.add(si);
		si = new SelectItem(Unable_to_Meet_Demands_Dropdown_Value,"Unable to Meet Demands");
		skillLevelEnglishList.add(si);
		
		skillLevelSpanishList.clear();
		si = new SelectItem("","Seleccionar");
		skillLevelSpanishList.add(si);
		si = new SelectItem(Performs_Independently_Dropdown_Value,"Lo realiza de manera independiente");
		skillLevelSpanishList.add(si);
		si = new SelectItem(Potentially_Able_with_Training_Dropdown_Value,"Lo puede realizar con entrenamiento");
		skillLevelSpanishList.add(si);
		si = new SelectItem(Unable_to_Meet_Demands_Dropdown_Value,"No puede cumplir con las demandas");
		skillLevelSpanishList.add(si);
		
		usageReasonList.clear();
		si = new SelectItem("","Select");
		usageReasonList.add(si);
		si = new SelectItem("DevelopTransportationPlan","Develop Transportation Plan");
		usageReasonList.add(si);
		si = new SelectItem("AssistInMM","Assist In Mobility Management");
		usageReasonList.add(si);		
		si = new SelectItem("EvalOfCommMobilitySkills","Evaluation Of Community Mobility Skills");
		usageReasonList.add(si);
		si = new SelectItem("TryingOut","Trying Out");
		usageReasonList.add(si);	
		si = new SelectItem("ExploringIndependence","Exploring Potential Independence in Mobility");
		usageReasonList.add(si);
						
		livingEnvironmentList.clear();
		si = new SelectItem("","Select");
		livingEnvironmentList.add(si);
		si = new SelectItem("Independent","Independent");
		livingEnvironmentList.add(si);
		si = new SelectItem("WithParents","With Parents/Family");
		livingEnvironmentList.add(si);
		si = new SelectItem("GroupHome","Group Home");
		livingEnvironmentList.add(si);
		si = new SelectItem("AssistiveLiving","Assistive Living");
		livingEnvironmentList.add(si);
		si = new SelectItem("Institution","Institution");
		livingEnvironmentList.add(si);
			
		agesList.clear();
		si = new SelectItem("","Select");
		agesList.add(si);
		
		for (int i = 12; i <= 80; i++) 
		{
			if (i < 80)
			{
				si = new SelectItem(String.valueOf(i),String.valueOf(i));
			}
			else
			{
				si = new SelectItem("80+","80+");
			}
			agesList.add(si);
		}
						
		try 
		{
			//this gets populated at app startup, no need to do it again when someone logs in.
			if (cmcSurveysDAO == null || cmcSurveysDAO.getFullSurveysMap().isEmpty())
			{
				DynamoClients dynamoClients = DynamoUtil.getDynamoClients();
				
				loadUsers(dynamoClients);
				loadCmcSurveys(dynamoClients);
				loadCmcSurveyQuestions(dynamoClients);
				loadCmcSurveyAnswers(dynamoClients);
				loadCmcDiagnoses(dynamoClients);
			}
			
			changeToEnglish();
		} 
		catch (Exception e) 
		{
			logger.error(e.getMessage(), e);
		}		
	}

	@PostConstruct
    public void init() 
	{
        this.setPhotosList(service.getPhotos());

        responsiveOptions1 = new ArrayList<>();
        responsiveOptions1.add(new ResponsiveOption("1024px", 5));
        responsiveOptions1.add(new ResponsiveOption("768px", 3));
        responsiveOptions1.add(new ResponsiveOption("560px", 1));
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
	
	public String exportResultColumn(UIColumn column) 
	{
	    String value = "";
	    for(UIComponent child: column.getChildren()) 
	    {
	        if(child instanceof ValueHolder) 
	        {
	            value = ComponentUtils.getValueToRender(FacesContext.getCurrentInstance(), child);
	        }
	    }
	    
	    value = value.replaceAll(HTML_CRLF, PDF_CRLF);
	    
	    return value;
	}
	
	public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException 
	{
        Document pdf = (Document) document;
        pdf.setPageSize(PageSize.A4.rotate());
        pdf.open();
        
        /*
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

        String separator = File.separator;
        String logo = externalContext.getRealPath("") + separator + "images" + separator + "planForTheRoadAhead.png";

        pdf.add(Image.getInstance(logo));
        
        */
    }
	
	public String getSignedOnUserName() 
	{
		String username = "";
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) 
		{
		   username = ((UserDetails)principal).getUsername();
		} 
		else 
		{
		   username = principal.toString();
		}
		
		if (username != null)
		{
			username = username.toLowerCase();
		}
		return username;
	}
	
	public void siteVisits(ActionEvent event) 
	{
		try 
        {		    
		    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();		    
		    
		    refreshSiteVisitsList();
		    
            String targetURL = Utils.getContextRoot() + "/reportSiteVisits.xhtml";
		    ec.redirect(targetURL);
            logger.info("successfully redirected to: " + targetURL);
        } 
        catch (Exception e) 
        {
            logger.error("exception: " + e.getMessage(), e);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);		 	
        }
	}  	
		
	public void surveyAnswerTotals(ActionEvent event) 
	{
		try 
        {		    
		    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();		    
		    String accountid = ec.getRequestParameterMap().get("accountid");		    
		    logger.info("account ID " + accountid + " selected to show trx from menu");
		    
		    refreshSurveyAnswerTotals();
		    
            String targetURL = Utils.getContextRoot() + "/reportSurveyAnswers.xhtml";
		    ec.redirect(targetURL);
            logger.info("successfully redirected to: " + targetURL);
        } 
        catch (Exception e) 
        {
            logger.error("exception: " + e.getMessage(), e);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);		 	
        }
	}  	
	
	public String register()
	{
		logger.info("entering register method");
		
		try
		{
			this.getCurrentUser().setUserRole("USER"); //default to normal user.  Admin would have to make them admin within the database using a db tool.
			
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();		    
		    String pw = ec.getRequestParameterMap().get("password");		    
			this.getCurrentUser().setPassword(pw); 
			
			cmcUsersDAO.addUser(this.getCurrentUser());
			
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Registered!", "Successfully Registered!");
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
		 	
			String targetURL = Utils.getContextRoot() + "/login.xhtml";
		    ec.redirect(targetURL);
            logger.info("successfully redirected to: " + targetURL);
		}
		catch (Exception e)
		{
        	logger.error("register errored: " + e.getMessage(), e);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
		 	return "";
        }
				
		return "";	
	}
	
	public String updateUser()
	{
		try
		{
			cmcUsersDAO.updateUser(this.getCurrentUser());
			
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Updated!", "Successfully Updated!");
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
		}
		catch (Exception e)
		{
        	logger.error("updateUser errored: " + e.getMessage(), e);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
		 	return "";
        }
				
		return "";	
	}
	
	public String changePassword()
	{
		logger.info("entering changePassword method");
		
		try
		{			
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			String userid = ec.getRequestParameterMap().get("username");	
		    String pw = ec.getRequestParameterMap().get("password");
		    
		    CmcUser cmcUser = cmcUsersDAO.getCmcUser(userid);
		    
		    if (cmcUser == null)
		    {
		    	FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "User not found.  Try again or Register if new","User not found.  Try again or Register if new");
			 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
			 	return "";
		    }
		    else //user is found and valid
		    {
		    	String encodedPW = Utils.getEncryptedPassword(pw);				
				cmcUser.setPassword(encodedPW);		
		    	cmcUsersDAO.updateUser(cmcUser);
		    	this.setCurrentUser(cmcUser);
		    	
		    	FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Changed Password!", "Successfully Changed Password!");
			 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
		    }
			
		}
		catch (Exception e)
		{
        	logger.error("change password errored: " + e.getMessage(), e);
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
		 	FacesContext.getCurrentInstance().addMessage(null, facesMessage);	
		 	return "";
        }
				
		return "";	
	}
	
	public String returnToLogin()
	{
		return "/login.xhtml";
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
	
	//This method only for Paul's unit testing of the results page
	public String submitTestAnswers()
	{
		logger.info("entering submitTestAnswers");
		
		this.setRenderPhysicalResults(true);
		
		ResultsRow resultsRow = new ResultsRow();
		resultsRow.setTransportationOption("Public (City) Bus");
		resultsRow.setTransportationViable(true);
		resultsRow.getSkillsAcquiredList().add("1. Ambulate independently, including steps/stairs or locate/operate elevators");
		resultsRow.getSkillsAcquiredList().add("2. Propels self up and down ramps without assistance");
		resultsRow.getSkillsAcquiredList().add("4. Transfers in and out of a vehicle efficiently");
		resultsRow.getSkillsNeedingDevelopmentList().add("6. Crosses a street without pedestrian controls (with/without traffic signals)");
		resultsRow.getSkillsNeedingDevelopmentList().add("18. Manage money/money card/fare card to pay for and register travel");
		resultsRow.getSkillsUnableToPerformList().add("9. Secures mobility device to vehicle or provide directions to others");
		
		resultsList.add(resultsRow);
		
		resultsRow = new ResultsRow();
		resultsRow.setTransportationOption("Coach (Intercity Bus)");
		resultsRow.setTransportationViable(false);
		resultsRow.getSkillsAcquiredList().add("5. Coach bus, including steps/stairs or locate/operate elevators");
		resultsRow.getSkillsAcquiredList().add("2. Propels self up into the coach bus");
		resultsRow.getSkillsAcquiredList().add("4. Transfers in and out of the coach bus");
		resultsRow.getSkillsNeedingDevelopmentList().add("6. Crosses a street into the coach bus");
		resultsRow.getSkillsNeedingDevelopmentList().add("18. Manage moneyto use for the coach bus");
		resultsRow.getSkillsUnableToPerformList().add("9. Secures mobility device for the coach bus");
		
		resultsList.add(resultsRow);
		
		return "";
	}	
		
	public String submitPhysicalDisabilityAnswers()
	{
		logger.info("entering submitPhysicalDisabilityAnswers");
		
		try
		{
			if (upperSectionAnswersInvalid(this.getPhysicalDisabilitiesSection1QuestionsHorizontalList(), Physical_Disabilities_Survey_Name)
			||  lowerSectionAnswersInvalid(this.getPhysicalDisabilitiesSection3QuestionsList())
			||  clientInfoInvalid()) 
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
			
			/*
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"All Physical Disabilities answers successfully submitted",null);
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
	        */
			
	        this.setRenderPhysicalResults(true);
	        this.setRenderIntellectualResults(false);
	        this.setRenderAutismResults(false);
	        
	        populateResultsPage("physical", this.getPhysicalDisabilitiesSection1QuestionsHorizontalList(), this.getPhysicalDisabilitiesSection3QuestionsList(), Physical_Disabilities_Survey_Name);
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

	private boolean clientInfoInvalid() 
	{
		boolean clientInfoInValid = false;
		
		if (this.getSurveyClientAge() == null || this.getSurveyClientAge().trim().length() == 0)
		{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Please answer client age",null);
	        FacesContext.getCurrentInstance().addMessage(null, msg);    
	        clientInfoInValid = true;
		}
		
		if (this.getSurveyClientDiagnosis() == null || this.getSurveyClientDiagnosis().trim().length() == 0)
		{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Please answer client diagnosis",null);
	        FacesContext.getCurrentInstance().addMessage(null, msg);    
	        clientInfoInValid = true;
		}	
		
		if (this.getSurveyClientLivingEnvironment() == null || this.getSurveyClientLivingEnvironment().trim().length() == 0)
		{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Please answer client living arrangement",null);
	        FacesContext.getCurrentInstance().addMessage(null, msg);    
	        clientInfoInValid = true;
		}	
		
		if (this.getSurveyClientPrimaryGoalOfUse() == null || this.getSurveyClientPrimaryGoalOfUse().trim().length() == 0)
		{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Please answer client primary goal of use",null);
	        FacesContext.getCurrentInstance().addMessage(null, msg);    
	        clientInfoInValid = true;
		}	
		
		return clientInfoInValid;
	}

	public void setCmcSurveyAnswerSubmitterAndClientStuff(CmcSurveyAnswer cmcSurveyAnswer) 
	{
		cmcSurveyAnswer.setSurveySubmitterUserName(this.getCurrentUser().getUserName());
		cmcSurveyAnswer.setSurveySubmitterFirstName(this.getCurrentUser().getFirstName());
		cmcSurveyAnswer.setSurveySubmitterLastName(this.getCurrentUser().getLastName());
		cmcSurveyAnswer.setSurveySubmitterEmailAddress(this.getCurrentUser().getEmailAddress());
		cmcSurveyAnswer.setSurveyClientAge(this.getSurveyClientAge());
		
		CmcDiagnosis cmcDiagnosis = cmcDiagnosesDAO.getFullDiagnosesMap().get(this.getSurveyClientDiagnosis());
		cmcSurveyAnswer.setSurveyClientDiagnosis(cmcDiagnosis.getCmcDiagnosis());
		
		cmcSurveyAnswer.setSurveyClientLivingEnvironment(this.getSurveyClientLivingEnvironment());
		cmcSurveyAnswer.setSurveyClientPrimaryGoalOfUse(this.getSurveyClientPrimaryGoalOfUse());
	}

	public String submitIntellectualDisabilityAnswers() throws Exception
	{
		logger.info("entering submitPhysicalDisabilityAnswers");
		
		try
		{
			if (upperSectionAnswersInvalid(this.getIntellectualDisabilitiesSection1QuestionsHorizontalList(), Intellectual_Disabilities_Survey_Name)
			||  lowerSectionAnswersInvalid(this.getIntellectualDisabilitiesSection2QuestionsList())
			||  clientInfoInvalid()) 
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
	        
	        populateResultsPage("intellectual", this.getIntellectualDisabilitiesSection1QuestionsHorizontalList(), this.getIntellectualDisabilitiesSection2QuestionsList(), Intellectual_Disabilities_Survey_Name);
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
			if (upperSectionAnswersInvalid(this.getAutismDisorderSection1QuestionsHorizontalList(), Autism_Disorder_Survey_Name)
			||  lowerSectionAnswersInvalid(this.getAutismDisorderSection2QuestionsList())
			||  clientInfoInvalid()) 
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
	        
	        populateResultsPage("autism", this.getAutismDisorderSection1QuestionsHorizontalList(), this.getAutismDisorderSection2QuestionsList(), Autism_Disorder_Survey_Name);
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
	

	private void populateResultsPage(String survey, List<DisabilityRow> topSectionList, List<CmcSurveyQuestion> bottomSectionList, String whichSurvey) 
	{
		logger.info("entering populateResultsPage for survey: " + survey);
		
		this.getResultsList().clear();
		
		//default all viability settings to true; if even one unable to perform is selected it'll get set to false.
		
		ResultsRow resultsRowPublicBus = new ResultsRow();
		ResultsRow resultsRowCoachBus = new ResultsRow();
		
		if (whichSurvey.equalsIgnoreCase(Physical_Disabilities_Survey_Name)
		||  whichSurvey.equalsIgnoreCase(Intellectual_Disabilities_Survey_Name))
		{			
			resultsRowPublicBus.setTransportationOption("Public (City) Bus");
			resultsRowPublicBus.setTransportationViable(true);			
			
			resultsRowCoachBus.setTransportationOption("Coach (Intercity Bus)");
			resultsRowCoachBus.setTransportationViable(true);
		}
		else //must be autism
		{
			resultsRowPublicBus.setTransportationOption("Bus");
			resultsRowPublicBus.setTransportationViable(true);
		}
		
		ResultsRow resultsRowTrainSubway = new ResultsRow();
		resultsRowTrainSubway.setTransportationOption("Train/Subway/Trolley/Tram");
		resultsRowTrainSubway.setTransportationViable(true);
		
		ResultsRow resultsRowTaxi = new ResultsRow();
		resultsRowTaxi.setTransportationOption("Taxi");
		resultsRowTaxi.setTransportationViable(true);
		
		ResultsRow resultsRowEhailing = new ResultsRow();
		resultsRowEhailing.setTransportationOption("Ehailing (Uber/Lyft)");
		resultsRowEhailing.setTransportationViable(true);
		
		ResultsRow resultsRowFerry = new ResultsRow();
		resultsRowFerry.setTransportationOption("Ferry");
		resultsRowFerry.setTransportationViable(true);
		
		ResultsRow resultsRowWalking = new ResultsRow();
		resultsRowWalking.setTransportationOption("Walking");
		resultsRowWalking.setTransportationViable(true);
		
		ResultsRow resultsRowBikeScooter = new ResultsRow();
		resultsRowBikeScooter.setTransportationOption("Bike/Scooter");
		resultsRowBikeScooter.setTransportationViable(true);
		
		for (int i = 0; i < bottomSectionList.size(); i++) 
		{
			CmcSurveyQuestion cmcsq = bottomSectionList.get(i);
			
			//skip "questions" that are only in there as subcategory separators on the questions pages
			if (cmcsq.getCmcSurveyQuestionEnglish().equalsIgnoreCase("Physical")
			||  cmcsq.getCmcSurveyQuestionEnglish().equalsIgnoreCase("Cognitive")
			||  cmcsq.getCmcSurveyQuestionEnglish().equalsIgnoreCase("Communication")
			||  cmcsq.getCmcSurveyQuestionEnglish().equalsIgnoreCase("Self-Monitoring")
			||  cmcsq.getCmcSurveyQuestionEnglish().equalsIgnoreCase("Behavioral"))
			{
				continue;
			}
						
			QuestionSkip questionSkip = new QuestionSkip();
			questionSkip.setSkipQuestion(cmcsq, survey);
			
			if (cmcsq.getAnswer().equalsIgnoreCase(Performs_Independently_Dropdown_Value))
			{
				if (questionSkip.isDoPublicBus())
				{
					resultsRowPublicBus.getSkillsAcquiredList().add(cmcsq.getCmcSurveyQuestionEnglish());
				}
				if (questionSkip.isDoCoachBus())
				{
					resultsRowCoachBus.getSkillsAcquiredList().add(cmcsq.getCmcSurveyQuestionEnglish());
				}
				if (questionSkip.isDoTrainSubway())
				{
					resultsRowTrainSubway.getSkillsAcquiredList().add(cmcsq.getCmcSurveyQuestionEnglish());
				}
				if (questionSkip.isDoTaxi())
				{
					resultsRowTaxi.getSkillsAcquiredList().add(cmcsq.getCmcSurveyQuestionEnglish());
				}
				if (questionSkip.isDoEhailing())
				{
					resultsRowEhailing.getSkillsAcquiredList().add(cmcsq.getCmcSurveyQuestionEnglish());
				}
				if (questionSkip.isDoFerry())
				{
					resultsRowFerry.getSkillsAcquiredList().add(cmcsq.getCmcSurveyQuestionEnglish());
				}
				if (questionSkip.isDoWalking())
				{
					resultsRowWalking.getSkillsAcquiredList().add(cmcsq.getCmcSurveyQuestionEnglish());
				}
				if (questionSkip.isDoBikeScooter())
				{
					resultsRowBikeScooter.getSkillsAcquiredList().add(cmcsq.getCmcSurveyQuestionEnglish());
				}
			}
			else if (cmcsq.getAnswer().equalsIgnoreCase(Potentially_Able_with_Training_Dropdown_Value))
			{
				if (questionSkip.isDoPublicBus())
				{
					resultsRowPublicBus.getSkillsNeedingDevelopmentList().add(cmcsq.getCmcSurveyQuestionEnglish());
				}
				if (questionSkip.isDoCoachBus())
				{
					resultsRowCoachBus.getSkillsNeedingDevelopmentList().add(cmcsq.getCmcSurveyQuestionEnglish());
				}
				if (questionSkip.isDoTrainSubway())
				{				
					resultsRowTrainSubway.getSkillsNeedingDevelopmentList().add(cmcsq.getCmcSurveyQuestionEnglish());
				}
				if (questionSkip.isDoTaxi())
				{
					resultsRowTaxi.getSkillsNeedingDevelopmentList().add(cmcsq.getCmcSurveyQuestionEnglish());
				}
				if (questionSkip.isDoEhailing())
				{
					resultsRowEhailing.getSkillsNeedingDevelopmentList().add(cmcsq.getCmcSurveyQuestionEnglish());
				}
				if (questionSkip.isDoFerry())
				{
					resultsRowFerry.getSkillsNeedingDevelopmentList().add(cmcsq.getCmcSurveyQuestionEnglish());
				}
				if (questionSkip.isDoWalking())
				{
					resultsRowWalking.getSkillsNeedingDevelopmentList().add(cmcsq.getCmcSurveyQuestionEnglish());
				}
				if (questionSkip.isDoBikeScooter())
				{
					resultsRowBikeScooter.getSkillsNeedingDevelopmentList().add(cmcsq.getCmcSurveyQuestionEnglish());
				}
			}
			else if (cmcsq.getAnswer().equalsIgnoreCase(Unable_to_Meet_Demands_Dropdown_Value))
			{
				if (questionSkip.isDoPublicBus())
				{
					resultsRowPublicBus.getSkillsUnableToPerformList().add(cmcsq.getCmcSurveyQuestionEnglish());
					resultsRowPublicBus.setTransportationViable(false);
				}
				if (questionSkip.isDoCoachBus())
				{
					resultsRowCoachBus.getSkillsUnableToPerformList().add(cmcsq.getCmcSurveyQuestionEnglish());
					resultsRowCoachBus.setTransportationViable(false);
				}
				if (questionSkip.isDoTrainSubway())
				{
					resultsRowTrainSubway.getSkillsUnableToPerformList().add(cmcsq.getCmcSurveyQuestionEnglish());
					resultsRowTrainSubway.setTransportationViable(false);
				}
				if (questionSkip.isDoTaxi())
				{
					resultsRowTaxi.getSkillsUnableToPerformList().add(cmcsq.getCmcSurveyQuestionEnglish());
					resultsRowTaxi.setTransportationViable(false);
				}
				if (questionSkip.isDoEhailing())
				{
					resultsRowEhailing.getSkillsUnableToPerformList().add(cmcsq.getCmcSurveyQuestionEnglish());
					resultsRowEhailing.setTransportationViable(false);
				}
				if (questionSkip.isDoFerry())
				{
					resultsRowFerry.getSkillsUnableToPerformList().add(cmcsq.getCmcSurveyQuestionEnglish());
					resultsRowFerry.setTransportationViable(false);
				}
				if (questionSkip.isDoWalking())
				{
					resultsRowWalking.getSkillsUnableToPerformList().add(cmcsq.getCmcSurveyQuestionEnglish());
					resultsRowWalking.setTransportationViable(false);
				}
				if (questionSkip.isDoBikeScooter())
				{
					resultsRowBikeScooter.getSkillsUnableToPerformList().add(cmcsq.getCmcSurveyQuestionEnglish());
					resultsRowBikeScooter.setTransportationViable(false);
				}
			}
			
		}	
			
		resultsList.add(resultsRowPublicBus);
		resultsList.add(resultsRowCoachBus);
		resultsList.add(resultsRowTrainSubway);
		resultsList.add(resultsRowTaxi);
		resultsList.add(resultsRowEhailing);
		resultsList.add(resultsRowFerry);
		resultsList.add(resultsRowWalking);
		resultsList.add(resultsRowBikeScooter);				
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

	private boolean upperSectionAnswersInvalid(List<DisabilityRow> tempList, String whichSurvey)
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
				
				if (whichSurvey.equalsIgnoreCase(Physical_Disabilities_Survey_Name)
				||  whichSurvey.equalsIgnoreCase(Intellectual_Disabilities_Survey_Name))
				{
					if (dr.getCoachBusAnswer() == null || dr.getCoachBusAnswer().trim().length() == 0)
					{
						errorFound = true;
					}	
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
				
			}
					
	    }
		
		return errorFound;				
	}
	
	private boolean lowerSectionAnswersInvalid(List<CmcSurveyQuestion> tempList)
	{
		boolean errorFound = false;
		
		for (int i = 0; i < tempList.size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = tempList.get(i);
			
			//skip "questions" that are only in there as subcategory separators on the questions pages
			if (cmcSurveyQuestion.getCmcSurveyQuestionEnglish().equalsIgnoreCase("Physical")
			||  cmcSurveyQuestion.getCmcSurveyQuestionEnglish().equalsIgnoreCase("Cognitive")
			||  cmcSurveyQuestion.getCmcSurveyQuestionEnglish().equalsIgnoreCase("Communication")
			||  cmcSurveyQuestion.getCmcSurveyQuestionEnglish().equalsIgnoreCase("Self-Monitoring")
			||  cmcSurveyQuestion.getCmcSurveyQuestionEnglish().equalsIgnoreCase("Behavioral"))
			{
				continue;
			}
			
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
	
	public String defaultAllPhysicalDisabilityAnswersToYes()
	{
		defaultAllPhysicalDisabilityAnswers("Yes", GREEN_STYLECLASS);
		return "";
	}
	
	public String defaultAllPhysicalDisabilityAnswersToNo()
	{
		defaultAllPhysicalDisabilityAnswers("No", RED_STYLECLASS);
		return "";
	}
	
	public String defaultAllPhysicalDisabilityAnswers(String defaultAnswer, String defaultStyleClass)
	{
		logger.info("entering defaultAllPhysicalDisabilityAnswers");
		
		for (int i = 0; i < this.getPhysicalDisabilitiesSection1QuestionsHorizontalList().size(); i++) 
	    {
			DisabilityRow dr = this.getPhysicalDisabilitiesSection1QuestionsHorizontalList().get(i);
			defaultDisabilityRow(dr, defaultAnswer, defaultStyleClass);			
	    }
		
		for (int i = 0; i < this.getPhysicalDisabilitiesSection2QuestionsHorizontalList().size(); i++) 
	    {
			DisabilityRow dr = this.getPhysicalDisabilitiesSection2QuestionsHorizontalList().get(i);
			defaultDisabilityRow(dr, defaultAnswer, defaultStyleClass);	
	    }
		
		for (int i = 0; i < this.getPhysicalDisabilitiesSection3QuestionsList().size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = this.getPhysicalDisabilitiesSection3QuestionsList().get(i);
			if (defaultAnswer.equalsIgnoreCase("Yes"))
			{
				cmcSurveyQuestion.setAnswer(Performs_Independently_Dropdown_Value);
				cmcSurveyQuestion.setAnswerStyle(GREEN_STYLECLASS);
			}
			else
			{
				cmcSurveyQuestion.setAnswer(Unable_to_Meet_Demands_Dropdown_Value);
				cmcSurveyQuestion.setAnswerStyle(RED_STYLECLASS);
			}
	    }
		
		return "";
		
	}
	
	public String defaultAllIntellectualDisabilityAnswersToYes()
	{
		defaultAllIntellectualDisabilityAnswers("Yes", GREEN_STYLECLASS);
		return "";
	}
	
	public String defaultAllIntellectualDisabilityAnswersToNo()
	{
		defaultAllIntellectualDisabilityAnswers("No", RED_STYLECLASS);
		return "";
	}
	
	public String defaultAllIntellectualDisabilityAnswers(String defaultAnswer, String defaultStyleClass)
	{
		logger.info("entering defaultAllIntellectualDisabilityAnswers");
		
		for (int i = 0; i < this.getIntellectualDisabilitiesSection1QuestionsHorizontalList().size(); i++) 
	    {
			DisabilityRow dr = this.getIntellectualDisabilitiesSection1QuestionsHorizontalList().get(i);
			defaultDisabilityRow(dr, defaultAnswer, defaultStyleClass);			
	    }
		
		for (int i = 0; i < this.getIntellectualDisabilitiesSection2QuestionsList().size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = this.getIntellectualDisabilitiesSection2QuestionsList().get(i);
			if (defaultAnswer.equalsIgnoreCase("Yes"))
			{
				cmcSurveyQuestion.setAnswer(Performs_Independently_Dropdown_Value);
				cmcSurveyQuestion.setAnswerStyle(GREEN_STYLECLASS);
			}
			else
			{
				cmcSurveyQuestion.setAnswer(Unable_to_Meet_Demands_Dropdown_Value);
				cmcSurveyQuestion.setAnswerStyle(RED_STYLECLASS);
			}
	    }
		
		return "";		
	}
	
	public String defaultAllAutismSpectrumDisorderAnswersToYes()
	{
		defaultAllAutismSpectrumDisorderAnswers("Yes", GREEN_STYLECLASS);
		return "";
	}
	
	public String defaultAllAutismSpectrumDisorderAnswersToNo()
	{
		defaultAllAutismSpectrumDisorderAnswers("No", RED_STYLECLASS);
		return "";
	}
	
	public String defaultAllAutismSpectrumDisorderAnswers(String defaultAnswer, String defaultStyleClass)
	{
		logger.info("entering defaultAllAllAutismSpectrumDisorderAnswers");
		
		for (int i = 0; i < this.getAutismDisorderSection1QuestionsHorizontalList().size(); i++) 
	    {
			DisabilityRow dr = this.getAutismDisorderSection1QuestionsHorizontalList().get(i);
			defaultDisabilityRow(dr, defaultAnswer, defaultStyleClass);			
	    }
		
		for (int i = 0; i < this.getAutismDisorderSection2QuestionsList().size(); i++) 
	    {
			CmcSurveyQuestion cmcSurveyQuestion = this.getAutismDisorderSection2QuestionsList().get(i);
			if (defaultAnswer.equalsIgnoreCase("Yes"))
			{
				cmcSurveyQuestion.setAnswer(Performs_Independently_Dropdown_Value);
				cmcSurveyQuestion.setAnswerStyle(GREEN_STYLECLASS);
			}
			else
			{
				cmcSurveyQuestion.setAnswer(Unable_to_Meet_Demands_Dropdown_Value);
				cmcSurveyQuestion.setAnswerStyle(RED_STYLECLASS);
			}
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
	
	private void defaultDisabilityRow(DisabilityRow dr, String defaultAnswer, String defaultStyleClass) 
	{
		dr.setPublicBusAnswer(defaultAnswer); 
		dr.setPublicBusAnswerStyle(defaultStyleClass); 
		dr.setCoachBusAnswer(defaultAnswer); 
		dr.setCoachBusAnswerStyle(defaultStyleClass); 
		dr.setTrainSubwayAnswer(defaultAnswer); 
		dr.setTrainSubwayAnswerStyle(defaultStyleClass); 
		dr.setTaxiAnswer(defaultAnswer); 
		dr.setTaxiAnswerStyle(defaultStyleClass); 
		dr.setEhailingAnswer(defaultAnswer); 
		dr.setEhailingAnswerStyle(defaultStyleClass); 
		dr.setFerryAnswer(defaultAnswer); 
		dr.setFerryAnswerStyle(defaultStyleClass); 
		dr.setWalkAnswer(defaultAnswer); 
		dr.setWalkAnswerStyle(defaultStyleClass); 
		dr.setBikeAnswer(defaultAnswer); 
		dr.setBikeAnswerStyle(defaultStyleClass); 
		dr.setOtherAnswer(defaultAnswer); 
		dr.setOtherAnswerStyle(defaultStyleClass); 
		dr.setPowerwheelchairAnswer(defaultAnswer); 
		dr.setManualwheelchairAnswer(defaultAnswer); 
		dr.setRollatorAnswer(defaultAnswer); 
		dr.setProstheticsAnswer(defaultAnswer); 
		dr.setWalkerAnswer(defaultAnswer); 
		dr.setCrutchescaneAnswer(defaultAnswer); 
		dr.setScooterAnswer(defaultAnswer); 
		dr.setPowerwheelchairAnswerStyle(defaultStyleClass); 
		dr.setManualwheelchairAnswerStyle(defaultStyleClass); 
		dr.setRollatorAnswerStyle(defaultStyleClass); 
		dr.setProstheticsAnswerStyle(defaultStyleClass); 
		dr.setWalkerAnswerStyle(defaultStyleClass); 
		dr.setCrutchescaneAnswerStyle(defaultStyleClass); 
		dr.setScooterAnswerStyle(defaultStyleClass); 		
	}
	
	public void loadUsers(DynamoClients dynamoClients)  throws Exception
	{
		logger.info("entering loadUsers");
		cmcUsersDAO = new CmcUsersDAO(dynamoClients);
		cmcUsersDAO.readAllUsersFromDB(); 
		logger.info("Cmc Users read in. List size = " + cmcUsersDAO.getFullCmcUsersList().size());
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
	

	private void loadCmcDiagnoses(DynamoClients dynamoClients)   throws Exception
	{
		logger.info("entering loadCmcDiagnoses");		
		cmcDiagnosesDAO = new CmcDiagnosesDAO(dynamoClients);
		cmcDiagnosesDAO.readAllDiagnosesFromDB();			
		logger.info("Cmc Diagnoses read in. List size = " + cmcDiagnosesDAO.getFullDiagnosesList().size());	
		
		physicalDiagnosesList.clear();
		SelectItem si = new SelectItem("","Select");
		physicalDiagnosesList.add(si);
		
		intellectualDiagnosesList.clear();
		si = new SelectItem("","Select");
		intellectualDiagnosesList.add(si);
		
		autismDiagnosesList.clear();
		si = new SelectItem("","Select");
		autismDiagnosesList.add(si);
		
		for (int i = 0; i < cmcDiagnosesDAO.getFullDiagnosesList().size(); i++) 
		{
			CmcDiagnosis cmcDiagnosis = cmcDiagnosesDAO.getFullDiagnosesList().get(i);
			si = new SelectItem(cmcDiagnosis.getCmcDiagnosisId(),cmcDiagnosis.getCmcDiagnosis());
			
			if (cmcDiagnosis.getCmcSurveyName().equalsIgnoreCase(Physical_Disabilities_Survey_Name))
			{				
				physicalDiagnosesList.add(si);
			}
			else if (cmcDiagnosis.getCmcSurveyName().equalsIgnoreCase(Intellectual_Disabilities_Survey_Name))
			{
				intellectualDiagnosesList.add(si);
			}
			else if (cmcDiagnosis.getCmcSurveyName().equalsIgnoreCase("Autism"))
			{
				autismDiagnosesList.add(si);
			}
		}
		
    }

	private void refreshSiteVisitsList() throws Exception 
	{	
		logger.info("entering refreshSiteVisitsList");
		siteVisitsList = cmcUsersDAO.readAllUsersFromDB();
	}

	private void refreshSurveyAnswerTotals() 
	{		
		logger.info("entering refreshSurveyAnswersList");
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

	public String getResultsPageTitle() {
		return resultsPageTitle;
	}

	public void setResultsPageTitle(String resultsPageTitle) {
		this.resultsPageTitle = resultsPageTitle;
	}

	public List<ResultsRow> getResultsList() {
		return resultsList;
	}

	public void setResultsList(List<ResultsRow> resultsList) {
		this.resultsList = resultsList;
	}

	public CmcUser getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(CmcUser currentUser) {
		this.currentUser = currentUser;
	}

	public List<SelectItem> getUsageReasonList() {
		return usageReasonList;
	}

	public void setUsageReasonList(List<SelectItem> usageReasonList) {
		this.usageReasonList = usageReasonList;
	}

	public List<CmcUser> getSiteVisitsList() {
		return siteVisitsList;
	}

	public void setSiteVisitsList(List<CmcUser> siteVisitsList) {
		this.siteVisitsList = siteVisitsList;
	}

	public List<Photo> getPhotosList() {
		return photosList;
	}

	public void setPhotosList(List<Photo> photosList) {
		this.photosList = photosList;
	}

	public List<ResponsiveOption> getResponsiveOptions1() {
		return responsiveOptions1;
	}

	public void setResponsiveOptions1(List<ResponsiveOption> responsiveOptions1) {
		this.responsiveOptions1 = responsiveOptions1;
	}

	public List<SelectItem> getAgesList() {
		return agesList;
	}

	public void setAgesList(List<SelectItem> agesList) {
		this.agesList = agesList;
	}

	public List<SelectItem> getLivingEnvironmentList() {
		return livingEnvironmentList;
	}

	public void setLivingEnvironmentList(List<SelectItem> livingEnvironmentList) {
		this.livingEnvironmentList = livingEnvironmentList;
	}

	public String getSurveyClientAge() {
		return surveyClientAge;
	}

	public void setSurveyClientAge(String surveyClientAge) {
		this.surveyClientAge = surveyClientAge;
	}

	public String getSurveyClientDiagnosis() {
		return surveyClientDiagnosis;
	}

	public void setSurveyClientDiagnosis(String surveyClientDiagnosis) {
		this.surveyClientDiagnosis = surveyClientDiagnosis;
	}

	public String getSurveyClientLivingEnvironment() {
		return surveyClientLivingEnvironment;
	}

	public void setSurveyClientLivingEnvironment(String surveyClientLivingEnvironment) {
		this.surveyClientLivingEnvironment = surveyClientLivingEnvironment;
	}

	public String getSurveyClientPrimaryGoalOfUse() {
		return surveyClientPrimaryGoalOfUse;
	}

	public void setSurveyClientPrimaryGoalOfUse(String surveyClientPrimaryGoalOfUse) {
		this.surveyClientPrimaryGoalOfUse = surveyClientPrimaryGoalOfUse;
	}

	public List<SelectItem> getPhysicalDiagnosesList() {
		return physicalDiagnosesList;
	}

	public void setPhysicalDiagnosesList(List<SelectItem> physicalDiagnosesList) {
		this.physicalDiagnosesList = physicalDiagnosesList;
	}

	public List<SelectItem> getIntellectualDiagnosesList() {
		return intellectualDiagnosesList;
	}

	public void setIntellectualDiagnosesList(List<SelectItem> intellectualDiagnosesList) {
		this.intellectualDiagnosesList = intellectualDiagnosesList;
	}

	public List<SelectItem> getAutismDiagnosesList() {
		return autismDiagnosesList;
	}

	public void setAutismDiagnosesList(List<SelectItem> autismDiagnosesList) {
		this.autismDiagnosesList = autismDiagnosesList;
	}

}
