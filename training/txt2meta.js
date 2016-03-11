var fs = require("fs");
var util = require('util');
var bosonnlp = require('bosonnlp');

exports.txt2Meta = function(data, res){
	var city = "none";
	var startup = "none";
	var company = "none";
	var founders = new Array();
	var	money = 30000;
	var equity = "20%";
	var industries = new Array();
	
	console.log(data);
//	var key = '5mR6aTnx.4451.Gx2Jt_BBGdFE';
//	var nlp = new bosonnlp.BosonNLP(key);
//	nlp.ner('成都商报记者 姚永忠', function (result) {
//		result = result.substring(1,result.length-1).replace("entity","enTity");
//		var json = eval('(' + result + ')'); 
//		
//		var locationFlag = 0,cnameFlag = 0,pronameFlag = 0;
//		for(i = 0;i < json.enTity.length;i++)
//		{
//			var temp = 0;
//			
//			if(json.enTity[i][2] == "location" && locationFlag==0)
//			{
//				locationFlag = 1;
//				temp = parseInt(json.enTity[i][0]);
//				city = json.word[temp];
//			}
//			else if(json.enTity[i][2] == "product_name" && pronameFlag==0)
//			{
//				pronameFlag = 1;
//				temp = parseInt(json.enTity[i][0]);
//				startup = json.word[temp];
//			}
//			else if(json.enTity[i][2] == "company_name" && cnameFlag==0)
//			{
//				cnameFlag = 1;
//				temp = parseInt(json.enTity[i][0]);
//				company = json.word[temp];
//			}
//			else if(json.enTity[i][2] == "person_name")
//			{
//				temp = parseInt(json.enTity[i][0]);
//				founders.push(json.word[temp]);
//			}
//		}
//	});
//	
//	var text = "垃圾回收垃圾啊垃圾啊垃圾啊啊"; 
//	nlp.extractKeywords(text, function (data) {
//	    data = JSON.parse(data);
//	    console.log(data);
//	    console.log("city"+city+"startup"+startup+"company"+company);
//	});
//	
//	console.log("city"+city+"startup"+startup+"company"+company);
	var metaResult = {
		"city":city,
		"startup":startup,
		"company":company,
		"founders":founders.toString(),
		"money":"30000",
		"equity":"20%",
		"industries":"[农业,科技]"
	};
	
	
	res.send(util.inspect(metaResult));
	res.send("hello");
}

