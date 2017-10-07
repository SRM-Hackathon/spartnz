var Tree=require('../datasets/trees.js');
var User=require('../datasets/user.js');

module.exports.list=function(req,res){
    console.log(req.body);
    Tree.find({lockedby:req.body.email},function(err,results){
            var i=0;
            if(results.length){
                /*var jsonarray=JSON.stringify(results);
                var js=JSON.parse(jsonarray);
                console.log(js);    */        
                return res.send(JSON.stringify(results)); 
                
            }
        });
}
module.exports.karma=function(req,res){
        User.find({email:req.body.email},function(err,results){
            console.log(results[0]);
            res.json(results[0]);
        });
}
