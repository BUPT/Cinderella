/*
 * Put request data into object
 */
var fs = require("fs");
var util = require("util");
var Txt2Meta = require("../training/txt2meta");

exports.getInfo = function(req, res){
   
    var sender = req.body.sender || 'none';
	var receiver = req.body.receiver || 'none';
	var sendtime = req.body.sendtime || 'none';
	var subject = req.body.subject || 'none';
	var body = req.body.body || 'none';
	
	var data = "发件人:"+sender+'\n'+"收件人："+receiver+'\n'+"发件时间："+sendtime+'\n'+"邮件主题："+subject+'\n'+"邮件正文："+body+'\n';		
	console.log(data);
		
	var filename = '../material/output.txt';
	var writerStream = fs.createWriteStream(filename);// 创建一个可以写入的流，写入到文件 output.txt 中
	writerStream.write(data,'UTF8');
	writerStream.end();
	writerStream.on('finish', function() {
	    console.log("写入完成。");
	});	
	writerStream.on('error', function(err){
	   console.log(err.stack);
	});	
	console.log("程序执行完毕");
	
	Txt2Meta.txt2Meta(data, res);
};
