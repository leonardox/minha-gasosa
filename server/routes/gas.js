/**
 * Created by GAEL on 10/12/2016.
 */
var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var GasModel = mongoose.model('GasStation');
var CommentModel = mongoose.model('Comment');

/* GET gas station listing. */
router.get('/', function(req, res, next) {
  var userCity = req.reqUser.city;
  var gasCond = {};
  if(!(userCity == undefined || userCity == null || userCity == "")){
    gasCond.city = userCity;
  }
  GasModel.find(gasCond, function (err, docs) {
    if(err){
      res.status(500).send("Error geting gas stations");
    }else {
      res.send(docs);
    }
  });
});

/* GET gas station listing. */
router.get('/:id', function(req, res, next) {
  var gasId = req.param('id');
  GasModel.findOne({_id: gasId}).exec(function(err, gas){
    if(err || !gas){
      res.status(500).send("Error geting gas station.");
    }else{
      res.send(gas);
    }
  })
});

router.put('/wrong-price/:id', function(req, res, next) {
  var gasId = req.param('id');
  var userId = req.reqUser._id;
  GasModel.findOne({
    _id: gasId,
    wrongPriceReports: {$ne: userId}
  }).exec(function(err, gas){
    if(err){
      res.status(500).send("Error reporting price 1.");
    }else if (!gas){
      res.status(400).send("Already reported.");
    }else{
      gas.wrongPriceReports.push(userId);
      gas.save(function(err, gas){
        if(err){
          res.status(500).send("Error reporting price 2.");
        }else{
          res.status(201).send("Reported");
        }
      });
    }
  })
});

router.put('/wrong-location/:id', function(req, res, next) {
  var gasId = req.param('id');
  var userId = req.reqUser._id;
  GasModel.findOne({
    _id: gasId,
    wrongLocation: {$ne: userId}
  }).exec(function(err, gas){
    if(err){
      res.status(500).send("Error reporting location.");
    }else if (!gas){
      res.status(400).send("Already reported.");
    }else{
      gas.wrongLocationReports.push(userId);
      gas.save(function(err, newGas){
        if(err){
          res.status(500).send("Error reporting location.");
        }else{
          res.status(201).send("Reported");
        }
      });
    }
  })
});


router.put('/closed/:id', function(req, res, next) {
  var gasId = req.param('id');
  var userId = req.reqUser._id;
  GasModel.findOne({
    _id: gasId,
    closedReports: {$ne: userId}
  }).exec(function(err, gas){
    if(err){
      res.status(500).send("Error reporting location.");
    }else if (!gas){
      res.status(400).send("Already reported.");
    }else{
      gas.closedReports.push(userId);
      gas.save(function(err, newGas){
        if(err){
          res.status(500).send("Error reporting location.");
        }else{
          res.status(201).send("Reported");
        }
      });
    }
  })
});

router.put('/rating/:id', function(req, res, next) {
  var gasId = req.param('id');
  var rating = req.body.rating;
  var userId = req.reqUser._id;
  GasModel.findOne({
    _id: gasId,
    ratingCount: {$ne: userId}
  }).exec(function(err, gas){
    if(err){
      res.status(500).send("Error reporting location 1.");
    }else if (!gas){
      res.status(400).send("Already rated.");
    }else{
      gas.ratingCount.push(userId);
      gas.totalRating += rating;
      gas.rating = gas.totalRating / gas.ratingCount.length;
      gas.save(function(err, newGas){
        if(err){
          res.status(500).send("Error reporting location 2.");
        }else{
          res.status(201).send("Rated");
        }
      });
    }
  })
});


router.put('/', function(req, res, next) {
  var gasStation = req.body;
  new GasModel(gasStation).save(function(err, gas){
    if (err) {
      res.status(500).send('Failed to register gas station!');
    } else {
      res.status(201).send("{\"status\":\"CREATED\", \"id\": \""+ gas._id +"}");
    }
  });
});

router.post('/:id', function(req, res, next) {
  var editedGasStation = req.body;
  var gasId = req.param('id');
  GasModel.update({_id: gasId}, editedGasStation, {}, function(err, numAffected) {
    if(err){
      res.status(500).send('Failed to update gas station!');
    }else if(numAffected == 0){
      res.status(404).send('Gas Station not found!');
    }else{
      res.status(200).send("OK");
    }
  });
});

router['delete']('/:id', function(req, res, next) {
  var gasId = req.param('id');
  GasModel.remove({ _id: gasId}, function (err) {
    if(err){
      res.status(500).send('Could not delete gas station.');
    }else{
      res.status(200).send("DELETED");
    }
  });
});

router.put('/comment/:id', function(req, res, next) {
  var gasId = req.param('id');
  var userId = req.reqUser._id;
  var commentText = req.body.text;
  var newComment = {
    text: commentText,
    author: userId
  };
  new CommentModel(newComment).save(function(err, comment){
    if (err) {
      res.status(500).send('Failed to create comment.');
    } else {
      GasModel.findById(gasId, function (err, gas) {
        if(err || !gas){
          res.status(500).send('Failed to create comment.');
        }else{
          gas.comments.push(comment._id);
          gas.save(function(err, updatedGas){
            if(err){
              res.status(500).send('Failed to create comment.');
            }else{
              res.status(201).send("{\"status\":\"CREATED\", \"id\": \""+ comment._id +"}");
            }
          });
        }
      });
    }
  });
});

router.get('/comment/:id', function(req, res, next) {
  var gasId = req.param('id');
  GasModel.findOne({_id: gasId}).populate({path:'comments', model: "Comment", populate: {path: 'author', model: "User"}}).exec(function(err, gas){
    if(err || !gas){
      res.send([]);
    }else{
      gas.populate('author',function(err){
        if(err){
          res.status(500).send("Error getting comments");
        }else{
          res.send(gas.comments);
        }
      });
    }
  })
});

router['delete']('/comment/:id', function(req, res, next) {
  var commentId = req.param('id');
  CommentModel.remove({ _id: commentId}, function (err) {
    if(err){
      res.status(500).send('Could not delete comment.');
    }else{
      res.status(200).send("DELETED");
    }
  });
});




module.exports = router;
