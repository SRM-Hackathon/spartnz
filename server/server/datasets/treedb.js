var mongoose=require('mongoose')
var dbschema=new mongoose.Schema({
    uid:String,
    email:String,
    image:String
})
module.exports=mongoose.model('timeline', dbschema);