/**
 * 
 */
	
	angular.module('proyecciongastoController',['dashboards']).controller('proyecciongastoController',['$scope','$http','$interval', 'uiGridTreeViewConstants',
	   'uiGridConstants','i18nService', 'uiGmapGoogleMapApi',
	   function($scope,$http, $interval, uiGridTreeViewConstants, uiGridConstants, i18nService, uiGmapGoogleMapApi){
		i18nService.setCurrentLang('es');
		
		this.options= {
				bezierCurve : false,
				datasetFill : false,
				datasetStrokeWidth : 6,
				pointDotRadius : 6,
				scaleLabel: function(label){return  numeral(label.value).format('$ 0,0.00')},
				tooltipTemplate: "<%if (label){%><%=label %>: <%}%><%= numeral(value).format('$ 0,0.00') %>",
				multiTooltipTemplate: "<%= numeral(value).format('$ 0,0.00') %>"
		//		fillColor: ['rgba(151,187,205,0.2)', 'rgba(247,70,74,0.2)', 'rgba(70,191,189,0.2)', 'rgba(235,180,92,0.2)', 'rgba(148,159,177,0.2)', 'rgba(77,83,96,1)', 'rgba(220,220,220,0.2)'],
		//		strokeColor: ['rgba(151,187,205,1)', 'rgba(247,70,74,1)', 'rgba(70,191,189,1)', 'rgba(235,180,92,1)', 'rgba(148,159,177,1)', 'rgba(77,83,96,1)', 'rgba(220,220,220,1)']
		};
		this.labels = ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"];
		this.series = ['2013', '2014', '2015', '2016'];
		this.data = [];
		this.original_data = [  ///Gasto
		    [3535.02,
		     4308.23,
		     4534.47,
		     5438.23,
		     4805.90,
		     4791.38,
		     6372.26,
		     4527.87,
		     4539.51,
		     4476.47,
		     5693.13,
		     7511.39],
		     [3646.42,
		      3951.66,
		      4996.37,
		      5656.06,
		      5445.98,
		      5274.82,
		      6344.60,
		      5788.10,
		      4632.00,
		      3803.06,
		      5364.42,
		      8259.11],
		      [4120.65,
		       5164.39,
		       5976.87,
		       5802.54,
		       5145.16,
		       4869.40,
		       5877.66,
		       5394.78,
		       4513.84,
		       4814.30,
		       4208.75,
		       6611.96],
		       [4264.14,
		        4703.61]
		  ];
		angular.copy(this.original_data, this.data);
		this.colors = ['rgba(151,187,205,1)', 'rgba(220,220,220,1)','rgba(247,70,74,1)', 'rgba(70,191,189,1)', 'rgba(235,180,92,1)', 'rgba(148,159,177,1)', 'rgba(77,83,96,1)'];
		this.show = [true, true, true, true, true];
		
		
		/*Entidades*/
		this.entidad_series = ['Ministerio de Gobernación','Ministerio de Salud'];
		this.entidad_original_data = [
		    [35, 49, 90, 41, 36, 75, 50, 25, 75, 86, 20, 10],
		    [18, 28, 40, 29, 46, 57, 95, 59, 20, 30, 48, 35]
		]; 
		this.entidad_data=[];
		angular.copy(this.entidad_original_data, this.entidad_data);
		this.entidad_show = [true, true];
		
		/*Renglones*/
		this.renglon_series = ['011','181'];
		this.renglon_original_data = [
		    [65, 59, 80, 81, 56, 55, 40, 35, 45, 56, 30, 20],
		    [18, 28, 40, 29, 46, 57, 95, 59, 20, 30, 48, 35]
		]; 
		this.renglon_data=[];
		angular.copy(this.renglon_original_data, this.renglon_data);
		this.renglon_show = [true, true];
		
		/*this.onClick = function (points, evt) {
			console.log(points, evt);
		};*/
		
		this.seriesShow=function(pos){
			var output = [];
		    angular.copy(this.original_data, output);
		    for(var i=0; i<output.length; i++){
		        if (this.show[i] === false){
		            for(var j=0; j<output[i].length; j++){
		                output[i][j] = null
		            }
		        }
		    }
		    this.data = output;
		}
		
		this.entidad_seriesShow=function(pos){
			var output = [];
		    angular.copy(this.entidad_original_data, output);
		    for(var i=0; i<output.length; i++){
		        if (this.entidad_show[i] === false){
		            for(var j=0; j<output[i].length; j++){
		                output[i][j] = null
		            }
		        }
		    }
		    this.entidad_data = output;
		}
		
		this.renglon_seriesShow=function(pos){
			var output = [];
		    angular.copy(this.renglon_original_data, output);
		    for(var i=0; i<output.length; i++){
		        if (this.renglon_show[i] === false){
		            for(var j=0; j<output[i].length; j++){
		                output[i][j] = null
		            }
		        }
		    }
		    this.renglon_data = output;
		}
		
		/****** Grids ************/
		
		$scope.rowDblClick_clase = function( row) {
		    alert(row.entity.clase); 
		  };
		  
		  function rowTemplate_clase() {
			    return '<div ng-dblclick="grid.appScope.rowDblClick_clase(row)" >' +
			                 '  <div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }"  ui-grid-cell></div>' +
			                 '</div>';
			  }
		
		this.renglones_gridOptions = {
			    enableSorting: true,
			    enableFiltering: true,
			    enableRowSelection: true,
			    selectionRowHeaderWidth: 35,
			    multiSelect: true,
			    modifierKeysToMultiSelect: false,
			    noUnselect: false,
			    showColumnFooter: true,
			    enableRowHeaderSelection: false,
			    showGridFooter:true,
			    //showTreeExpandNoChildren: true,
			    showColumnFooter: true,
			    //rowTemplate: rowTemplate_clase(),
			    columnDefs: [
			      { name: 'clase', width: '15%', displayName: 'Clase', cellClass: 'grid-align-right', pinnedLeft: true  },
			      { name: 'nombre', width: '43%', displayName: 'Nombre' },
			      { name: 'monto', width: '300', cellFilter: 'currency:"Q " : 2', displayName: 'Monto', 
			    	  cellClass: 'grid-align-right', aggregationType: uiGridConstants.aggregationTypes.sum, footerCellFilter: 'currency:"Q " : 2',
			    	  footerCellClass: 'grid-align-right', aggregationHideLabel: true }
			    ],
			    onRegisterApi: function( gridApi ) {
			      $scope.gridApi = gridApi;
			      $scope.gridApi.treeBase.on.rowExpanded($scope, function(row) {
			        
			      });
			    }
			  };
		
		this.grid_renglones_data = [
				{	"clase" : 0,
					"nombre" : "Servicios Personales",
					"monto": 10000
					 
				},
				{
					"clase" : 1,
					"nombre": "Personal en cargos fijos",
					"monto": 7000
				},
				{
					"clase" : 11,
					"nombre": "Personal Permanente",
					"monto": 6500
				},
				{	"clase" : 1,
					"nombre" : "Servicios No Personales",
					"monto": 5000
				},
				{
					"clase" : 11,
					"nombre": "Servicios Básicos y Otros Complementarios",
					"monto": 4500
				},
				{
					"clase" : 111,
					"nombre": "Engergía Eléctrica",
					"monto": 4300
				}
		];
		
		this.grid_renglones_data[0].$$treeLevel = 0;
		this.grid_renglones_data[1].$$treeLevel = 1;
		this.grid_renglones_data[3].$$treeLevel = 0;
		this.grid_renglones_data[4].$$treeLevel = 1;
		
		this.renglones_gridOptions.data = this.grid_renglones_data;
		
		$scope.rowDblClick_entidad = function( row) {
		    alert(row.entity.entidad); 
		  };
		  
		  function rowTemplate_entidad() {
			    return '<div ng-dblclick="grid.appScope.rowDblClick_entidad(row)" >' +
			                 '  <div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }"  ui-grid-cell></div>' +
			                 '</div>';
			  }
		
		this.entidades_gridOptions = {
			    enableSorting: true,
			    enableFiltering: true,
			    enableRowSelection: true,
			    selectionRowHeaderWidth: 35,
			    showColumnFooter: true,
			    showGridFooter:true,
			    rowTemplate: rowTemplate_entidad(),
			    columnDefs: [
			      { name: 'entidad', width: '15%', displayName: 'Entidad', cellClass: 'grid-align-right' },
			      { name: 'descripcion', width: '45%', displayName: 'Descripción' },
			      { name: 'monto', width: '30%', cellFilter: 'currency:"Q " : 2', displayName: 'Monto', cellClass: 'grid-align-right', 
			    	  aggregationType: uiGridConstants.aggregationTypes.sum, footerCellFilter: 'currency:"Q " : 2',
			    	  footerCellClass: 'grid-align-right', aggregationHideLabel: true
			      }
			    ],
			    onRegisterApi: function( gridApi ) {
			      $scope.grid2Api = gridApi;
			    }
			  };
		
		this.entidades_data = [
		        {
		        	"entidad": "018",
		        	"descripcion" : "Ministerio de Gobernación",
		        	"monto": 1200000
		        },{
		        	"entidad": "119",
		        	"descripcion": "Ministerio de Salud",
		        	"monto": 5400000
		        }
		];
		
		this.entidades_gridOptions.data = this.entidades_data;
		
		/**************  Google Maps ******************************/
		

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
			    	}
			    }
		 	}, function errorCallback(response){
		 		
		 	}
		);
		
		uiGmapGoogleMapApi.then(function() {
			$scope.map = { center: { latitude: 14.627934, longitude: -90.513216 }, 
						   zoom: 7,
						   height: 400,
						   circle_radius: 500,
						   circle_stroke: {
						            color: '#4863A0',
						            weight: 2,
						            opacity: 1
						    },
						    circle_fill: {
									color: '#4863A0',
						            opacity: 0.5
						    },
						   options: {
							   streetViewControl: false,
							   scrollwheel: true
						   }
						};
	    });
		
	}]);
	
	
	
