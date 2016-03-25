/*
 * Put request data into object
 */
var util = require("util");
var Txt2Meta = require("../training/txt2meta");
var GetAttach = require('./Attach2Txt');
var formidable = require('formidable');

exports.getInfo = function(req, res){    
    var contentType = req.get('Content-Type');    	
    if(contentType == "application/json")
    {
    	DealAppJson(req,res);
    }
    else if(contentType.indexOf("multipart/form-data") >= 0)
    {
    	DealMultiData(req,res);
	}
};
function DealAppJson(req,res)
{
    var sender = req.body.sender || 'none';
	var receiver = req.body.receiver || 'none';
	var sendtime = req.body.sendtime || 'none';
	var subject = req.body.subject || 'none';
	var body = req.body.body || 'none';		
	var data = "发件人:"+sender+'\n'+"收件人："+receiver+'\n'+"发件时间："+sendtime+'\n'+"邮件主题："+subject+'\n'+"邮件正文："+body+'\n';		
	console.log(data);
	Txt2Meta.txt2Meta(data, res);
};
function DealMultiData(req,res)
{
	var form = new formidable.IncomingForm();
    form.encoding = 'utf-8';   
    form.uploadDir = "../material/uploadFile/";
    form.keepExtensions = true; 
    form.maxFieldsSize = 2 * 1024 * 1024;
   
   	form.parse(req,function(err, fields, files) {
		var sender = fields.sender || 'none';
		var receiver = fields.receiver || 'none';
		var sendtime = fields.sendtime || 'none';
		var subject = fields.subject || 'none';
		var body = fields.body || 'none';			
		var allData = "发件人:"+sender+'\n'+"收件人："+receiver+'\n'+"发件时间："+sendtime+'\n'+"邮件主题："+subject+'\n'+"邮件正文："+body+'\n';							
		GetAttach.getAttachTxt(files,form.uploadDir,allData,function(data){
			Txt2Meta.txt2Meta(data,res);
		});		
	});	
	form.on('error', function(err) {
    	console.log(err);
	});
};

