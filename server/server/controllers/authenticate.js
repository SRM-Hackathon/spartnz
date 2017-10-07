var User=require('../datasets/user.js');
var crypto=require('crypto');
module.exports.register=function(req,res){
    var check = {'email': req.body.email};
    if (/\s/.test(req.body.name)) {
        response = {
            error: true,
            details: 'Username cannot contain spaces'
        };
        res.json(response);
    }
    else {
    User.find({name:req.body.name},function (err,result) {
        if(result.length){
            response = {
                error: true,
                details: 'Username exists'
            };
            res.json(response);
        }

        else{
            User.find(check, function (err, result) {
                if(result.length) {
                    response = {
                        error: true,
                        details: 'email already exist'
                    };
                    res.json(response);

                }
                else {
                    if (req.body.password != req.body.password2) {
                        response = {
                            error: true,
                            details: 'passwords do not match'
                        };

                        res.json(response);
                    }
                    else {
                        var user_details = {
                            name: req.body.name,
                            email: req.body.email
                        };
                        user = new User(user_details);
                        user.setpassword(req.body.password);
                        user.save();
                        response = {
                            error: false,
                            details: 'Hurrey youve registered!'
                        };

                        res.json(response);

                    }
                }



            })

        }
    });}
   };
   module.exports.login=function (req,res) {
    var check = {'email': req.body.mail};
    User.find(check, function (err, result) {
        if(result.length === 1) {
            var salt=result[0].salt;
            var hash=crypto.pbkdf2Sync(req.body.password, salt, 1000, 64,'sha1').toString('hex');
            if(hash===result[0].hash){
                response = {
                    error: false,
                    details: 'Redirecting to your profile'
                };


            }
            else{
            response = {
                error: true,
                details: 'Email/Password Incorrect'
            };

            }
            res.json(response);
        }
        else{
            response = {
                error: true,
                details: 'Email/Password Incorrect'
            };
            res.json(response);
        }
    })

};
