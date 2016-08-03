/**
 * 
 */


angular.module('jerezMapaController',['dashboards']).controller('jerezMapaController',['$scope','$routeParams','$http','uiGmapGoogleMapApi',
	   function($scope,$routeParams,$http,uiGmapGoogleMapApi){
	
	this.lastupdate='';
	this.actividades='';
	
	$http.post('/SLastupdate', { dashboard: 'ejecucionpresupuestaria', t: (new Date()).getTime() }).then(function(response){
		    if(response.data.success){
		    	this.lastupdate = response.data.lastupdate;
			}
		}.bind(this)
	);
	
	uiGmapGoogleMapApi.then(function() {
   		$scope.mapjerez = { center: { latitude: '14.092376', longitude: '-89.767197' }, 
   					   zoom: 15,
   					   options: {
						   streetViewControl: false,
						   scrollwheel: false,
						   mapTypeId: google.maps.MapTypeId.SATELLITE
					   },
					   refresh: true
   					};
       });
	
	$http.post('/SSaveActividad', { action: 'getlist', t: (new Date()).getTime() }).then(function(response){
	    if(response.data.success){
	    	this.actividades = response.data.actividades;
	    }
 	}.bind(this), function errorCallback(response){
 		
 	}
	);
	
}]);

