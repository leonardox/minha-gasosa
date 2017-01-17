var express = require('express');
var mongoose = require('mongoose');
var handlebars = require('express-handlebars');

var port = process.env.PORT || 5000;
//var db = process.env.DB || 'mongodb://localhost:27017/minhagasosa' || 'mongodb://heroku_mhcrtkhx:lihoc3618usahfd81au68rqtjn@ds119728.mlab.com:19728/heroku_mhcrtkhx';
var db = process.env.DB || 'mongodb://heroku_mhcrtkhx:lihoc3618usahfd81au68rqtjn@ds119728.mlab.com:19728/heroku_mhcrtkhx' || 'mongodb://localhost:27017/minhagasosa';

var https = require('https');
var querystring = require('querystring');
var jwt = require('jsonwebtoken');

mongoose.connect(db, function(err) {
  if (err) throw err;
});




require('./models/User');
require('./models/Route');
require('./models/Location');
require('./models/GasStation');
require('./models/Admin');

var UserModel = mongoose.model('User');
var RouteModel = mongoose.model('Route');
var CommentModel = mongoose.model('Comment');
var GasModel = mongoose.model('GasStation');
var AdminModel = mongoose.model('Admin');
var LocationModel = mongoose.model('Location');
var StateModel = mongoose.model('State');
var CityModel = mongoose.model('City');


var gasStation = require('./routes/gas');
var routes = require('./routes/app');

var app = require('./config/app_config');
var router = express.Router();

app.set('fbDebugTokenUri', 'graph.facebook.com');

function getServerFbToken(){
  var options = {
    host: app.get('fbDebugTokenUri'),
    port: 443,
    headers: {
      'Content-Type': 'text/plain; charset=utf-8',
      'Accept': '*/*',
      'Accept-Encoding': 'gzip,deflate,sdch',
      'Accept-Language': 'pt-BR,pt;q=0.8,en-US;q=0.6,en;q=0.4'
    },
    path: "/oauth/access_token?client_id=212852169169812&client_secret=50f2866ae0f6fdfa2f7999e92b0948bd&redirect_uri=http://localhost:5000&grant_type=client_credentials",
    method: 'GET'
  };

  var req = https.request(options, function(res) {
    res.on('data', function(d){
      console.log("Successfully got facebook app token");
      var parsedResponse = querystring.parse(d.toString());
      console.log(parsedResponse.access_token);
      app.set('fbAppToken', parsedResponse.access_token);
    });
  });
  req.end();

  req.on('error', function(e){
    console.error(e);
  });
}

getServerFbToken();
app.set('superSecret', 'secret0');

router.use(function(req, res, next) {
  // check header or url parameters or post parameters for token
  var token;
  if(req.body != undefined){
    var token = req.query.token || req.headers['x-access-token'];
  }else{
    var token = req.query.token || req.headers['x-access-token'];
  }


  // decode token
  if (token) {

    // verifies secret and checks exp
    jwt.verify(token, app.get('superSecret'), function(err, decoded) {
      if (err) {
        return res.status(403).json({ success: false, message: 'Failed to authenticate token.' });
      } else {
        // if everything is good, save to request for use in other routes
        req.reqUser = decoded._doc;
        next();
      }
    });

  } else {

    // if there is no token
    // return an error
    return res.status(403).send({
      success: false,
      message: 'No token provided.'
    });

  }
});


app.use(express.static(__dirname + '/public'));
app.use(express.static(__dirname + '/bower_components'));

app.use('/', routes);
app.use('/api', router);
app.use('/api/gas', gasStation);

app.engine('handlebars', handlebars({
  defaultLayout: 'main'
}));
app.set('view engine', 'handlebars');


app.get('/', function(request, response) {
  response.render('admin_login', {})
});

app.listen(port, function() {
  console.log('Node app is running on port', port);
  //
  //LocationModel.find({}, function(err, locs){
  //  console.log('Locations: ' + locs);
  //  for(var i = 0; i< locs.length; i++){
  //    var location = locs[i];
  //    var newState = {
  //      sigla: location.sigla,
  //      nome: location.nome,
  //      index: i
  //    }
  //    new StateModel(newState).save(function(err, state){
  //      console.log("Saving for state: " + state.nome);
  //      for(var j = 0; j < locs[state.index].cidades.length; j++){
  //        var cdd = locs[state.index].cidades[j];
  //        var newCity = {
  //          nome: cdd,
  //          state: state._id
  //        }
  //        new CityModel(newCity).save(function(err, city){
  //          if(err){
  //            console.log('erro salvando cidade: ' + cidade);
  //          }else{
  //            console.log("Salvando cidade: " + city.nome);
  //          }
  //        })
  //      }
  //      console.log("----------------------------------//--------------------------");
  //    });
  //  }
  //});
  //RouteModel.findOneAndUpdate({
  //  name: 'AAA'
  //}, {
  //  name: 'AAA',
  //  goingDistance: 123.76,
  //  startPoint: {lat: -5, lng: 4},
  //  extraPoints: [{lat: 6, lng: 9}, {lat: 67, lng: 98}]
  //}, {
  //  upsert: true,
  //  'new': true
  //});
  //
  //UserModel.findOneAndUpdate({ // se nao existir, insere
  //  firstName: "Zé"
  //}, {
  //  firstName: "Zé",
  //  lastName: "Zezo",
  //  login: "Zezox"
  //}, {
  //  upsert: true,
  //  'new': true
  //}, function(err, document) {
  //  CommentModel.findOneAndUpdate({
  //    text: "AAAA"
  //  }, {
  //    text: "AAAA",
  //    author: document._id,
  //    thumbsUp: 45
  //  }, {
  //    upsert: true,
  //    'new': true
  //  }, function(err2, document2){
  //    GasModel.findOneAndUpdate({
  //      name: "Posto Ipiranga"
  //    }, {
  //      name: "Posto Ipiranga",
  //      comments: [document2._id],
  //      rating: 5,
  //      location: {lat: -7.2294637, lng: -35.9092364}
  //    }, {
  //      upsert: true,
  //      'new': true
  //    });
  //  });
  //});

});


