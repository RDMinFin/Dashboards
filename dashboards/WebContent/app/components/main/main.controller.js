/**
 * 
 */
var app = angular.module('dashboards',['ngRoute','ui.bootstrap','chart.js', 'loadOnDemand','ngAnimate', 'ngTouch', 
                                       'ui.grid', 'ui.grid.treeView', 'ui.grid.selection','ui.grid.moveColumns', 'ui.grid.resizeColumns', 'ui.grid.saveState','ui.grid.pinning',
                                       'uiGmapgoogle-maps','ng.deviceDetector','ui.grid.grouping']);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
	   $locationProvider.hashPrefix('!');
	   //$locationProvider.html5Mode(true);
	   $routeProvider
	   		/*.when('/main',{
        		templateUrl : '',
        		resolve:{
        			main: function main(){
        				window.location.href = '/main.jsp';
        			}
        		}
        	})*/
            .when('/dashboards/ejecucionpresupuestaria/:reset_grid?',{
            	template: '<div load-on-demand="\'ejecucionpresupuestariaController\'" class="all_page"></div>'
            })
            .when('/dashboards/ejecucionrenglon/:reset_grid?',{
            	template: '<div load-on-demand="\'ejecucionrenglonController\'" class="all_page"></div>'
            })
            .when('/dashboards/copep/:reset_grid?',{
            	template: '<div load-on-demand="\'copepController\'" class="all_page"></div>'
            })
            .when('/dashboards/copeprenglon/:reset_grid?',{
            	template: '<div load-on-demand="\'copeprenglonController\'" class="all_page"></div>'
            })
            .when('/dashboards/ejecucionprograma/:reset_grid?',{
            	template: '<div load-on-demand="\'ejecucionprogramaController\'" class="all_page"></div>'
            })
            .when('/dashboards/ejecucionprogramaUE/:reset_grid?',{
            	template: '<div load-on-demand="\'ejecucionprogramaUEController\'" class="all_page"></div>'
            })
            .when('/paptn/ejecucionfinanciera',{
            	template: '<div load-on-demand="\'paptn_ejecucionfinancieraController\'" class="all_page"></div>'
            })
            .when('/maps/geograficogasto',{
            	template: '<div load-on-demand="\'mapsGastoGeograficoController\'" class="all_page"></div>'
            })
            .when('/dashboards/proyecciongasto',{
            	template: '<div load-on-demand="\'proyecciongastoController\'" class="all_page"></div>'
            })
            .when('/transparencia/jerez',{
            	template: '<div load-on-demand="\'jerezController\'" class="all_page"></div>'
            })
            .when('/transparencia/jerez/mapa',{
            	template: '<div load-on-demand="\'jerezMapaController\'" class="all_page"></div>'
            })
            .when('/transparencia/jerez/actividades',{
            	template: '<div load-on-demand="\'jerezActividadesController\'" class="all_page"></div>'
            })
            .when('/transparencia/jerez/ejecucion',{
            	template: '<div load-on-demand="\'jerezEjecucionController\'" class="all_page"></div>'
            })
            .when('/transparencia/jerez/admin',{
            	template: '<div load-on-demand="\'jerezAdminController\'" class="all_page"></div>'
            })
            .when('/transparencia/jerez/documentos',{
            	template: '<div load-on-demand="\'jerezDocumentosController\'" class="all_page"></div>'
            })
            .when('/transparencia/jerez/compras',{
            	template: '<div load-on-demand="\'jerezComprasController\'" class="all_page"></div>'
            })
            .when('/presidenciales/metas',{
            	template: '<div load-on-demand="\'metasController\'" class="all_page"></div>'
            })
            /*.when('/salir',{
            	templateUrl : '<div></div>',
            	resolve:{
            		logout: function logout($http){
            			$http.post('/SLogout', '').then(function(response){
	        				    if(response.data.success)
	        				    	window.location.href = '/login.jsp';
	        			 	}, function errorCallback(response){
	        			 		
	        			 	}
	        			 );
            			return true;
            		}
            	}
            });*/
    }]);

