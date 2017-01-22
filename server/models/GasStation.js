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
  totalRating: { type: Number, default: 0 },
  ratingCount: [{ type: mongoose.Schema.Types.ObjectId, ref: 'User' }],
  description: String,
  gasPrice: Number,
  gasPlusPrice: Number,
  alcoolPrice: Number,
  location: { lat: Number, lng: Number },
  closedReports: [{ type: mongoose.Schema.Types.ObjectId, ref: 'User' }],
  wrongPriceReports: [{ type: mongoose.Schema.Types.ObjectId, ref: 'User' }],
  wrongLocationReports: [{ type: mongoose.Schema.Types.ObjectId, ref: 'User' }],
  wrongLocation: { type: Boolean, default: false },
  wrongPrice: { type: Boolean, default: false },
  closed: { type: Boolean, default: false },
  city: { type: mongoose.Schema.Types.ObjectId, ref: 'City' }
});

mongoose.model('GasStation', gasSchema);