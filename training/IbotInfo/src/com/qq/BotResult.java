package com.qq;

import java.util.ArrayList;

public class BotResult {
	private String location;
	private int financeLimit;
	private String tranStock;
	private String projectName;
	private String companyName;
	private ArrayList<String> founderName= new ArrayList<String> ();
	private ArrayList<String> bizArea = new ArrayList<String> ();
		
	public BotResult()
	{
		location="none";
		financeLimit=0;
		tranStock="none";
		projectName="none";
		companyName="none";
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getFinanceLimit() {
		return financeLimit;
	}
	public void setFinanceLimit(int financeLimit) {
		this.financeLimit = financeLimit;
	}
	public String getTranStock() {
		return tranStock;
	}
	public void setTranStock(String tranStock) {
		this.tranStock = tranStock;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public ArrayList<String> getFounderName() {
		return founderName;
	}

	public void setFounderName(ArrayList<String> founderName) {
		this.founderName = founderName;
	}

	public ArrayList<String> getBizArea() {
		return bizArea;
	}

	public void setBizArea(ArrayList<String> bizArea) {
		this.bizArea = bizArea;
	}
	
}
