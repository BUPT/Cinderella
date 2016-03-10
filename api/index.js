/**
 * Module dependencies.
 */
var express = require('express');
var routes = require('../material/All2Txt');
var http = require('http');
var path = require('path');
var app = express();

// all environments参数设置
app.set('port', process.env.PORT || 8080);
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.json());
app.use(express.urlencoded());
app.use(express.methodOverride());
app.use(app.router);

//app.configure(function () {
//  app.use(express.bodyParser({ keepExtensions: true, uploadDir: '/tmp' }));
//});

// development only app.use启用大量的中间件
if ('development' == app.get('env')) {
  app.use(express.errorHandler());
}
//是一个路由控制器
//app.get('/', routes.index);
app.post('/ibot', routes.getInfo);

http.createServer(app).listen(app.get('port'), function(){
  console.log('Express server listening on port ' + app.get('port'));
});
