var app=angular.module('myApp',[]);
app.controller('myCtrl',function($scope,$http) {
	$scope.gifyhide=false;
	$scope.openModal=function(){
		$('#myModal').modal('show');
	}
	$scope.phoneNumber='';
	var sent=false;
	$scope.sendSms=function(){
		var phoneno = /^\d{10}$/;  
		if($scope.phoneNumber.match(phoneno))  
		{  
			$http.get('http://localhost:1337/sendSms?number='+$scope.phoneNumber+"&amount="+$scope.billAmt)
			.success(function(res){
				alertify.success('SMS Sent Successfully');
				sent=true;
				$scope.gifyhide=true;
				$http.get('http://localhost:1337/createPayment')
					.success(function(res){});
			});
		}  
		else  
		{  
			alertify.error('Invalid Phone Number');
		}  
	}
	var count=0;

	setInterval(function(){ 
			if(sent){
				$http.get('http://localhost:1337/checkPayment')
			.success(function(res){
				console.log(res);
				count++;
				if(count==10){
					if(res.a=='1')
						swal("Good job!", "Your payment is successful!", "success");
					else
						swal("Oops!", "Your payment failed!", "error");
					sent=false;
					$scope.gifyhide=false;
				}
			})
		}
	}, 1000);

	$scope.refreshFunction=function(){
		$scope.gifyhide=true;
		setTimeout(function(){ 

			$http.get('http://localhost:1337/checkPayment')
			.success(function(res){
					if(res.a=='1')
						swal("Good job!", "Your payment is successful!", "success");
					else
						swal("Oops!", "Your payment failed!", "error");
					$scope.gifyhide=false;
				
			})


		}, 2000);

	}



});