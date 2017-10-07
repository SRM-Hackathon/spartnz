var User=require('../datasets/user.js');
var Tree=require('../datasets/trees.js');
var Timeline=require('../datasets/treedb.js');
module.exports.upload=function(req,res){
    console.log(req.body);
    var timeline=new Timeline(req.body);
    timeline.save();
    Tree.find({uid:req.body.uid},function(err,results){
        results[0].locked=false;
        results[0].lockedby='';
        results[0].save();
    });
    User.find({email:req.body.email},function(err,results){
        results[0].karma=results[0].karma+100;
        results[0].save();
    });
    res.json({error:false,details:"mission complete"})
}