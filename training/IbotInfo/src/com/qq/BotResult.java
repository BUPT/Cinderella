package com.qq;

public class BotResult {
	private String location;
	private int financeLimit;
	private String tranStock;
	private String projectName;
	private String companyName;
	private String founderName;
	private String bizArea;
	private double subness;
	
	public BotResult()
	{
		location="none";
		financeLimit=0;
		tranStock="none";
		projectName="none";
		companyName="none";
		founderName="none";
		bizArea="none";
		subness=0;
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
	public String getFounderName() {
		return founderName;
	}
	public void setFounderName(String founderName) {
		this.founderName = founderName;
	}
	public String getBizArea() {
		return bizArea;
	}
	public void setBizArea(String bizArea) {
		this.bizArea = bizArea;
	}

	public double getSubness() {
		return subness;
	}

	public void setSubness(double subness) {
		this.subness = subness;
	}
}
