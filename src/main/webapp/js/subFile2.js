function fun()
{
	var sender = document.getElementById("sender").value;
	var receiver = document.getElementById("receiver").value;
	var sendtime = document.getElementById("sendtime").value;
	var subject = document.getElementById("subject").value;
	var body = document.getElementById("body").innerHTML;
	var datatosend = {
		"sender":sender,
		"receiver":receiver,
		"sendtime":sendtime,
		"subject":subject,
		"body":body
	};
//	alert(datatosend.sender);
	$.ajax({
	   type: "POST",
	   url: "GetInfo",
	   data: datatosend,
	   dataType: 'json',
	   success: function(data){
//	     console.log(data);
	     FillTable(data);
   		}
	});
}

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