package com.qq;

public class BotResult {
	private String location;//鍦扮偣
	private int financeLimit;//铻嶈祫棰濆害
	private String tranStock;//鍑鸿鑲℃潈
	private String projectName;//椤圭洰鍚嶇О
	private String companyName;//鍏徃鍚嶇О
	private String founderName;//鎴愮珛鑰呭悕瀛�
	private String bizArea;//鎵�睘琛屼笟
	private double subness;//缃俊绋嬪害
	
	public BotResult()
	{
//		location="鍖椾含";
//		financeLimit=20000;
//		tranStock="20%";
//		projectName="鍐滃か涔嬪";
//		companyName="鍖椾含甯傚啘涓氱鎶�湁闄愬叕鍙�;
//		founderName="寮犱笁";
//		bizArea="鍐滀笟姘戠敓";
//		subness=0;
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
