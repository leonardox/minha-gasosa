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
  GasModel.find({}, function (err, docs) {
    if(err){
      res.status(500).send("Error geting gas stations");
    }else {
      res.send(docs);
    }
  });
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

router.get('/comments/:id', function(req, res, next) {
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

router['delete']('/comments/:id', function(req, res, next) {
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
