/**
 * 
 */

angular.module('mapsGeneralController',['dashboards']).controller('mapsGeneralController',['$scope','$http','uiGmapGoogleMapApi',
	   function($scope,$http, uiGmapGoogleMapApi){
			
			this.stroke= {
                    color: '#4863A0',
                    weight: 2,
                    opacity: 1
                };
                this.fill= {
                    color: '#4863A0',
                    opacity: 0.5
                };
                
            $scope.lastInfoWindow=-1;
                
            $scope.showInfoCircle = function(pos){
            	if($scope.lastInfoWindow>-1)
            		$scope.towns[$scope.lastInfoWindow].showinfowindow = false;
    			$scope.towns[pos].showinfowindow = true;
    			$scope.lastInfoWindow = pos;
    		}
                
			var data = { action: 'listTownsMap'};
			$http.post('/STown', data).then(function(response){
				    if(response.data.success){
				    	$scope.towns = response.data.towns;
				    	for(var i=0;i<$scope.towns.length; i++){
				    		var fun = Function("angular.element(document.getElementById('mainmap')).scope().showInfoCircle("+i+");");
				    		$scope.towns[i].showinfowindow = false;
				    		$scope.towns[i].events = {
				    				click : eval(fun)
				    		};
				    		$scope.towns[i].radius = Math.floor((Math.random() * 8000) + 1000);
				    		var ccolor = ($scope.towns[i].id % 3) +1;
				    		switch(ccolor){
				    			case 1: $scope.towns[i].stroke = { color: '#164890', opacity: 1.0 }; $scope.towns[i].fill = { color: '#164890', opacity: 0.2 }; break;
				    			case 2: $scope.towns[i].stroke = { color: '#535353', opacity: 1.0 }; $scope.towns[i].fill = { color: '#535353', opacity: 0.2 }; break;
				    			case 3: $scope.towns[i].stroke = { color: '#901619', opacity: 1.0 }; $scope.towns[i].fill = { color: '#901619', opacity: 0.2 }; break;
				    		}
				    	}
				    }
			 	}, function errorCallback(response){
			 		
			 	}
			);
			
			
	
			uiGmapGoogleMapApi.then(function() {
				$scope.map = { center: { latitude: 14.627934, longitude: -90.513216 }, 
							   zoom: 8,
							   options: {
								   streetViewControl: false,
								   scrollwheel: false
							   }
							};

		    });
	   }
]);