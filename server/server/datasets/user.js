var mongoose =require('mongoose');
var crypto=require('crypto');
var userschema=new mongoose.Schema({

    email:{
        type:String,
        unique:true,
        required:true
    },
    name:{
        type:String,
        required:true

    },
    coupons:[{
        date:{
            type:Date,
            default:Date.now()
        },
        coupon:String,
        karma:Number
     } ],
    karma:{
        type:Number,
        default:0
    },
    hash:String,
    salt:String
});

userschema.methods.setpassword=function (password) {
    this.salt=crypto.randomBytes(16).toString('hex');
    this.hash=crypto.pbkdf2Sync(password, this.salt, 1000, 64,'sha1').toString('hex');

};
userschema.methods.validatepassword=function(password){
    var hash=crypto.pbkdf2Sync(password, this.salt, 1000, 64).toString('hex');
    return this.hash === hash;
};

module.exports=mongoose.model('users', userschema);