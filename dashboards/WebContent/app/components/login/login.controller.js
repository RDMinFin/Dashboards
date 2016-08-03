/**
 * 
 */
 
 var app=angular.module('dashboards',[]);
 
 app.controller('LoginController',['$scope','$http',function($scope,$http){
	 this.username = "";
	 this.password = "";
	 $scope.showerror = false;
	 this.login=function(){
		 if(this.username!='' && this.password!=''){
			 var data = { username: this.username, password: this.password};
			 $http.post('/SLogin', data).then(function(response){
				    if(response.data.success)
				    	window.location.href = '/main.jsp';
				    else
				    	$scope.showerror = true;
			 	}, function errorCallback(response){
			 		$scope.showerror = true;
			 	}
			 );
		 }
	 }
 }]);