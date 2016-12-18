/**
 * Created by Alessandro on 04/12/2016.
 */
var mongoose = require('mongoose');

var userSchema = mongoose.Schema({
  firstName: String,
  lastName: String,
  login: String,
  email: String,
  gender: String,
  address: String,
  city: String,
  state: String,
  country: String,
  phoneNumber: String,
  accountCreationDate: Date,
  lastLogin: Date,
  birthDate: Date,
  routes: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Route' }],
  fb_id: String
});

mongoose.model('User', userSchema);