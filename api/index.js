// grab the packages we need
var express = require('express');
var app = express();
var port = process.env.PORT || 8080;

// routes will go here

// routes will go here
app.get('/api/ibot', function(req, res) {
  var id = req.param('id');
  var geo = req.param('geo');  

  res.send(id + ' ' + geo);
});

// start the server
app.listen(port);
console.log('Server started! At http://localhost:' + port);
