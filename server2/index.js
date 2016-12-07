var express = require('express');
var mongoose = require('mongoose');
var models = require('./models');

mongoose.connect('mongodb://heroku_mhcrtkhx:lihoc3618usahfd81au68rqtjn@ds119728.mlab.com:19728/heroku_mhcrtkhx', function(err) {
  if (err) throw err;
});

var UserModel = mongoose.model('User');
var RouteModel = mongoose.model('Route');
var CommentModel = mongoose.model('Comment');
var GasModel = mongoose.model('GasStation');


var app = express();

app.set('port', (process.env.PORT || 5000));

app.use(express.static(__dirname + '/public'));

// views is directory for all template files
app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');

app.get('/', function(request, response) {
  var user = new UserModel();
  user.firstName = "Zé" + (new Date()).getTime();
  user.lastName = "Zezo" + (new Date()).getTime();
  user.login = "Zezox" + (new Date()).getTime();
  user.save(function(err){
    var comment = CommentModel();
    comment.text = "AAAA" + (new Date()).getTime();
    comment.author = user._id;
    comment.thumbsUp = 45;
    comment.save(function(err){
      var gas = GasModel();
      gas.name = "Posto LulZ";
      gas.comments = [comment._id];
      gas.rating = 5;
      gas.location = {lat: 45, lng: 87};
      gas.save();
    });
  });
  var route = new RouteModel();
  route.name = 'AAA';
  route.goingDistance = 123.76;
  route.startPoint = {lat: -5, lng: 4};
  route.extraPoints = [{lat: 6, lng: 9}, {lat: 67, lng: 98}];
  route.save();


  response.send('ok');
});

app.listen(app.get('port'), function() {
  console.log('Node app is running on port', app.get('port'));
});


