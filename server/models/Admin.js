var mongoose = require('mongoose');

var adminSchema = new mongoose.Schema({
    username: String,
    email: String,
    password: String
}, {
    timestamps: true
});

mongoose.model('Admin', adminSchema);