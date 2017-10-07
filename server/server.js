var express=require('express');
var app=express();
var bodyparser=require('body-parser');
var fs = require('fs');
var mongoose=require('mongoose');
mongoose.connect('mongodb://127.0.0.1:27017/spartanz', { useMongoClient: true, promiseLibrary: global.Promise });

app.use(bodyparser.urlencoded({ extended: false }))
app.use(bodyparser.json());

//controllers
var authentication=require('./server/controllers/authenticate');
var treeController=require('./server/controllers/trees');
var userController=require('./server/controllers/user');
var offercontroller=require('./server/controllers/offers')
var imagecontroller=require('./server/controllers/image')

app.post('/upload', function(req, res) {
	console.log(req.files.image.originalFilename);
	console.log(req.files.image.path);
		fs.readFile(req.files.image.path, function (err, data){
		
		var newPath = __dirname + "/uploads/" + 	req.files.image.originalFilename;
		fs.writeFile(newPath, data, function (err) {
		if(err){
		res.json({'response':"Error"});
		}else {
		res.json({'response':"Saved"});
}
});
});
});
app.post('/api/trees/upload',imagecontroller.upload);
app.post('/api/user/redeem',offercontroller.redeem);
app.post('/api/user/rewards',offercontroller.offers);
app.post('/api/user/register',authentication.register);
app.post('/api/user/login',authentication.login);
app.post('/api/trees/mark',treeController.tag);
app.post('/api/trees/list',treeController.list);
app.post('/api/user/list',userController.list);
app.post('/api/trees/claim',treeController.care);
app.post('/api/user/karma',userController.karma);

app.get('/',function(req,res){
    res.send('There you go');
})
app.listen(3038,function(){
    console.log('tada!');
});
