/**
 * 
 */


angular.module('jerezDocumentosController',['dashboards','smart-table','ngFileUpload']).controller('jerezDocumentosController',['$scope','$route','$routeParams','$http','$filter','Upload','$timeout',
	   function($scope,$route,$routeParams,$http,$filter,Upload,$timeout){
	
	this.lastupdate='';
	this.documentos=[];
	this.original_documentos=[];
	this.docFile = null;
	
	$http.post('/SLastupdate', { dashboard: 'ejecucionpresupuestaria', t: (new Date()).getTime() }).then(function(response){
		    if(response.data.success){
		    	this.lastupdate = response.data.lastupdate;
			}
		}.bind(this)
	);
	
	$http.post('/STransparenciaDocumentos', { action: 'getlist', t: (new Date()).getTime() }).then(function(response){
	    if(response.data.success){
	    	this.original_documentos = response.data.documentos;
	    	this.documentos = this.original_documentos.length> 0 ? this.original_documentos.slice(0) : [];
	    }
 	}.bind(this), function errorCallback(response){
 		
 	}
	);
	
	this.uploadFile = function(file) {
		file.upload = Upload.upload({
		    url: '/SSaveFile',
		    data: {id_actividad: -1, place:"jerez", file: file},
		  });
		
		 file.upload.then(function (response) {
		    $timeout(function () {
		      file.result = response.data;
		      $route.reload();
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

