/**
 * Created by GAEL on 04/12/2016.
 */
/**
 * Created by Alessandro on 04/12/2016.
 */
var mongoose = require('mongoose'), Schema = mongoose.Schema;

var Comment = mongoose.Schema({
  text: Number,
  author: { type: Schema.Types.ObjectId, ref: 'User' },
  creationDate: Date,
  thumbsUp: Number
});

var Comment = module.exports = mongoose.model('Comment', Comment);

var gasSchema = mongoose.Schema({
  name: String,
  city: String,
  state: String,
  comments: [{ type: Schema.Types.ObjectId, ref: 'Comment' }],
  rating: Number,
  description: String,
  location: { lat: Number, lng: Number },
});

var GasStation = module.exports = mongoose.model('GasStation', gasSchema);