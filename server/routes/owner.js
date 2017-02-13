/**
 * Created by GAEL on 11/01/2017.
 */

var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var Admin = mongoose.model('Admin');
var Location = mongoose.model('Location');
var City = mongoose.model('City');

router.get('/home', function(req, res) {
  Admin.findOne({username: req.reqUser.username}).populate('gasStation').exec(function(err, owner) {
    console.log(JSON.stringify(req.reqUser));
    res.render('owner_home', {owner: owner})
  });
});

router.get('/station', function(req, res) {
  Admin.findOne({username: req.reqUser.username}).populate({path:'gasStation', model: "GasStation", populate: {path: 'city', model: "City"}}).exec(function(err, owner) {
    var creditOptions = ["visa","master","hiper_card","dinners","elo","hiper", "amex", "union","cabal", "banes","cooper"];
    var debitOptions = ["visa_d","master_d","hiper_d","elo_d","union_d","cabal_d"];
    res.render('new_station', {
      debit: debitOptions,
      credit: creditOptions,
      gas: owner._doc.gasStation._doc,
      owner: owner._doc
    });
  });

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