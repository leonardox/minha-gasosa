/**
 * Created by GAEL on 11/01/2017.
 */

var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var Admin = mongoose.model('Admin');

router.get('/home', function(req, res) {
  Admin.findOne({username: req.reqUser.username}).populate('gasStation').exec(function(err, owner) {
    console.log(JSON.stringify(req.reqUser));
    res.render('owner_home', {owner: owner})
  });
});

router.get('/station', function(req, res) {
  res.render('new_station', {});
});

router.get('/logout', function(req, res) {
  res.clearCookie('x-access-token');
  res.redirect('/');
});


//Rota para Enio Modificar/usar

router.get('/edit', function(req, res) {
  res.clearCookie('x-access-token');
  Admin.findOne({username: req.reqUser.username}).exec(function(err, owner) {
    res.render('view_de_enio', {owner: owner})
  });
});

router.post('/', function(req, res) {
  var owner = req.body;
  Admin.findByIdAndUpdate(req.reqUser._id, owner).exec(function(err, owner) {
    console.log(JSON.stringify(owner));
    res.render('owner_home', {owner: owner})
  });
});

///

module.exports = router;