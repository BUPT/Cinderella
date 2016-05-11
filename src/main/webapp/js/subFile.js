function fun()
{
	$.ajaxFileUpload({
		url: "GetInfo",
		secureuri: false,
		fileElementId: 'uploadFile1', //原使用方法 
//		fileElementId : ['uploadFile1','uploadFile2'],//现使用方法 
		dataType: 'json',
		success: function (data, status) {  
            //把图片替换  
			console.log(data);
			var index_begin = data.indexOf('>');
            var index_end = data.length;
			data = data.substring(index_begin+1,index_end);
			data =  eval('(' + data + ')'); 
            if(typeof(data.error) != 'undefined') {  
                if(data.error != '') {  
                    alert(data.error);  
                } else {  
                    alert(data.msg);  
                }  
            }  
			FillTable(data);
        },  
        error: function (data, status, e) {  
            alert(e);  
        }  
    });  
}
//
function FillTable(data)
{
	document.getElementById('city').innerHTML = data.city;
	document.getElementById('startup').innerHTML = data.startup;
	document.getElementById('company').innerHTML = data.company;
	document.getElementById('founders').innerHTML = data.founders;
	document.getElementById('money').innerHTML = data.money;
	document.getElementById('equity').innerHTML = data.equity;
	document.getElementById('industries').innerHTML = data.industries;
}