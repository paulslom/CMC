package com.pas.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultsRow implements Serializable
{
	private static final long serialVersionUID = 131158039169073163L;
	
	private String transportationOption;
	private boolean transportationViable;
	private List<String> skillsAcquiredList = new ArrayList<>();
	private String skillsAcquiredDisplay = "";
	private List<String> skillsNeedingDevelopmentList = new ArrayList<>();
	private String skillsNeedingDevelopmentDisplay = "";
	private List<String> skillsUnableToPerformList = new ArrayList<>();
	private String skillsUnableToPerformDisplay = "";
	
	private static String HTML_CRLF = "<br><br>";
	
	@Override
	public String toString() 
	{
		String myString = "Transportation Option: " + transportationOption + " transportationViable: " + transportationViable;
		return myString;
	}

	public String getTransportationOption() {
		return transportationOption;
	}

	public void setTransportationOption(String transportationOption) {
		this.transportationOption = transportationOption;
	}

	public boolean isTransportationViable() {
		return transportationViable;
	}

	public void setTransportationViable(boolean transportationViable) {
		this.transportationViable = transportationViable;
	}

	
	public String getSkillsAcquiredDisplay() 
	{
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < skillsAcquiredList.size(); i++) 
		{
			String skillAcquired = skillsAcquiredList.get(i);
			sb.append(skillAcquired + HTML_CRLF);
		} 
		
		skillsAcquiredDisplay = sb.toString();
		
		return skillsAcquiredDisplay;
	}

	public void setSkillsAcquiredDisplay(String skillsAcquiredDisplay) {
		this.skillsAcquiredDisplay = skillsAcquiredDisplay;
	}

	public String getSkillsNeedingDevelopmentDisplay() 
	{
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < skillsNeedingDevelopmentList.size(); i++) 
		{
			String skillAcquired = skillsNeedingDevelopmentList.get(i);
			sb.append(skillAcquired + HTML_CRLF);
		} 
		
		skillsNeedingDevelopmentDisplay = sb.toString();
		
		return skillsNeedingDevelopmentDisplay;
	}

	public void setSkillsNeedingDevelopmentDisplay(String skillsNeedingDevelopmentDisplay) {
		this.skillsNeedingDevelopmentDisplay = skillsNeedingDevelopmentDisplay;
	}

	public String getSkillsUnableToPerformDisplay() 
	{
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < skillsUnableToPerformList.size(); i++) 
		{
			String skillAcquired = skillsUnableToPerformList.get(i);
			sb.append(skillAcquired + HTML_CRLF);
		} 
		
		skillsUnableToPerformDisplay = sb.toString();
		
		return skillsUnableToPerformDisplay;
	}

	public void setSkillsUnableToPerformDisplay(String skillsUnableToPerformDisplay) {
		this.skillsUnableToPerformDisplay = skillsUnableToPerformDisplay;
	}

	public List<String> getSkillsAcquiredList() {
		return skillsAcquiredList;
	}

	public void setSkillsAcquiredList(List<String> skillsAcquiredList) {
		this.skillsAcquiredList = skillsAcquiredList;
	}

	public List<String> getSkillsNeedingDevelopmentList() {
		return skillsNeedingDevelopmentList;
	}

	public void setSkillsNeedingDevelopmentList(List<String> skillsNeedingDevelopmentList) {
		this.skillsNeedingDevelopmentList = skillsNeedingDevelopmentList;
	}

	public List<String> getSkillsUnableToPerformList() {
		return skillsUnableToPerformList;
	}

	public void setSkillsUnableToPerformList(List<String> skillsUnableToPerformList) {
		this.skillsUnableToPerformList = skillsUnableToPerformList;
	}

	
}
