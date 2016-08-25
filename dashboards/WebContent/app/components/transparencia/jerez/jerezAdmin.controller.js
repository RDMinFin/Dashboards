angular.module('jerezAdminController',['dashboards','smart-table','ngFileUpload','ui.bootstrap']);
angular.module('jerezAdminController').controller('adminCtrl', function($log,$scope,$rootScope,$routeParams,$http,$uibModal,$route){
	
    this.showloading = false;  
    this.actividades_data = [];
    this.actividades_data_original=[];
  
	this.loadList=function(){
		this.showloading=true;
		$http.post('/SSaveActividad', { action: 'getlist', t: (new Date()).getTime() }).then(function(response){
		    if(response.data.success){
		    	this.actividades_data_original = response.data.actividades;
		    	this.actividades_data = this.actividades_data_original.length> 0 ? this.actividades_data_original.slice(0) : [];
		    }
		    this.showloading=false;
	 	}.bind(this), function errorCallback(response){	
	 	}
		);
	}
	
	this.select=function(index){	
		$rootScope.id =this.actividades_data[index].id;
		$rootScope.nombre=this.actividades_data[index].nombre;
		$rootScope.descripcion=this.actividades_data[index].descripcion;
		$rootScope.fecha_inicio =  moment(this.actividades_data[index].fecha_inicio,"DD/MM/YYYY HH:mm a").toDate();
		$rootScope.fecha_fin =  moment(this.actividades_data[index].fecha_fin,"DD/MM/YYYY HH:mm a").toDate();
		$rootScope.entidades=this.actividades_data[index].entidades;
		$rootScope.porcentaje_ejecucion = this.actividades_data[index].porcentaje_ejecucion;
		$rootScope.coord_lat=this.actividades_data[index].latitude;
		$rootScope.coord_long=this.actividades_data[index].longitude;	
		$rootScope.coord = "(" + $rootScope.coord_lat +", "+ $rootScope.coord_long + ")";
		$rootScope.responsable_id=this.actividades_data[index].responsable_id;
		$rootScope.responsable_nombre=this.actividades_data[index].responsable_nombre;
		$rootScope.responsable_correo=this.actividades_data[index].responsable_correo;
		$rootScope.responsable_telefono=this.actividades_data[index].responsable_telefono;
		$rootScope.edit = false;
		 
		 var modalInstance = $uibModal.open({
		      animation: true,
		      keyboard:false,
		      backdrop:'static',
		      scope:$scope,
		      templateUrl: 'edit.html',
		      controller: 'editActivity',
		 });
		 
		 modalInstance.result.then(function() {
			 $route.reload();
		}, function() {

		})['finally'](function(){
			modalInstance = undefined  // <--- This fixes
			$scope.render=false;
		});
 
	}
    
    this.addActivity = function () {
    	$rootScope.id = -1;
		 var modalInstance = $uibModal.open({
		      animation: true,
		      keyboard:false,
		      backdrop:'static',
		      scope:$scope,
		      templateUrl: 'edit.html',
		      controller: 'editActivity',
		 });
		 
		 modalInstance.result.then(function () {
			 $route.reload();
		    }, function () {
		    	
		    });
	 };	 
	 
	 this.addCompras = function () {
	    	$rootScope.id = -1;
			 var modalInstance = $uibModal.open({
			      animation: true,
			      keyboard:false,
			      backdrop:'static',
			      scope:$scope,
			      templateUrl: 'editCompras.html',
			      controller: 'editCompras',
			 });
			 
			 modalInstance.result.then(function () {
				 $route.reload();
			    }, function () {
			    	
			    });
		 };	 
});


