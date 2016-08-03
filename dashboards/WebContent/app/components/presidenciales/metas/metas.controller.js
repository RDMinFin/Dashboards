/**
 * 
 */

angular.module('metasController',['dashboards']).controller('metasController',['$scope','$routeParams','$http',
	   function($scope,$routeParams,$http){
	
			this.chart_colors = [ '#4682B4', '#F7464A', ,'#00ff00', '#dcdcdc' , '#ebb45c', '#949fb1', '#4d5360'];
			Chart.defaults.global.colours = [ '#4682B4', '#F7464A', '#00ff00', '#dcdcdc', '#ebb45c', '#949fb1', '#4d5360'];
			
			this.show_rows=[false];
			this.lastupdate;
			this.metas_entidades;
			this.original_metas_entidades;
			this.metas_metas;
			this.original_metas_metas;
			
			this.totales_entidades=[0.0,0.0,0.0,0.0,0.0,0.0,0.0];
			this.totales_metas=[0.0,0.0,0.0,0.0,0.0,0.0,0.0];
			this.indice_alineamiento_total;
			
			
			this.chart_ejecucion_metas=[];
			this.chart_ejecucion_metas['labels']=['Ejecución Financiera'];
			this.chart_ejecucion_metas['data']=[[0.0],[0.0],[0.0]];
			this.chart_ejecucion_metas['series']=['Ejecución Metas','Vigente de Metas','Vigente de Entidad'];
			this.chart_ejecucion_metas['options']={
					bezierCurve : false,
					datasetStrokeWidth : 6,
					pointDotRadius : 6,
					scaleLabel: function(label){return  numeral(label.value).format('$ 0,0')},
					tooltipTemplate: "<%if (label){%><%=label %>: <%}%><%= numeral(value).format('$ 0,0.00') %>",
					multiTooltipTemplate: "<%= numeral(value).format('$ 0,0.00') %>"
			};
			
			this.labels_month = ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"];
			
			this.chart_metasff=[];
			this.chart_metasff['labels']=[''];
			this.chart_metasff['data']=[];
			this.chart_metasff['series']=['Ejecución Física','Ejecución Financiera'];
			this.chart_metasff['options']={
					bezierCurve : false,
					datasetStrokeWidth : 6,
					pointDotRadius : 6,
					scaleLabel: function(label){return  numeral(label.value).format('0.00%')},
					tooltipTemplate: "<%if (label){%><%=label %>: <%}%><%= numeral(value).format('0.00%') %>",
					multiTooltipTemplate: "<%= numeral(value).format('0.00%') %>"
			};
			
			$http.post('/SLastupdate', { dashboard: 'ejecucionpresupuestaria' }).then(function(response){
			    if(response.data.success){
			    	this.lastupdate = response.data.lastupdate;
				}
			}.bind(this), function errorCallback(response){
		 		
			 });
			
			
			$http.post('/SMetas', { action: 'getMetasEntidades', mes: 7 }).then(function(response){
			    if(response.data.success){
			    	this.original_metas_entidades = response.data.metas;
			    	this.metas_entidades = this.original_metas_entidades.length> 0 ? this.original_metas_entidades.slice(0) : [];
			    	for(var i=0; i<response.data.metas.length; i++){
			    		this.totales_entidades[0]+=response.data.metas[i].asignado;
			    		this.totales_entidades[1]+=response.data.metas[i].modificaciones;
			    		this.totales_entidades[2]+=response.data.metas[i].vigente;
			    		this.totales_entidades[3]+=response.data.metas[i].gasto;
			    		this.totales_entidades[4]+=response.data.metas[i].avance;
			    		this.totales_entidades[5]+=response.data.metas[i].meta;
			    		this.totales_entidades[6]+=response.data.metas[i].vigente_entidad;
			    	}
			    	this.chart_ejecucion_metas['data']=[[this.totales_entidades[3]/1000000],[this.totales_entidades[2]/1000000],[this.totales_entidades[6]/1000000]];
			    	var indice = (this.totales_entidades[2]/this.totales_entidades[6])*100;
			    	if(indice<50)
			    		this.indice_alineamiento_total = 4;
					else if(indice<75)
						this.indice_alineamiento_total = 2;
					else if(indice<100)
						this.indice_alineamiento_total = 3;
					else
						this.indice_alineamiento_total = 1;
				}
			}.bind(this), function errorCallback(response){
		 		
			 });
			
			$http.post('/SMetas', { action: 'getMetasMetas', mes: 7 }).then(function(response){
			    if(response.data.success){
			    	this.original_metas_metas = response.data.metas;
			    	this.metas_metas = this.original_metas_metas.length> 0 ? this.original_metas_metas.slice(0) : [];
			    	for(var i=0; i<response.data.metas.length; i++){
			    		this.totales_metas[0]+=response.data.metas[i].asignado;
			    		this.totales_metas[1]+=response.data.metas[i].modificaciones;
			    		this.totales_metas[2]+=response.data.metas[i].vigente;
			    		this.totales_metas[3]+=response.data.metas[i].gasto;
			    		this.totales_metas[4]+=response.data.metas[i].avance;
			    		this.totales_metas[5]+=response.data.metas[i].meta;
			    	}
				}
			}.bind(this), function errorCallback(response){
		 		
			 });
			
			$http.post('/SMetas', { action: 'chartMetasFF', mes: 7 }).then(function(response){
			    if(response.data.success){
			    	this.chart_metasff['labels'] = this.labels_month.slice(0,7);
			    	this.chart_metasff['data']=[];
			    	var fisica =[];
			    	var financiera=[];
			    	for(var i=0; i<response.data.puntos.length; i++){
			    		fisica[response.data.puntos[i].mes-1]=response.data.puntos[i].fisica;
			    		financiera[response.data.puntos[i].mes-1]=response.data.puntos[i].financiera;
			    	}
			    	this.chart_metasff['data'].push(fisica);
			    	this.chart_metasff['data'].push(financiera);
				}
			}.bind(this), function errorCallback(response){
		 		
			 });
			
		}
	]);