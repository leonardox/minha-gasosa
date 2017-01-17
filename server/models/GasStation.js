/**
 * Created by GAEL on 04/12/2016.
 */
/**
 * Created by Alessandro on 04/12/2016.
 */
var mongoose = require('mongoose');

var Comment = mongoose.Schema({
  text: String,
  author: { type: mongoose.Schema.Types.ObjectId, ref: 'User' },
  creationDate: Date,
  thumbsUp: Number
});

mongoose.model('Comment', Comment);

var gasSchema = mongoose.Schema({
  name: String,
  city: String,
  state: String,
  comments: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Comment' }],
  rating: Number,
  description: String,
  gasPrice: Number,
  gasPlusPrice: Number,
  alcoolPrice: Number,
  location: { lat: Number, lng: Number },
  city: { type: mongoose.Schema.Types.ObjectId, ref: 'City' }
});

mongoose.model('GasStation', gasSchema);