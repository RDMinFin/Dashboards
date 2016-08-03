/**
 * 
 */

angular.module('mapsGastoGeograficoController',['dashboards']).controller('mapsGastoGeograficoController',['$scope','$http','uiGmapGoogleMapApi',
	   function($scope,$http, uiGmapGoogleMapApi){
			
			var pointsData=[];
			var heatLayer;
            function GetDataHeatLayer(hLayer) {
			    this.heatLayer = hLayer;
			    
			    var data = { action: 'gastogeografico'};
				$http.post('/SGastoGeografico', data).then(function(response){
					    if(response.data.success){
					    	var points=[];
					    	var geograficos = response.data.geograficos;
					    	for(var i=0;i<geograficos.length; i++){
					    		for(var j=0; j<geograficos[i].puntos; j++)
					    			points.push(new google.maps.LatLng(geograficos[i].latitud, geograficos[i].longitud));
					    	}
					    	this.pointsData = new google.maps.MVCArray(points);
					    	this.heatLayer.setData(this.pointsData);
					    }
				 	}.bind(this), function errorCallback(response){
				 		
				 	}
				);
			};
			
			
	
			uiGmapGoogleMapApi.then(function() {
				$scope.map = { center: { latitude: 14.627934, longitude: -90.513216 }, 
							   zoom: 8,
							   options: {
								   streetViewControl: false,
								   scrollwheel: false
							   },
							   heatLayerCallback: function (layer) {
					                //set the heat layers backend data
					                var mockHeatLayer = new GetDataHeatLayer(layer);
					                },
					           showHeat: true
							};

		    });
	   }
]);