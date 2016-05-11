package com.qq;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BotResult {
	private String location;
	private String financeLimit;
	private String tranStock;
	private String projectName;
	private String companyName;
	private Set<String> founderName = new HashSet<String>();
	private String bizArea;

	public BotResult()
	{
		location = "none";
		financeLimit = "none";
		tranStock = "none";
		projectName = "none";
		companyName = "none";
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getFinanceLimit() {
		return financeLimit;
	}
	public void setFinanceLimit(String financeLimit) {
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

	public Set<String> getFounderName() {
		return founderName;
	}

	public void setFounderName(Set<String> founderName) {
		this.founderName = founderName;
	}
	public String getBizArea() {
		return bizArea;
	}

	public void setBizArea(String bizArea) {
		this.bizArea = bizArea;
	}
	
}