angular.module('jerezAdminController')
.controller('editActivity', function ($log, $scope, $rootScope,  $http, $window,  $uibModalInstance,  $timeout,  uiGmapGoogleMapApi,uiGmapIsReady,Upload) {
	if ($rootScope.id<=0){
		$rootScope.id=-1; 
		$rootScope.nombre="";
		$rootScope.descripcion="";
		$rootScope.fecha_inicio=null;
		$rootScope.fecha_fin=null;
		$rootScope.entidades="";
		$rootScope.porcentaje_ejecucion = 0.0;
		$rootScope.coord_lat=null;
		$rootScope.coord_long=null;	
		$rootScope.coord="";
		$rootScope.entidad="";
		$rootScope.unidad_ejecutora="";
		$rootScope.programa="";
		$rootScope.subprograma="";
		$rootScope.proyecto="";
		$rootScope.actividad="";
		$rootScope.obra="";
		$rootScope.responsable_nombre="";
		$rootScope.responsable_correo="";
		$rootScope.responsable_telefono="";
	}
	$rootScope.docFile = null
	$scope.activeTab=0;
	
	$scope.cancel = function () {
		$rootScope.render=false;
	    $uibModalInstance.dismiss('cancel');
	};
	
	$http.post('/STransparenciaDocumentos', { action: 'getlist', id:$rootScope.id, t: (new Date()).getTime() }).then(function(response){
	    if(response.data.success){
	    	$scope.original_documentos = response.data.documentos;
	    	$scope.documentos = $scope.original_documentos.length> 0 ? $scope.original_documentos.slice(0) : [];
	    }
 	}.bind(this), function errorCallback(response){
 		
 	}
	);
	
	 $scope.deleteDoc=function(idDoc){
		 var data = {action:"delete",iddoc:idDoc}
		 $http.post('/STransparenciaDocumentos', data).then(function(response){
			    if(response.data.success){
			    	$http.post('/STransparenciaDocumentos', { action: 'getlist', id:$rootScope.id, t: (new Date()).getTime() }).then(function(response){
			    	    if(response.data.success){
			    	    	$scope.original_documentos = response.data.documentos;
			    	    	$scope.documentos = $scope.original_documentos.length> 0 ? $scope.original_documentos.slice(0) : [];
			    	    }
			     	}.bind(this), function errorCallback(response){
			     		
			     	}
			    	);			    
			    }else
			    	window.alert("error");
		 	}.bind(this), function errorCallback(response){
		 		$scope.showerror = true;
		 	}
		) 
	 };
	
	$scope.save=function(){	  
		if ($rootScope.id>0)
			$scope.update()
		else{
	    	var tsFI= Math.floor($rootScope.fecha_inicio / 1 );
		 	var tsFF= Math.floor($rootScope.fecha_fin / 1 );
		 	var data = { action:"create", nombre: $rootScope.nombre, descripcion: $rootScope.descripcion, fecha_inicio:tsFI, fecha_fin:tsFF,
					 	entidades: $rootScope.entidades, porcentaje_ejecucion:$rootScope.porcentaje_ejecucion, coord_lat:$rootScope.coord_lat, coord_long:$rootScope.coord_long,
					 	responsable_nombre:$rootScope.responsable_nombre, responsable_correo:$rootScope.responsable_correo, responsable_telefono:$rootScope.responsable_telefono,
					 	programa:$rootScope.programa,subprograma:$rootScope.subprograma};
			$http.post('/SSaveActividad', data).then(function(response){
				    if(response.data.success){
						$rootScope.render=false;
				    	$uibModalInstance.close();
				    }
				    else
				    	window.alert("error");
			 	}.bind(this), function errorCallback(response){
			 		$scope.showerror = true;
			 	}
			 );		
		}
	 }.bind($scope);
		
	$scope.update=function(){
		 var tsFI= Math.floor($rootScope.fecha_inicio / 1 );
		 var tsFF= Math.floor($rootScope.fecha_fin / 1 );
		 var data = { action:"update", id:$rootScope.id, nombre:$rootScope.nombre, descripcion:$rootScope.descripcion, coord_lat:$rootScope.coord_lat, coord_long:$rootScope.coord_long,
				 		porcentaje_ejecucion:$rootScope.porcentaje_ejecucion, fecha_inicio:tsFI, fecha_fin:tsFF, entidades:$rootScope.entidades,
				 		responsable_nombre:$rootScope.responsable_nombre, responsable_correo:$rootScope.responsable_correo, responsable_telefono:$rootScope.responsable_telefono, responsable_id:$rootScope.responsable_id};
		 $http.post('/SSaveActividad', data).then(function(response){
			    if(response.data.success){
					$rootScope.render=false;
			    	$uibModalInstance.close();
			    }else
			    	window.alert("error");
		 	}.bind(this), function errorCallback(response){
		 		$scope.showerror = true;
		 	}
		 );
	 }.bind($scope);
	 
	 $scope.erase=function(){
		 var data = {action:"delete",id:$rootScope.id,responsable_id:$rootScope.responsable_id}
		 $http.post('/SSaveActividad', data).then(function(response){
			    if(response.data.success){
					$rootScope.render=false;
			    	$uibModalInstance.close();
			    }else
			    	window.alert("error");
		 	}.bind(this), function errorCallback(response){
		 		$scope.showerror = true;
		 	}
		 );
	 }.bind($scope);

 
	 $scope.uploadPic = function(file) {
			file.upload = Upload.upload({
			    url: '/SSaveFile',
			    data: {id_actividad: this.id, place:"jerez", file: file},
			  });
			
			 file.upload.then(function (response) {
			    $timeout(function () {
			      file.result = response.data;
			      $rootScope.docFile=null;
			      $http.post('/STransparenciaDocumentos', { action: 'getlist', id:$rootScope.id, t: (new Date()).getTime() }).then(function(response){
			    	    if(response.data.success){
			    	    	$scope.original_documentos = response.data.documentos;
			    	    	$scope.documentos = $scope.original_documentos.length> 0 ? $scope.original_documentos.slice(0) : [];
			    	    }
			     	}.bind(this), function errorCallback(response){
			     		
			     	}
			    	);	
			    });
			  }, function (response) {
			    if (response.status > 0)
			      this.errorMsg = response.status + ': ' + response.data;
			  }, function (evt) {
			    file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
			  });
		 };	 
	
	uiGmapGoogleMapApi.then(function() {
		$scope.map = { center: { latitude: '14.091376', longitude:'-89.766197' }, 
					   zoom: 15,					   
					   options: {
						   streetViewControl: false,
						   scrollwheel: true,
						   mapTypeId: google.maps.MapTypeId.SATELLITE
					   },
					   refresh: true,
					   clickedMarker: {
					        id: 0,
					        options:{
					        },
					        latitude:$scope.coord_lat,
					        longitude:$scope.coord_long,
					   },
					   events: {
						   click: function (mapModel, eventName, originalEventArgs) {
					          var e = originalEventArgs[0];
					          var lat = e.latLng.lat(),
					              lon = e.latLng.lng();
					          $scope.map.clickedMarker = {
					            id: 0,
					            options: {
					            },
					            latitude: lat,
					            longitude: lon
					          };
					          $rootScope.coord_lat=lat;
					          $rootScope.coord_long=lon;
					          $rootScope.coord = "(" + $rootScope.coord_lat +", "+ $rootScope.coord_long + ")";
					          $scope.activeTab=0;
					          $scope.$digest();
					        }					   
					   }
					   
					};
	  });
	
	uiGmapIsReady.promise().then(function(instances) {
		 maps = instances[0].map;
		 center = maps.getCenter();
		 maps.setCenter(center);
		 maps.setZoom(15);
		 google.maps.event.trigger(maps,'resize');
     })
     

	$scope.changeTab = function(val){
		$scope.activeTab = val;
	    $rootScope.render=true;

	};
	
	$scope.mapTabSelect = function(){
	    $rootScope.render=true;
	}
});

