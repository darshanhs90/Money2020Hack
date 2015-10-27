var app = require('express')();
var express = require('express');
var cfenv = require('cfenv');
var cors = require('cors');
var app = express();
var bodyParser = require('body-parser');
var paypal = require('paypal-rest-sdk');
var accountSid = 'AC07275e4294f1b0d42623c3ec9559911e';
var authToken = "650d049a9bd99323fb899ce4b9e84fcc";
var client = require('twilio')(accountSid, authToken);
app.use(cors());
app.use(express.static(__dirname + '/public'));
app.use(bodyParser.json());

paypal.configure({
  'mode': 'sandbox', //sandbox or live
  'client_id': 'AeKFTEWOLJ3zEjq116kQAXhVy2-RINQNC7Er4aakfhFWkaOmDL4yZS3IkbG7RaZqPolH60G7e4wOIII5',
  'client_secret': 'EJctgdv0F9vbM04pcf5TiUmwkI-XRZsumjhA-yBhjNHc_fIMEdr-nOXHhkZ_bHXavTQXAhSv074fsZv5'
});
app.listen(1337, '127.0.0.1', function() {
    console.log("server starting on " + 1337);
});



var create_payment_json = {
    "intent": "sale",
    "payer": {
        "payment_method": "credit_card",
        "funding_instruments": [{
            "credit_card": {
                "type": "visa",
                "number": "4417119669820331",
                "expire_month": "11",
                "expire_year": "2018",
                "cvv2": "874",
                "first_name": "Joe",
                "last_name": "Shopper",
                "billing_address": {
                    "line1": "52 N Main ST",
                    "city": "Johnstown",
                    "state": "OH",
                    "postal_code": "43210",
                    "country_code": "US"
                }
            }
        }]
    },
    "transactions": [{
        "amount": {
            "total": "7",
            "currency": "USD",
            "details": {
                "subtotal": "5",
                "tax": "1",
                "shipping": "1"
            }
        },
        "description": "This is the payment transaction description."
    }]
};
var flag=false;
app.get('/createPayment',function(req,res){
    paypal.payment.create(create_payment_json, function (error, payment) {
        if (error) {
            throw error;
        } else {
            flag=true;
            console.log(payment);
            res.send(payment);
            res.end();
        }
    });
});

var search_attr = {
    "start_invoice_date": "2010-05-10 PST",
    "end_invoice_date": "2015-04-10 PST",
    "page": 1,
    "page_size": 3,
    "total_count_required": true
};

app.get('/checkPayment',function(req,res){
    paypal.invoice.search(search_attr, function (error, results) {
        if (error) {
            throw error;
        } else {
            if(flag){
                flag=false;
                res.send({'a':'1'});
            }
            else
                res.send({'a':'0'});
            res.end();
        }
    });
});




app.get("/sendSms", function(req, res) {
    console.log(req.query.number);
    console.log(req.query.number);
    client.messages.create({
        body: "Sender:Subway/Amount:"+req.query.amount,
        to: "+1"+req.query.number,
        from: "+14694164117"
    }, function(err, message) {
        console.log(message);
        res.end();
    });
});

var listPayment = {
    'count': '3',
    'start_index': '1'
};

app.get('/getList',function(req,res){
    paypal.payment.list(listPayment, function (error, payment) {
        if (error) {
            throw error;
        } else {
            console.log("List Payments Response");
            console.log(payment);
            res.send(payment);
            res.end();
        }
    });
});

var creditCardId = "CARD-2MW305457R2279623KYWLDAI";

app.get('/getCardInfo',function(req,res){
    paypal.creditCard.get(creditCardId, function (error, credit_card) {
        if (error) {
            throw error;
        } else {
            console.log("Retrieve Credit Card Response");
            console.log(JSON.stringify(credit_card));
            res.send(credit_card);
            res.end();
        }
    });
});