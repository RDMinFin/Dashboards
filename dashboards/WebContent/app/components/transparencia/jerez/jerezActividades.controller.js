/**
 * 
 */


angular.module('jerezActividadesController',['dashboards','angular-timeline','angular-scroll-animate','ngSanitize']).controller('jerezActividadesController',['$scope','$routeParams','$http','uiGridConstants','$document','$timeout','$sce','$uibModal','uiGmapGoogleMapApi',
	   function($scope,$routeParams,$http,uiGridConstants, $document, $timeout,$sce,$uibModal,uiGmapGoogleMapApi){
			
			this.lastupdate = '';
			this.actividad_seleccionada=-1;
			
			$http.post('/SLastupdate', { dashboard: 'ejecucionpresupuestaria', t: (new Date()).getTime() }).then(function(response){
				    if(response.data.success){
				    	this.lastupdate = response.data.lastupdate;
					}
			}.bind(this)
			);
			
			
			this.trustAsHtml = function(string) {
		        return $sce.trustAsHtml(string);
		    };
		
			this.side='';
			this.actividad = '';
			this.activiades = [];
			
			$http.post('/SSaveActividad', { action: 'getlist', t: (new Date()).getTime() }).then(function(response){
			    if(response.data.success){
			    	this.actividades = response.data.actividades;
			    }
		 	}.bind(this), function errorCallback(response){
		 		
		 	}
			);
			
			// optional: not mandatory (uses angular-scroll-animate)
			this.animateElementIn = function($el) {
				$el.removeClass('timeline-hidden');
				$el.addClass('bounce-in');
			};

			// optional: not mandatory (uses angular-scroll-animate)
			this.animateElementOut = function($el) {
				$el.addClass('timeline-hidden');
				$el.removeClass('bounce-in');
			};
			
			this.selectActividad=function(index){
				this.side = this.side='left';
				this.actividad = this.actividades[index];
				this.actividad_seleccionada = index;
			}
			
			this.open = function (posicionlat, posicionlong) {
				$scope.geoposicionlat = posicionlat;
				$scope.geoposicionlong = posicionlong;
				
			    var modalInstance = $uibModal.open({
			      animation: true,
			      templateUrl: 'map.html',
			      controller: 'mapCtrl',
			      resolve: {
			        glat: function(){
			        	return $scope.geoposicionlat;
			        },
			        glong: function(){
			        	return $scope.geoposicionlong;
			        }
			      }
			    
			    });
			  };
			  
			  this.cerrarActividad=function(){
				  this.side='';
				  this.actividad_seleccionada=-1;
			  }
			
		}
	]);

angular.module('jerezActividadesController').controller('mapCtrl',[ '$scope','$uibModalInstance','$timeout', 'uiGmapGoogleMapApi','glat','glong',
                                                         function ($scope, $uibModalInstance,$timeout, uiGmapGoogleMapApi, glat, glong) {
                                                     	$scope.geoposicionlat = glat;
                                                     	$scope.geoposicionlong = glong;
                                                     	
                                                     	$scope.refreshMap = true;
                                                     	
                                                     	uiGmapGoogleMapApi.then(function() {
                                                     		$scope.map = { center: { latitude: $scope.geoposicionlat, longitude: $scope.geoposicionlong }, 
                                                     					   zoom: 15,
                                                     					   height: 400,
                                                     					   options: {
                                                     						   streetViewControl: false,
                                                     						   scrollwheel: true,
                                                     						  mapTypeId: google.maps.MapTypeId.SATELLITE
                                                     					   },
                                                     					   refresh: true
                                                     					};
                                                         });
                                                     	
                                                     	
                                                     	
                                                     	  $scope.ok = function () {
                                                     	    $uibModalInstance.dismiss('cancel');
                                                     	    
                                                     	  };

                                                     	}]);