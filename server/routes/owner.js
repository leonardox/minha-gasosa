/**
 * Created by GAEL on 11/01/2017.
 */

var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var Admin = mongoose.model('Admin');

router.get('/home', function(req, res) {
  Admin.findOne({username: req.reqUser.username},function(err, owner) {
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


module.exports = router;