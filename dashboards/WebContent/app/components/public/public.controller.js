/**
 * 
 */
var app = angular.module('dashboards',['ngRoute','ui.bootstrap','chart.js', 'loadOnDemand','ngAnimate', 'ngTouch', 
                                       'ui.grid', 'ui.grid.treeView', 'ui.grid.selection','ui.grid.moveColumns', 'ui.grid.resizeColumns', 'ui.grid.saveState','ui.grid.pinning',
                                       'uiGmapgoogle-maps']);

app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
	   $locationProvider.hashPrefix('!');
	   //$locationProvider.html5Mode(true);
	   $routeProvider
	   		.when('/paptn/ejecucionfinanciera',{
            	template: '<div load-on-demand="\'paptn_ejecucionfinancieraController\'" class="all_page"></div>'
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
            .when('/transparencia/jerez/documentos',{
            	template: '<div load-on-demand="\'jerezDocumentosController\'" class="all_page"></div>'
            });
    }]);

app.config(['$loadOnDemandProvider', function ($loadOnDemandProvider) {
	   var modules = [
	       {
	           name: 'paptn_ejecucionfinancieraController',     
	           script: '/app/components/paptn/ejecucionfinanciera/ejecucionfinanciera.controller.js',
	           template: '/app/components/paptn/ejecucionfinanciera/ejecucionfinanciera.jsp'
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
	    	   name: 'jerezDocumentosController',     
	           script: '/app/components/transparencia/jerez/jerezDocumentos.controller.js',
	           template: '/app/components/transparencia/jerez/jerezDocumentos.jsp'
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

app.controller('publicController',['$scope','$document','$rootScope','$location','$window',
   function($scope,$document,$rootScope,$location,$window){
	$scope.lastscroll = 0;
	$scope.hidebar = false;
	
	numeral.language('es', numeral_language);
	
	$rootScope.$on('$routeChangeSuccess', function (event, current, previous) {
		$window.ga('create', 'UA-74443600-1', 'auto');
    	$window.ga('send', 'pageview', $location.path());
    });
}]);

