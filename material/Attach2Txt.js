var needle = require('needle');
var util = require('util');

exports.getAttachTxt = function(files,uploadDir,data,callback){	
	var fileSum = getStrCount(util.inspect(files),"fileUpload");
	var count = 0;
	for(var i=1 ;i<=fileSum ;i++) {
		var fileNum = 'fileUpload' + i;
		fs.rename(files[fileNum].path, uploadDir + files[fileNum].name);
		var datatosend = {
		  file:{file:uploadDir + files[fileNum].name, content_type:files[fileNum].type},
		  apikey:'c173872b-5938-457f-9083-332c418ccf25',
		  extract_metadata:'false',
		  extract_text:'true',
		  extract_xmlattributes:'false'
		};
		needle.post('https://api.havenondemand.com/1/api/sync/extracttext/v1', datatosend, { multipart: true }, function(err, resp, body){
			if (!err && resp.statusCode == 200)
			{								
				data = data + resp.body.document[0].content;	
				count ++;
				if(count == fileSum)
				{
					callback(data);
				}
			}	   
		});	
	}
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