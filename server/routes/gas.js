/**
 * Created by GAEL on 10/12/2016.
 */
var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var GasModel = mongoose.model('GasStation');
/* GET users listing. */
router.get('/', function(req, res, next) {
  GasModel.find({}, function (err, docs) {
    if(err){
      res.status(500).send("Error geting gas stations");
    }else {
      res.send(docs);
    }
  });

});

module.exports = router;
