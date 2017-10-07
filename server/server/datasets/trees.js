var mongoose = require('mongoose')
require('mongoose-double')(mongoose);

var SchemaTypes = mongoose.Schema.Types;
var treeschema=new mongoose.Schema({
    name:String,
    uid:String,
    lat: {
        type: SchemaTypes.Double
    },
    lng: {
        type: SchemaTypes.Double
    },
    lastcared:Date,
    marked:String,
    people:[{
        email:String
    }],
    level:Number,
    locked:{
        type:Boolean,
        default:false
    },
    lockedDate:{
        type:Date
        },
    lockedby:String
});
treeschema.methods.genuid=function (name) {
    this.uid=Date.now()+name;
};
module.exports=mongoose.model('trees',treeschema);