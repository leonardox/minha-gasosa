/**
 * Created by Alessandro on 04/12/2016.
 */
var mongoose = require('mongoose'), Schema = mongoose.Schema;

var routeSchema = mongoose.Schema({
  name: String,
  roundTrip: Boolean,
  extra: Boolean,
  goingDistance: Number,
  backDistance: Number,
  extraPoints: [{ lat: Number, lng: Number}],
  startPoint: { lat: Number, lng: Number},
  endPoint: { lat: Number, lng: Number }
});

var Route = module.exports = mongoose.model('Route', routeSchema);