/**
 * 
 */

angular.module('paptn_ejecucionfinancieraController',['dashboards']).controller('paptn_ejecucionfinancieraController',['$scope','$routeParams','$http',
	   function($scope,$routeParams,$http){
			
			this.chart_colors = [ '#4682B4', '#F7464A', '#dcdcdc', '#97bbcd', '#ebb45c', '#949fb1', '#4d5360'];
			Chart.defaults.global.colours = [ '#4682B4', '#F7464A', '#dcdcdc', '#97bbcd', '#ebb45c', '#949fb1', '#4d5360'];
			
			var now = moment();
			this.mes = now.month()+1;
			this.tabla_entidades=[];
			this.tabla_entidades_totales=[0.0,0.0,0.0,0.0,0.0,0.0];
			this.tabla_ejes_totales=[0.0,0.0,0.0,0.0,0.0];
			this.tabla_ejes_ejes=[];
			
			this.lastupdate;
			
			
			this.chart_entidades=[];
			this.chart_entidades['labels']=["Institucional","PAPTN"];
			this.chart_entidades['data']=[];
			this.chart_entidades['series']=["Instituciones"];
			this.chart_entidades['options']={
					bezierCurve : false,
					datasetStrokeWidth : 6,
					pointDotRadius : 6,
					scaleLabel: function(label){return  numeral(label.value).format('$ 0,0')},
					tooltipTemplate: "<%if (label){%><%=label %>: <%}%><%= numeral(value).format('$ 0,0.00') %>",
					multiTooltipTemplate: "<%= numeral(value).format('$ 0,0.00') %>"
			};
			
			this.chart_eje_estrategico=[];
			this.chart_eje_estrategico['labels']=[];
			this.chart_eje_estrategico['data']=[];
			this.chart_eje_estrategico['series']=['Vigente','Ejecución'];
			this.chart_eje_estrategico['options']={
					bezierCurve : false,
					datasetStrokeWidth : 6,
					pointDotRadius : 6,
					scaleLabel: function(label){return  numeral(label.value).format('$ 0,0')},
					tooltipTemplate: "<%if (label){%><%=label %>: <%}%><%= numeral(value).format('$ 0,0.00') %>",
					multiTooltipTemplate: "<%= numeral(value).format('$ 0,0.00') %>"
			};
			
			
			this.chart_financiamiento=[];
			this.chart_financiamiento['labels']=['Origen Tributario', 'Préstamos Externos', 'Donaciones','Otras'];
			this.chart_financiamiento['data']=[1000,1000,2000,1474.4];
			this.chart_financiamiento['series']=['data'];
			this.chart_financiamiento['options']={
					bezierCurve : false,
					datasetStrokeWidth : 6,
					pointDotRadius : 6,
					scaleLabel: function(label){return  numeral(label.value).format('$ 0,0')},
					tooltipTemplate: "<%if (label){%><%=label %>: <%}%><%= numeral(value).format('$ 0,0.00') +' (' + numeral((circumference / 6.283)*100).format('0.00')+' %)' %>",
					multiTooltipTemplate: "<%= numeral(value).format('$ 0,0.00') %>"
			};
			
			$http.post('/SEntidadesTabla', { mes: this.mes, t: (new Date()).getTime() }).then(function(response){
				    if(response.data.success){
				    	this.tabla_entidades = response.data.entidades;
				    	for(var index in this.tabla_entidades){
				    		this.tabla_entidades_totales[0]+=this.tabla_entidades[index].gp_vigente;
				    		this.tabla_entidades_totales[1]+=this.tabla_entidades[index].gp_ejecucion;
				    		this.tabla_entidades_totales[2]+=this.tabla_entidades[index].gp_porcentaje;
				    		this.tabla_entidades_totales[3]+=this.tabla_entidades[index].paptn_vigente;
				    		this.tabla_entidades_totales[4]+=this.tabla_entidades[index].paptn_ejecucion;
				    		this.tabla_entidades_totales[5]+=this.tabla_entidades[index].paptn_porcentaje;
				    	}
				    	this.tabla_entidades_totales[2] = (this.tabla_entidades_totales[2]/this.tabla_entidades.length);
				    	this.tabla_entidades_totales[5] = (this.tabla_entidades_totales[5]/this.tabla_entidades.length);
				    	this.chart_entidades['data']=[[this.tabla_entidades_totales[1]/1000000,this.tabla_entidades_totales[4]/1000000]];
					}
			 	}.bind(this), function errorCallback(response){
			 		
			 	}
			);
			$http.post('/SEjesTabla', { mes: this.mes, t: (new Date()).getTime() }).then(function(response){
			    if(response.data.success){
			    	this.tabla_ejes = response.data.ejes;
			    	var count=[];
			    	var data_vigente=[];
			    	var data_ejecucion=[]
			    	for(var index in this.tabla_ejes){
			    		if(this.tabla_ejes[index].linea>0){
				    		this.tabla_ejes_totales[0]+=this.tabla_ejes[index].aprobado;
				    		this.tabla_ejes_totales[1]+=this.tabla_ejes[index].modificaciones;
				    		this.tabla_ejes_totales[2]+=this.tabla_ejes[index].vigente;
				    		this.tabla_ejes_totales[3]+=this.tabla_ejes[index].ejecucion;
				    		this.tabla_ejes_totales[4]+=this.tabla_ejes[index].porcentaje;
				    		if(this.tabla_ejes_ejes[this.tabla_ejes[index].eje]=== undefined)
				    			this.tabla_ejes_ejes[this.tabla_ejes[index].eje]=[0.0,0.0,0.0,0.0,0.0];
				    		this.tabla_ejes_ejes[this.tabla_ejes[index].eje][0]+=this.tabla_ejes[index].aprobado;
				    		this.tabla_ejes_ejes[this.tabla_ejes[index].eje][1]+=this.tabla_ejes[index].modificaciones;
				    		this.tabla_ejes_ejes[this.tabla_ejes[index].eje][2]+=this.tabla_ejes[index].vigente;
				    		this.tabla_ejes_ejes[this.tabla_ejes[index].eje][3]+=this.tabla_ejes[index].ejecucion;
				    		this.tabla_ejes_ejes[this.tabla_ejes[index].eje][4]+=this.tabla_ejes[index].porcentaje;
				    		count[this.tabla_ejes[index].eje] = count[this.tabla_ejes[index].eje] !== undefined ?  count[this.tabla_ejes[index].eje]+1 : 1;
			    		}
			    	}
			    	for(var index in this.tabla_ejes){
			    		if(this.tabla_ejes[index].linea==0){
			    			this.chart_eje_estrategico['labels'].push(this.tabla_ejes[index].eje_nombre_corto);
			    			data_vigente.push(this.tabla_ejes_ejes[this.tabla_ejes[index].eje][2]/1000000);
			    			data_ejecucion.push(this.tabla_ejes_ejes[this.tabla_ejes[index].eje][3]/1000000);
			    			this.tabla_ejes[index].aprobado = this.tabla_ejes_ejes[this.tabla_ejes[index].eje][0];
			    			this.tabla_ejes[index].modificaciones = this.tabla_ejes_ejes[this.tabla_ejes[index].eje][1];
			    			this.tabla_ejes[index].vigente = this.tabla_ejes_ejes[this.tabla_ejes[index].eje][2];
			    			this.tabla_ejes[index].ejecucion = this.tabla_ejes_ejes[this.tabla_ejes[index].eje][3];
			    			this.tabla_ejes[index].porcentaje = this.tabla_ejes_ejes[this.tabla_ejes[index].eje][4]/count[this.tabla_ejes[index].eje];
			    		}
				    }
				    	this.chart_eje_estrategico['data']=[data_vigente,data_ejecucion];
		    			this.tabla_ejes_totales[4] = (this.tabla_ejes_totales[4]/this.tabla_ejes.length);
				}
			 }.bind(this), function errorCallback(response){
			 		
			 });
			
			$http.post('/SEstructurasFinanciamiento', { t: (new Date()).getTime() }).then(function(response){
			    if(response.data.success){
			    	this.chart_financiamiento['data']=[response.data.tributarias/1000000,response.data.prestamos_externos/1000000,response.data.donaciones/1000000, response.data.otras/1000000];
			    	this.chart_financiamiento['total'] = (response.data.tributarias + response.data.prestamos_externos + response.data.donaciones + response.data.otras)/1000000;
			    }
			}.bind(this), function errorCallback(response){
		 		
			 });
			
			$http.post('/SLastupdate', { dashboard: 'paptn_ejecucionfinanciera' }).then(function(response){
			    if(response.data.success){
			    	this.lastupdate = response.data.lastupdate;
				}
			}.bind(this), function errorCallback(response){
		 		
			 });
		}
	]);