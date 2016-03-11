/*
 * Put request data into object
 */
var fs = require("fs");
var util = require("util");
var Txt2Meta = require("../training/txt2meta");
var formidable = require('formidable');

exports.getInfo = function(req, res){
      
    var contentType = req.get('Content-Type');
    console.log(contentType);
    
    var data = "DATA";
    var sender = req.body.sender || 'none';
	var receiver = 'none';
	var sendtime = 'none';
	var subject = 'none';
	var body = 'none';		
    if(contentType == "application/json")
    {
    	console.log(req.body);
	    sender = req.body.sender;
		receiver = req.body.receiver;
		sendtime = req.body.sendtime;
		subject = req.body.subject;
		body = req.body.body;		
		data = "发件人:"+sender+'\n'+"收件人："+receiver+'\n'+"发件时间："+sendtime+'\n'+"邮件主题："+subject+'\n'+"邮件正文："+body+'\n';		
		console.log(data);
    }
    else if(contentType.indexOf("multipart/form-data") >= 0)
    {
    	console.log(contentType);
    	var form = new formidable.IncomingForm();
	    form.encoding = 'utf-8';   
	    form.uploadDir = "../material/uploadFile/";
	    form.keepExtensions = true; 
	    form.maxFieldsSize = 2 * 1024 * 1024;
	    
	    form.on('file',function(name, file) {
	    	console.log("file:"+name);
	    	console.log(file.path);
	    	fs.rename(file.path, form.uploadDir + file.name);
		});
	    form.on('field',function(name, value){
	    	if(name === "sender")
	    	{
	    		sender = value;
	    	}
	    	else if(name === "receiver")
	    	{
	    		receiver = value;
	    	}
	    	else if(name === "sendtime")
	    	{
	    		sendtime = value;
	    	}
	    	else if(name === "subject")
	    	{
	    		subject = value;
	    	}
	    	else if(name === "body")
	    	{
	    		body = value;
	    	}
	    });
	    form.on('error', function(err) {
	    	console.log(err);
		});
	  	form.parse(req);
//	  	console.log(util.inspect(field));
		data = "发件人:"+sender+'\n'+"收件人："+receiver+'\n'+"发件时间："+sendtime+'\n'+"邮件主题："+subject+'\n'+"邮件正文："+body+'\n';		
		console.log(data);
	}
	console.log("lalallala");	
//	var filename = '../material/output.txt';
//	var writerStream = fs.createWriteStream(filename);// 创建一个可以写入的流，写入到文件 output.txt 中
//	writerStream.write(data,'UTF8');
//	writerStream.end();
//	writerStream.on('finish', function() {
//	    console.log("写入完成。");
//	});	
//	writerStream.on('error', function(err){
//	   console.log(err.stack);
//	});	
//	console.log("程序执行完毕");
	
	Txt2Meta.txt2Meta(data, res);
};
