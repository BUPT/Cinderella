package com.qq;

import java.util.ArrayList;

public class EmailInput {
	private String sender;
	private String receiver;
	private String sendTime;
	private String emailSubject;
	private String emailBody;
	//private String[] emailAttach={"null","null","null","null"};
	private ArrayList<String> emailAttach = new ArrayList<String> (); 
	
	public EmailInput()
	{
		sender="none";
		receiver="none";
		sendTime="none";
		emailSubject="none";
		emailBody="none";
		//emailAttach;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public ArrayList<String> getEmailAttach() {
		return emailAttach;
	}

	public void setEmailAttach(ArrayList<String> emailAttach) {
		this.emailAttach = emailAttach;
	}
    
	
}
