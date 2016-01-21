package com.qq;

import java.util.ArrayList;

public class EmailInput {
	private String sender;
	private String reciever;
	private String sendTime;
	private String emailSubject;
	private String emailBody;
	//private String[] emailAttach={"null","null","null","null"};
	private ArrayList<String> emailAttach = new ArrayList<String> (); 
	
	public EmailInput()
	{
		sender="none";
		reciever="none";
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

	public String getReciever() {
		return reciever;
	}

	public void setReciever(String reciever) {
		this.reciever = reciever;
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
