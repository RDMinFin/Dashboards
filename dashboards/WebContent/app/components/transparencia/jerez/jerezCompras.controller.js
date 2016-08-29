

angular.module('jerezComprasController',['dashboards','smart-table']);
angular.module('jerezComprasController').controller('ComprasCtrl', function($scope,$http){
	
	this.compras=[];
	this.original_compras=[];
	this.showloading=true;

	
	$http.post('/STransparenciaCompras', { action: 'getlist', t: (new Date()).getTime() }).then(function(response){
	    if(response.data.success){
	    	this.original_compras = response.data.compras;
	    	this.compras = this.original_compras.length> 0 ? this.original_compras.slice(0) : [];
	    	this.showloading=false;
	    }
 	}.bind(this), function errorCallback(response){
    	this.showloading=false;
 	}
	);
	
});

