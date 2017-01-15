/**
 * Created by Alessandro on 04/12/2016.
 */
var mongoose = require('mongoose');

var stateSchema = new mongoose.Schema({
  sigla: String,
  nome: String
});

mongoose.model('State', stateSchema);

var citySchema = new mongoose.Schema({
  nome: String,
  state: { type: mongoose.Schema.Types.ObjectId, ref: 'State' }
});

mongoose.model('City', citySchema);