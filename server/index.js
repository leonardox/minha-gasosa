var express = require('express');
var mongoose = require('mongoose');
var handlebars = require('express-handlebars');

var port = process.env.PORT || 5000;
var db = process.env.DB || 'mongodb://localhost:27017/minhagasosa' || 'mongodb://heroku_mhcrtkhx:lihoc3618usahfd81au68rqtjn@ds119728.mlab.com:19728/heroku_mhcrtkhx';

mongoose.connect(db, function(err) {
  if (err) throw err;
});

require('./models/User');
require('./models/Route');
require('./models/GasStation');
require('./models/Admin');

var UserModel = mongoose.model('User');
var RouteModel = mongoose.model('Route');
var CommentModel = mongoose.model('Comment');
var GasModel = mongoose.model('GasStation');
var AdminModel = mongoose.model('Admin');

var gasStation = require('./routes/gas');

var app = express();

app.use(express.static(__dirname + '/public'));
app.use(express.static(__dirname + '/bower_components'));

app.use('/gas', gasStation);

app.engine('handlebars', handlebars({
  defaultLayout: 'main'
}));
app.set('view engine', 'handlebars');

app.get('/', function(request, response) {
  response.render('admin_login', {})
});

app.listen(port, function() {
  console.log('Node app is running on port', port);

  RouteModel.findOneAndUpdate({
    name: 'AAA'
  }, {
    name: 'AAA',
    goingDistance: 123.76,
    startPoint: {lat: -5, lng: 4},
    extraPoints: [{lat: 6, lng: 9}, {lat: 67, lng: 98}]
  }, {
    upsert: true,
    'new': true
  });

  UserModel.findOneAndUpdate({ // se nao existir, insere
    firstName: "Zé"
  }, {
    firstName: "Zé",
    lastName: "Zezo",
    login: "Zezox"
  }, {
    upsert: true,
    'new': true
  }, function(err, document) {
    CommentModel.findOneAndUpdate({
      text: "AAAA"
    }, {
      text: "AAAA",
      author: document._id,
      thumbsUp: 45
    }, {
      upsert: true,
      'new': true
    }, function(err2, document2){
      GasModel.findOneAndUpdate({
        name: "Posto Ipiranga"
      }, {
        name: "Posto Ipiranga",
        comments: [document2._id],
        rating: 5,
        location: {lat: -7.2294637, lng: -35.9092364}
      }, {
        upsert: true,
        'new': true
      });
    });
  });

});


