/**
 * Created by GAEL on 11/01/2017.
 */

var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var Admin = mongoose.model('Admin');
var Location = mongoose.model('Location');
var City = mongoose.model('City');

var services = [
  {val:"oil", text: "Troca de Óleo"},
  {val:"sleep", text: "Dormida"} ,
  {val:"tire", text: "Calibragem de Pneus"} ,
  {val:"restaurant", text: "Restaurante"},
  {val:"atm", text: "Caixa Eletrônco"},
  {val:"glass", text: "Limpeza de para briza"},
  {val:"depStore", text: "Loja de Conveniência"}
];

router.get('/home', function(req, res) {
  var selectedStationId = req.params.stationId;

  Admin.findOne({username: req.reqUser.username}).populate({path:'gasStation', model: "GasStation", populate: {path: 'city', model: "City"}}).exec(function(err, owner) {
    var creditOptions = ["visa","master","hiper_card","dinners","elo","hiper", "amex", "union","cabal", "banes","cooper"];
    var debitOptions = ["visa_d","master_d","hiper_d","elo_d","union_d","cabal_d"];

    var selectedGasList = owner.gasStation.filter(function(gs){
      return gs._id == selectedStationId;
    });
    if(selectedGasList.length == 0 && (typeof owner.gasStation != 'undefined' && owner.gasStation.length > 0)){
      selectedGasList.push(owner.gasStation[0]);
    }
    res.render('edit_station', {
      debit: debitOptions,
      credit: creditOptions,
      services: services,
      gas: selectedGasList.length > 0 ? selectedGasList[0] : undefined,
      gasList: owner.gasStation,
      owner: owner._doc
    });
  });
});



router.get('/station/:stationId', function(req, res) {
    var selectedStationId = req.params.stationId;

    Admin.findOne({username: req.reqUser.username}).populate({path:'gasStation', model: "GasStation", populate: {path: 'city', model: "City"}}).exec(function(err, owner) {
        var creditOptions = ["visa","master","hiper_card","dinners","elo","hiper", "amex", "union","cabal", "banes","cooper"];
        var debitOptions = ["visa_d","master_d","hiper_d","elo_d","union_d","cabal_d"];
        var selectedGasList = owner.gasStation.filter(function(gs){
          return gs._id == selectedStationId;
        });
        res.render('edit_station', {
            debit: debitOptions,
            credit: creditOptions,
            services: services,
            gas: selectedGasList.length > 0 ? selectedGasList[0] : undefined,
            gasList: owner.gasStation,
            owner: owner._doc
        });
    });
});

router.get('/station', function(req, res) {
  Admin.findOne({username: req.reqUser.username}).populate({path:'gasStation', model: "GasStation", populate: {path: 'city', model: "City"}}).exec(function(err, owner) {
    if(typeof owner.gasStation != 'undefined' && owner.gasStation.length > 0){
      res.redirect('/admin/owner/station/'+ owner.gasStation[0]._id)
    }else{
        res.render('edit_station', {
            owner: owner._doc
        });
    }
  });
});

router.get('/new', function(req, res) {
    Admin.findOne({username: req.reqUser.username}).populate({path:'gasStation', model: "GasStation", populate: {path: 'city', model: "City"}}).exec(function(err, owner) {
        var creditOptions = ["visa","master","hiper_card","dinners","elo","hiper", "amex", "union","cabal", "banes","cooper"];
        var debitOptions = ["visa_d","master_d","hiper_d","elo_d","union_d","cabal_d"];
        res.render('new_station', {
            debit: debitOptions,
            credit: creditOptions,
            services: services,
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