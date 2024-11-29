package com.pas.beans;

import java.io.Serializable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.pas.dao.CmcSurveyAnswersDAO;
import com.pas.dao.CmcSurveyQuestionsDAO;
import com.pas.dao.CmcSurveysDAO;
import com.pas.dynamodb.DynamoClients;
import com.pas.dynamodb.DynamoUtil;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named("pc_CmcMain")
@Component
@SessionScoped
public class CmcMain implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(CmcMain.class);	
		
	private final double id = Math.random();	
	
	private CmcSurveyQuestionsDAO cmcSurveyQuestionsDAO;
	private CmcSurveysDAO cmcSurveysDAO;
	private CmcSurveyAnswersDAO cmcSurveyAnswersDAO;
	
	public CmcMain() 
	{
		logger.info("Entering CmcMain constructor.  Should only be here ONE time with Spring singleton pattern implemented");	
		logger.info("CmcMain id is: " + this.getId());
		
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

	public CmcMain(String tempString)
	{		
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
		
}
