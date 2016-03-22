/*
 * Put request data into object
 */
var fs = require("fs");
var util = require("util");
var Txt2Meta = require("../training/txt2meta");
var formidable = require('formidable');
var needle = require('needle');


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
   
    form.on('error', function(err) {
    	console.log(err);
	});
	
	form.parse(req,function(err, fields, files) {
		var sender = fields.sender || 'none';
		var receiver = fields.receiver || 'none';
		var sendtime = fields.sendtime || 'none';
		var subject = fields.subject || 'none';
		var body = fields.body || 'none';			
		var allData = "发件人:"+sender+'\n'+"收件人："+receiver+'\n'+"发件时间："+sendtime+'\n'+"邮件主题："+subject+'\n'+"邮件正文："+body+'\n';					
		var fileSum = getStrCount(util.inspect(files),"fileUpload");
		var count = 0
		for(var i=1 ;i<=fileSum ;i++) {
			var fileNum = 'fileUpload' + i;
			fs.rename(files[fileNum].path, form.uploadDir + files[fileNum].name);
			allData = allData + sendPost(form.uploadDir + files[fileNum].name,files[fileNum].type,allData);
		}		
		console.log("main:"+allData);
	});
	res.writeHead(200, {'content-type': 'text/html'});
	res.end("hello!");
};
function sendPost(fileName,fileType,allData)
{	
	var datatosend = {
	  file:{file:fileName, content_type:fileType},
	  apikey:'c173872b-5938-457f-9083-332c418ccf25',
	  extract_metadata:'false',
	  extract_text:'true',
	  extract_xmlattributes:'false'
	};
			
	needle.post('https://api.havenondemand.com/1/api/sync/extracttext/v1', datatosend, { multipart: true }, function(err, resp, body){
		if (!err && resp.statusCode == 200)
	    allData = allData + resp.body.document[0].content;
	    console.log("sendpost:"+allData);
		return allData;
	});	
}

function getStrCount(scrstr,armstr)
{
	 var count=0;
	 while(scrstr.indexOf(armstr) >=1 )
	 {
	    scrstr = scrstr.replace(armstr,"")
	    count++;    
	 }
	 return count;
};
