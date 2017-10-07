var Tree=require('../datasets/trees.js');
var User=require('../datasets/user.js');

module.exports.tag=function(req,res){
    console.log('added');
    
    tree=new Tree(req.body);
    tree.genuid(req.body.name);
    tree.save();
    User.find({email:req.body.marked},function(err,Results){
        if(Results.length){
            Results[0].karma=Results[0].karma+10;
            Results[0].save();
            console.log('+10 karma')
        }
    });
    response = {
        error: false,
        details: 'Tree added'
    };
    res.json(response);
};
module.exports.list=function(req,res){
    var lt=req.body.lat;
    var ln=req.body.lng;
    Tree.find({},function(err,results){
        var i=0;
        if(results.length){
            var jsonarray=JSON.stringify(results);
            console.log(jsonarray);            
            res.send(jsonarray);
            
        }
    });
}
module.exports.care=function(req,res){
    console.log(req.body);
    Tree.find({uid:req.body.uid},function(err,results){
        if(results.length){
            if(results[0].locked){
            
                console.log({
                    error:true,
                    details:'Already Locked'
                });
                return res.send({
                    error:true,
                    details:'Already Locked'
                });
            }
            else{
                User.find({email:req.body.email},function(err,Results){
                    if(Results.length){
                        Results[0].karma=Results[0].karma+10;
                        Results[0].save();
                        console.log('+10 karma')
                    }
                });
                results[0].locked=true;
                results[0].lockedby=req.body.email;
                results[0].lockedDate=Date.now();
                results[0].save();
               
                console.log({
                    error:false,
                    details:' Locked'
                });
               return res.send({
                    error:false,
                    details:' Locked'
                });
            }
        }}
    );
}

