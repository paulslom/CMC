package com.pas.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class Utils 
{
	private static Logger logger = LogManager.getLogger(Utils.class);	
	
	private static ResourceBundle genericProps = ResourceBundle.getBundle("ApplicationProperties");
	
	public static String PAR_OR_WORSE_STYLECLASS = "textBlack";
	public static String BIRDIE_OR_BETTER_STYLECLASS = "textRed";
	
	public static int FRONT9_STYLE_HOLENUM = 19;
	public static int BACK9_STYLE_HOLENUM = 20;
	public static int TOTAL_STYLE_HOLENUM = 21;
	public static int NET_STYLE_HOLENUM = 22;
	
	public static String MY_TIME_ZONE = "America/New_York";
		
	public static String getLastYearsLastDayDate() 
	{
	    Calendar prevYear = Calendar.getInstance();
	    prevYear.add(Calendar.YEAR, -1);
	    String returnDate = prevYear.get(Calendar.YEAR) + "-12-31";
	    return returnDate;
	}
	
	public static String getOneMonthAgoDate() 
	{
	    Calendar calOneMonthAgo = Calendar.getInstance();
	    calOneMonthAgo.add(Calendar.MONTH, -1);
	    Date dateOneMonthAgo = calOneMonthAgo.getTime();
	    Locale locale = Locale.getDefault();
	    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", locale);
	    String returnDate = formatter.format(dateOneMonthAgo);
	    return returnDate;
	}
			
	@SuppressWarnings("unchecked")
	public static boolean isAdminUser()
	{
		boolean adminUser = false;
	
		Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		
		for (Iterator<SimpleGrantedAuthority> iterator = authorities.iterator(); iterator.hasNext();) 
		{
			SimpleGrantedAuthority simpleGrantedAuthority = (SimpleGrantedAuthority) iterator.next();
			if (simpleGrantedAuthority.getAuthority().contains("ADMIN"))
			{
				adminUser = true; //admin user can see all the rounds
				break;
			}			
		}
		
		return adminUser;
		
	}
	
	public static String getLoggedInUserName()
	{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
		String currentCmcUser = (String) session.getAttribute("currentCmcUser");
		
		currentCmcUser = SecurityContextHolder.getContext().getAuthentication().getName();
		
	    return currentCmcUser == null ? null : currentCmcUser.toLowerCase().trim();
	}	

	public static String getContextRoot()
	{
		String contextRoot = "";
		try
		{
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    	HttpSession httpSession = request.getSession();	
	        contextRoot = (String) httpSession.getAttribute(AppConstants.CONTEXT_ROOT);
		}
		catch (Exception e)
		{			
		}
		
		return contextRoot;
	}
	
	public static ArrayList<String> setEmailFullRecipientList(List<String> fullList) 
	{
		ArrayList<String> emailRecipients = new ArrayList<String>();
		
		String mailTo = genericProps.getString("mailTo");
		
		for (int i = 0; i < fullList.size(); i++) 
		{
			String emailAddress = "";
			
			if (emailAddress == null 
			|| emailAddress.trim().length() == 0 
			|| emailAddress.equalsIgnoreCase("unknown")
			|| emailAddress.equalsIgnoreCase(mailTo))
			{
				continue;
			}
			
			emailRecipients.add(emailAddress);
		}
		
		return emailRecipients;
	}	
	
	public static String getDayofWeekString(Date date) 
	{
		Locale locale = Locale.getDefault();
	    DateFormat formatter = new SimpleDateFormat("EEEE", locale);
	    return formatter.format(date);
	}
	
	public static String getEncryptedPassword(String unencryptedPassword)
	{
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encryptedPW = passwordEncoder.encode(unencryptedPassword);
		return encryptedPW;
	}
	

	public static void main(String[] args) throws Exception
    { 
    	logger.info("**********  Utils main method start  ***********"); 
    	String unencryptedPW = "NdUyv&jzCr35vEZ";
    	String encryptedPW = getEncryptedPassword(unencryptedPW);
    	logger.info("encrypted password for " + unencryptedPW + " is: " + encryptedPW); 
    	logger.info("**********  Utils main method end  ***********"); 
    }
	
				
}
