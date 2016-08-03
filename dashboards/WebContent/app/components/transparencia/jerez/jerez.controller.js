/**
 * 
 */


angular.module('jerezController',['dashboards']).controller('jerezController',['$scope','$routeParams','$http','$location','uiGmapGoogleMapApi',
	   function($scope,$routeParams,$http,$location,uiGmapGoogleMapApi){
	
	this.num_documentos=0;
	this.num_actividades=0;
	this.ejecucion_fisica = 0.0;
	this.ejecucion_financiera = 0.0;
	
	uiGmapGoogleMapApi.then(function() {
   		$scope.mapjerez = { center: { latitude: '14.092376', longitude: '-89.767197' }, 
   					   zoom: 15,
   					   options: {
   						   scrollwheel: false,
   						   mapTypeId: google.maps.MapTypeId.SATELLITE,
   						   disableDefaultUI: true
   					   },
   					   refresh: true
   					};
       });
	
	$http.post('/STransparenciaVentanas', { t: (new Date()).getTime() }).then(function(response){
	    if(response.data.success){
	    	this.num_documentos = response.data.results.documentos;
	    	this.num_actividades = response.data.results.actividades;
	    	this.ejecucion_financiera = response.data.results.ejecucion_financiera;
	    	this.ejecucion_fisica = response.data.results.ejecucion_fisica;
		}
 	}.bind(this), function errorCallback(response){
 		
 	});
	
	$scope.go = function ( path ) {
		  $location.path( path );
	};
	
}]);