angular.module('jerezAdminController')
.controller('editCompras', function ($log, $scope, $http, $window,  $uibModalInstance,  $timeout) {
	
	$scope.tipoCompra;
	$scope.idCompra;
	
	$scope.clearError=function(){
		$scope.error = false;
		$scope.errorMessage="";
	}
	
	$scope.cancel = function () {
	    $uibModalInstance.dismiss('cancel');
	};
	
	$http.post('/STransparenciaCompras', { action: 'getlist', t: (new Date()).getTime() }).then(function(response){
	    if(response.data.success){
	    	$scope.original_compras = response.data.compras;
	    	$scope.compras = $scope.original_compras.length> 0 ? $scope.original_compras.slice(0) : [];
	    }
 	}.bind($scope), function errorCallback(response){
 		
 	}
	);
	
	$scope.getCompra=function(){
		$http.post('/STransparenciaCompras', { action: 'getlist', t: (new Date()).getTime() }).then(function(response){
		    if(response.data.success){
		    	$scope.original_compras = response.data.compras;
		    	$scope.compras = $scope.original_compras.length> 0 ? $scope.original_compras.slice(0) : [];
		    }
	 	}.bind($scope), function errorCallback(response){
	 		
	 	}
		);
	}
	
	$scope.addCompra=function(){
		isValid = false;
		if (this.tipoCompra=="NOG"){
			if( !isNaN(parseFloat(this.idCompra)) && isFinite(this.idCompra) )
				isValid=true;
			else{
				isValid=false;
				$scope.error=true;
				$scope.errorMessage=" Formato invalido para NOG";
			}
		}
		
		for (i=0; i<=$scope.compras.length; i++ ){
			isValid=true;
			if ($scope.compras[i].id == this.idCompra){
				isValid=false;
				$scope.error=true;
				$scope.errorMessage=" Proceso de compra ya existe";
				i=$scope.compras.length;
			}
		}
		
		if (isValid){
			$http.post('/STransparenciaCompras', { action: 'add', tipoCompra:this.tipoCompra, idCompra:this.idCompra, t: (new Date()).getTime() }).then(function(response){
			    if(response.data.success){
			    	this.getCompra();
			    	this.tipoCompra=null;
			    	this.idCompra=null;
			    }
		 	}.bind($scope), function errorCallback(response){
		 	}
			);
		}
		
	};
	
	$scope.deleteCompra=function(tipo,id){
		$http.post('/STransparenciaCompras', { action: 'delete', tipoCompra:tipo, idCompra:id, t: (new Date()).getTime() }).then(function(response){
		    if(response.data.success){
		    	this.getCompra();
		    }
	 	}.bind($scope), function errorCallback(response){
	 		
	 	}
		);
	};
	
});
