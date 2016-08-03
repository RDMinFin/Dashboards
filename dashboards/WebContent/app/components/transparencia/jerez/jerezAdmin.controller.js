angular.module('jerezAdminController',['dashboards','smart-table','ngFileUpload']).controller('adminCtrl',['$scope','$rootScope','$routeParams','$http','$uibModal','uiGmapGoogleMapApi','Upload','$timeout',
 function($scope,$rootScope,$routeParams,$http,$uibModal,uiGmapGoogleMapApi,Upload,$timeout){
	this.id=-1;
	this.nombre="";
	this.descripcion="";
	this.fecha_inicio;
	this.fecha_fin;
	this.entidades="";
	this.porcentaje_ejecucion = 0.0;
    $rootScope.coord_lat;
    $rootScope.coord_long;
        
    this.entidad="";
    this.unidad_ejecutora="";
    this.programa="";
    this.subprograma="";
    this.proyecto="";
    this.actividad="";
    this.obra="";
 
    this.responsable_nombre="";
    this.responsable_correo="";
    this.responsable_telefono="";
    
    this.showloading = false;
    this.activityType = 1; //1 : presupuestaria - 2 : coordinacion 
    this.actividades_data = [];
    this.actividades_data_original=[];
    
    this.docFile = null;
            
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
		this.id =this.actividades_data[index].id;
		this.nombre=this.actividades_data[index].nombre;
		this.descripcion=this.actividades_data[index].descripcion;
		this.fecha_inicio =  moment(this.actividades_data[index].fecha_inicio,"DD/MM/YYYY HH:mm a").toDate();
		this.fecha_fin =  moment(this.actividades_data[index].fecha_fin,"DD/MM/YYYY HH:mm a").toDate();
		this.entidades=this.actividades_data[index].entidades;
		this.porcentaje_ejecucion = this.actividades_data[index].porcentaje_ejecucion;
		$rootScope.coord_lat=this.actividades_data[index].latitude;
		$rootScope.coord_long=this.actividades_data[index].longitude;	    
	    this.responsable_id=this.actividades_data[index].responsable_id;
	    this.responsable_nombre=this.actividades_data[index].responsable_nombre;
	    this.responsable_correo=this.actividades_data[index].responsable_correo;
	    this.responsable_telefono=this.actividades_data[index].responsable_telefono;
	    
	    if (this.actividades_data[index].entidad>0)
	    	this.activityType = 1;
    	else 
	    	this.activityType = 2;
   
	}
    
    this.save=function(){	     	
    	if (this.id <= 0) {
    	var tsFI= Math.floor(this.fecha_inicio / 1 );
	 	var tsFF= Math.floor(this.fecha_fin / 1 );
	 		
		 var data = { action:"create", nombre: this.nombre, descripcion: this.descripcion, fecha_inicio:tsFI, fecha_fin:tsFF,
				 	entidades: this.entidades, porcentaje_ejecucion:this.porcentaje_ejecucion, coord_lat:$rootScope.coord_lat, coord_long:$rootScope.coord_long,
				 	responsable_nombre:this.responsable_nombre, responsable_correo:this.responsable_correo, responsable_telefono:this.responsable_telefono,programa:this.programa,subprograma:this.subprograma};
		 $http.post('/SSaveActividad', data).then(function(response){
			    if(response.data.success)
			    	this.loadList();
			    else
			    	window.alert("error");
		 	}.bind(this), function errorCallback(response){
		 		$scope.showerror = true;
		 	}
		 );		
		 this.clear();
    	}else{
    		this.update();
    	}    	
	 }
	 
	 this.update=function(){
		 var tsFI= Math.floor(this.fecha_inicio / 1 );
		 var tsFF= Math.floor(this.fecha_fin / 1 );
		 var data = { action:"update", id:this.id, nombre:this.nombre, descripcion:this.descripcion, coord_lat:$rootScope.coord_lat, coord_long:$rootScope.coord_long,
				 		porcentaje_ejecucion:this.porcentaje_ejecucion, fecha_inicio:tsFI, fecha_fin:tsFF, entidades:this.entidades,
				 		responsable_nombre:this.responsable_nombre, responsable_correo:this.responsable_correo, responsable_telefono:this.responsable_telefono, responsable_id:this.responsable_id};
		 $http.post('/SSaveActividad', data).then(function(response){
			    if(response.data.success){
			    	this.loadList();
			    }else
			    	window.alert("error");
		 	}.bind(this), function errorCallback(response){
		 		$scope.showerror = true;
		 	}
		 );
	 }
	 
	 this.erase=function(){
		 var data = {action:"delete",id:this.id,responsable_id:this.responsable_id}
		 $http.post('/SSaveActividad', data).then(function(response){
			    if(response.data.success){
			    	this.loadList();
			    	this.clear();
			    }else
			    	window.alert("error");
		 	}.bind(this), function errorCallback(response){
		 		$scope.showerror = true;
		 	}
		 );
	 }
	 
	 this.clear=function(){
		this.id=-1;
		this.nombre="";
		this.descripcion="";
		this.fecha_inicio=null;
		this.fecha_fin=null;
		this.entidades="";
		this.porcentaje_ejecucion = 0.0;
		$rootScope.coord_lat=null;
		$rootScope.coord_long=null;	        
	    this.entidad="";
	    this.unidad_ejecutora="";
	    this.programa="";
	    this.subprograma="";
	    this.proyecto="";
	    this.actividad="";
	    this.obra="";
	    this.responsable_nombre="";
	    this.responsable_correo="";
	    this.responsable_telefono="";
	    this.docFile = null
	    this.activityType=2;
	 }
	 
	 
	 this.showMap = function () {
	 var modalInstance = $uibModal.open({
	      animation: true,
	      scope:$rootScope,
	      templateUrl: 'map.html',
	      controller: 'mapCtrl'
	    
	    });
	 };
	
	 this.uploadPic = function(file) {
		file.upload = Upload.upload({
		    url: '/SSaveFile',
		    data: {id_actividad: this.id, place:"jerez", file: file},
		  });
		
		 file.upload.then(function (response) {
		    $timeout(function () {
		      file.result = response.data;
		    });
		  }, function (response) {
		    if (response.status > 0)
		      this.errorMsg = response.status + ': ' + response.data;
		  }, function (evt) {
		    // Math.min is to fix IE which reports 200% sometimes
		    file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
		  });
	 }
	 
}]);

angular.module('jerezAdminController').controller('mapCtrl',[ '$scope','$rootScope','$uibModalInstance','$timeout', 'uiGmapGoogleMapApi',
function ($scope,$rootScope, $uibModalInstance,$timeout, uiGmapGoogleMapApi) {

$scope.refreshMap = true;

uiGmapGoogleMapApi.then(function() {
	$scope.map = { center: { latitude: '14.091376', longitude:'-89.766197' }, 
				   zoom: 15,
				   height: 400,
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
				        latitude:$rootScope.coord_lat,
				        longitude:$rootScope.coord_long,
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
				          $scope.$evalAsync();
				          $scope.$apply();
				        }					   
				   }
				};
  });



  $scope.ok = function () {
    $uibModalInstance.dismiss('cancel');
    
  };

}]);