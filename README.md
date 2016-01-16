# ibot
a bot that helps people to summary document in different way

## API

接口地址 :http://111.207.243.70:8088/IbotInfo/GetInfo  
  

请求方法 :POST  

请求参数(urlParam) :  

sender:发送人邮箱地址，默认值为“346786495@qq.com”。  

reciever:接收人邮箱地址，默认值为“wnbupt@qq.com” 。  

sendtime:邮件发送时间，默认值为“2015-12-16-10:43”。  

subject:邮件主题，默认值为“测试demo”。  

body:邮件正文，默认值为“快塞给我一封邮件吧！”。  

attachment:附件在本地的路径，可以上传多个，以“|”分隔。默认值为“null”。  

请求示例 :运行ibot/api/APIuse（java project）

###运行环境  

Eclipse IDE for Java Developers

Version: Mars.1 Release (4.5.1)  

JDK Version：1.8.0


