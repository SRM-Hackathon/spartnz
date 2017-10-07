var mongoose=require('mongoose')
var offerschema=new mongoose.Schema({
    offer:String,
    karma:Number
})
module.exports=mongoose.model('offers', offerschema);