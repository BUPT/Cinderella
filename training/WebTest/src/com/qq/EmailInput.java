package com.qq;

public class EmailInput {
	private String sender;
	private String reciever;
	private String sendTime;
	private String emailSubject;
	private String emailBody;
	private String emailAttach;
	
	public EmailInput()
	{
//		sender="noreply@coursera.org";
//		reciever="346786495@qq.com";
//		sendTime="2015骞�2鏈�5鏃�鏄熸湡浜� 涓婂崍10:31";
//		emailSubject="娆㈣繋鏉ュ埌Coursera锛�;
//		emailAttach="D:\\iBotest\\test.ppt";
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

	public String getEmailAttach() {
		return emailAttach;
	}

	public void setEmailAttach(String temp) {
		this.emailAttach = temp;
	}	
}
