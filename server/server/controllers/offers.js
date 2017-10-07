Offers=require('../datasets/offers.js');
var User=require('../datasets/user.js');

module.exports.offers=function(req,res){
    Offers.find({},function(err,results){
       console.log(results)
       return res.send(results)
    });
}
module.exports.redeem=function(req,res){
    User.find({email:req.body.email},function(err,results){
         var karma=results[0].karma;
         if(karma>req.body.karma){
             results[0].karma=results[0].karma-req.body.karma;
             results[0].save();
             res.json({error:false,details:'karma updated'})
             }else{
                 res.json({error:true,details:'not enough karma points'})
             }
    })
}