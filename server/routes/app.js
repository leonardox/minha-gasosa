/**
 * Created by GAEL on 18/12/2016.
 */
var jwt = require('jsonwebtoken');
var https = require('https');
var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var User = mongoose.model('User');
var State = mongoose.model('State');
var City = mongoose.model('City');
var Admin = mongoose.model('Admin');

router.post('/register', function (req, res, next) {

  var user = req.body;

  //If the user isn't really authenticated in the server
  User.find({"fb_id": user.fb_id}, {}, function (e, docs) {
    if (docs.length == 0) {
      new User(user).save(user, function(err, usr){
        if (err) {
          res.status(500).send('Failed to register user!');
        } else {
          res.status(201).send("{\"status\":\"CREATED\"}");
        }
      });
    } else {
      res.status(405).send('User already registered!');
    }
  });
});

router.get('/states', function (req, res, next) {
  //If the user isn't really authenticated in the server
  State.find({}, {}, function (err, states) {
    if (err) {
      res.status(500).send('Failed to get states.');
    } else {
      res.send(states)
    }
  });
});

router.get('/cities', function (req, res, next) {
  //If the user isn't really authenticated in the server
  var stateId = req.query.state;
  console.log("StateId: " + stateId);
  City.find({state: stateId}, {}, function (err, cities) {
    if (err) {
      res.status(500).send('Failed to get cities.');
    } else {
      res.send(cities);
    }
  });
});



router.post('/authenticate', function (req, res, next) {
  console.log("Auth");
  var admin = req.body.username;
  var password = req.body.password;
  Admin.findOne({username: admin}).exec(function(err, admin){
    var ad = {};
    if(admin != undefined && admin.password == password){
      ad._doc = admin;
      var token = jwt.sign(ad, req.app.get('superSecret'), {
        expiresInMinutes: 43200000 // expires in 24 hours
      });
      res.cookie('x-access-token', token, { maxAge: 9000000000, httpOnly: true });
      //res.redirect(301, '/api/profile');
      console.log(admin.gasStation);
      if(admin.role == "OWNER"){
        res.redirect('/admin/owner/home');
      }else{
        res.status(301).send("Redirect");
      }
      //res.status(301).send("Redirect");
    }else{
      res.render('admin_login', {error: {field: "password", message: "Senha inválida"}});
    }
  });
});

router.get('/dbg-auth', function (req, res, next) {
  console.log("Auth");
  var fbId = req.query.fbId;
  User.find({"fb_id": fbId}, function(err, users){
    var user = users[0];
    console.log("user: " + JSON.stringify(user));
    user.registered = true;
    var token = jwt.sign(user, req.app.get('superSecret'), {
      expiresInMinutes: 43200000 // expires in 24 hours
    });
    console.log("Token: " + token);
    res.send(token);
  });

});

router.get('/auth', function (req, res, next) {
  console.log("Auth");
  var fbToken = req.query.fbToken;
  var appToken = req.app.get('fbAppToken');
  var fbValidationOptions = {
    host: req.app.get('fbDebugTokenUri'),
    port: 443,
    headers: {},
    path: "/debug_token?input_token=" + fbToken + "&access_token=" + appToken,
    method: 'GET'
  };

  var reqFbValidation = https.request(fbValidationOptions, function (fbResponse) {
    var validUser = false;
    var body = '';
    fbResponse.on('data', function (chunk) {
      body += chunk;
    });
    fbResponse.on('end', function () {
      var dataObject = JSON.parse(body);
      if (typeof dataObject.data !== 'undefined' && typeof dataObject.data.is_valid !== 'undefined') {
        validUser = dataObject.data.is_valid;
      }
      User.find({"fb_id": dataObject.data.user_id}, function(err, users){
        if(err){
          res.status(500).send('Failed to find user!');
        }else{
          if (users.length == 0) {
            console.log("TRR");
            res.status(403).send('User not registered');
          }else if(users[0].fb_id == undefined){
            res.status(403).send('User not registered');
          }else{
            var user = users[0];
            console.log("user: " + JSON.stringify(user));
            user.registered = true;
            var token = jwt.sign(user, req.app.get('superSecret'), {
              expiresInMinutes: 43200000 // expires in 24 hours
            });
            console.log("Token: " + token);
            res.send(token);
          }
        }
      });
    });
  });


  reqFbValidation.on('error', function (e) {
    res.status(500).send('FB auth server anavailable!');
  });

  reqFbValidation.write('data\n');
  reqFbValidation.write('data\n');
  reqFbValidation.end();

});

module.exports = router;