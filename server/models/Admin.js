var mongoose = require('mongoose');

var adminSchema = new mongoose.Schema({
    username: String,
    email: String,
    password: String,
    role: String,
    gasStation: { type: mongoose.Schema.Types.ObjectId, ref: 'GasStation' }
}, {
    timestamps: true
});

mongoose.model('Admin', adminSchema);