app.config(['$loadOnDemandProvider', function ($loadOnDemandProvider) {
	   var modules = [
	       {
	    	   name: 'ejecucionpresupuestariaController',
	    	   script: '/app/components/ejecucionpresupuestaria/ejecucionpresupuestaria.controller.js',
	    	   template: '/app/components/ejecucionpresupuestaria/ejecucionpresupuestaria.jsp'
	       },
	       {
	    	   name: 'ejecucionrenglonController',
	    	   script: '/app/components/ejecucionrenglon/ejecucionrenglon.controller.js',
	    	   template: '/app/components/ejecucionrenglon/ejecucionrenglon.jsp'
	       },
	       {
	    	   name: 'copepController',
	    	   script: '/app/components/copep/copep.controller.js',
	    	   template: '/app/components/copep/copep.jsp'
	       },
	       {
	    	   name: 'copeprenglonController',
	    	   script: '/app/components/copeprenglon/copeprenglon.controller.js',
	    	   template: '/app/components/copeprenglon/copeprenglon.jsp'
	       },
	       {
	    	   name: 'ejecucionprogramaController',
	    	   script: '/app/components/ejecucionprograma/ejecucionprograma.controller.js',
	    	   template: '/app/components/ejecucionprograma/ejecucionprograma.jsp'
	       },
	       {
	    	   name: 'ejecucionprogramaUEController',
	    	   script: '/app/components/ejecucionprogramaUE/ejecucionprogramaUE.controller.js',
	    	   template: '/app/components/ejecucionprogramaUE/ejecucionprogramaUE.jsp'
	       },
	       {
	           name: 'paptn_ejecucionfinancieraController',     
	           script: '/app/components/paptn/ejecucionfinanciera/ejecucionfinanciera.controller.js',
	           template: '/app/components/paptn/ejecucionfinanciera/ejecucionfinanciera.jsp'
	       },
	       {
	    	   name: 'mapsGastoGeograficoController',
	    	   script: '/app/components/maps/gastogeografico_h/gastogeografico_h.js',
	    	   template: '/app/components/maps/gastogeografico_h/gastogeografico_h.jsp'
	       },
	       {
	    	   name: 'proyecciongastoController',
	    	   script: '/app/components/proyecciongasto/proyecciongasto.controller.js',
	    	   template: '/app/components/proyecciongasto/proyecciongasto.jsp'
	       },
	       {
	    	   name: 'jerezController',     
	           script: '/app/components/transparencia/jerez/jerez.controller.js',
	           template: '/app/components/transparencia/jerez/jerez.jsp'
	       },
	       {
	    	   name: 'jerezMapaController',     
	           script: '/app/components/transparencia/jerez/jerezMapa.controller.js',
	           template: '/app/components/transparencia/jerez/jerezMapa.jsp'
	       },
	       {
	    	   name: 'jerezActividadesController',     
	           script: '/app/components/transparencia/jerez/jerezActividades.controller.js',
	           template: '/app/components/transparencia/jerez/jerezActividades.jsp'
	       },
	       {
	    	   name: 'jerezEjecucionController',     
	           script: '/app/components/transparencia/jerez/jerezEjecucion.controller.js',
	           template: '/app/components/transparencia/jerez/jerezEjecucion.jsp'
	       },
	       {
	    	   name: 'jerezAdminController',     
	           script: '/app/components/transparencia/jerez/jerezAdmin.controller.js',
	           template: '/app/components/transparencia/jerez/jerezAdmin.jsp'
	       },
	       {
	    	   name: 'jerezDocumentosController',     
	           script: '/app/components/transparencia/jerez/jerezDocumentos.controller.js',
	           template: '/app/components/transparencia/jerez/jerezDocumentos.jsp'
	       },
	       {
	    	   name: 'jerezComprasController',     
	           script: '/app/components/transparencia/jerez/jerezCompras.controller.js',
	           template: '/app/components/transparencia/jerez/jerezCompras.jsp'
	       },
	       {
	    	   name: 'metasController',     
	           script: '/app/components/presidenciales/metas/metas.controller.js',
	           template: '/app/components/presidenciales/metas/metas.jsp'
	       }
	   ];
	   $loadOnDemandProvider.config(modules);
}]);

app.config(['uiGmapGoogleMapApiProvider',function(uiGmapGoogleMapApiProvider) {
    uiGmapGoogleMapApiProvider.configure({
        key: 'AIzaSyBPq-t4dJ1GV1kdtXoVZfG7PtfEAHrhr00',
        v: '3.23', //defaults to latest 3.X anyhow
        libraries: 'weather,geometry,visualization'
    });
}]);

app.controller('MainController',['$scope','$document','deviceDetector','$rootScope','$location','$window',
   function($scope,$document,deviceDetector,$rootScope,$location,$window){
	$scope.lastscroll = 0;
	$scope.hidebar = false;
	
	numeral.language('es', numeral_language);
	
	$document.bind('scroll', function(){
		if($document[0].body.scrollTop > 15){
			if ($scope.lastscroll>$document[0].body.scrollTop) { //Scroll to Top
		        $scope.hidebar = false;
		    } else if($document[0].body.scrollTop>15) { //Scroll to Bottom
		        $scope.hidebar = true;
		    }
			$scope.$apply();
		}
		$scope.lastscroll = $document[0].body.scrollTop;
	});
	
	$scope.hideBarFromMenu=function(){
		$scope.hidebar = true;
		document.getElementById("title").scrollIntoView()
	}
	
	$scope.device = deviceDetector;
	
	$rootScope.$on('$routeChangeSuccess', function (event, current, previous) {
		$window.ga('create', 'UA-74443600-1', 'auto');
    	$window.ga('send', 'pageview', $location.path());
    });
}]);



