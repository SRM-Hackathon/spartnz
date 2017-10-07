var mongoose=require('mongoose')
var Offer=require('./server/datasets/offers.js')
mongoose.connect('mongodb://127.0.0.1:27017/spartanz', { useMongoClient: true, promiseLibrary: global.Promise });

var offers=[
   {offer:'25% off on all products',karma:100},
   
]
offers.forEach(function(element) {
    var offer=new Offer(element)
    offer.save()
}, this); 