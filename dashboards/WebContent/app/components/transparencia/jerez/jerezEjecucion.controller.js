/**
 * 
 */

angular.module('jerezEjecucionController',['dashboards','smart-table']).controller('jerezEjecucionController',['$scope','$routeParams','$http','$document','$timeout',
	   function($scope,$routeParams,$http, $document, $timeout){			
			this.lastupdate = '';
			
			$http.post('/SLastupdate', { dashboard: 'ejecucionpresupuestaria', t: (new Date()).getTime() }).then(function(response){
			    if(response.data.success){
			    	this.lastupdate = response.data.lastupdate;
				}
			}.bind(this)
			);
			
			var current_year = moment().year();		
			this.entidad_ejecucion_financiera=0;
			this.entidad_ejecucion_fisica=0;
			this.entidad_vigente=0;
			this.entidad_ejecutado=0;
			this.entidad_level = 1;
			this.entidad_nombre="";
			this.entidad=null;
			this.entidad_unidad_ejecutora_nombre="";
			this.entidad_unidad_ejecutora=null;
			this.entidad_programa_nombre="";
			this.entidad_programa=94;
			this.entidad_subprograma_nombre="";
			this.entidad_subprograma=2;
			this.entidad_proyecto_nombre="";
			this.entidad=proyecto=null;
			this.entidad_actividad_nombre="";
			this.entidad_actividad=null;
			this.entidad_renglon_nombre="";
			this.entidad_renglon=null;
			
			this.programa = this.entidad_programa;
			this.programa_subprograma = this.entidad_subprograma;
			this.programa_proyecto=null;
			this.programa_actividad=null;
			this.programa_renglon=null;
			
			this.programa_ejecucion_financiera=0;
			this.programa_ejecucion_fisica=0;
			this.programa_vigente=0;
			this.programa_ejecutado=0;
			this.programa_level = 0;
			this.programa_nombre="";
			this.programa_subprograma_nombre="";
			this.programa_proyecto_nombre="";
			this.programa_actividad_nombre="";
						
			this.entidad_ejecucion_data=[];
			this.entidad_ejecucion_data_original=[];
			this.entidad_ejecucion_totales=[0.0, 0.0, 0.0, 0.0, 0.0, 0.0];
			
			this.programa_ejecucion_data=[];
			this.programa_ejecucion_data_original=[];
			this.programa_ejecucion_totales=[0.0, 0.0, 0.0, 0.0, 0.0, 0.0];
			
			this.gauge1_loaded = false;
						
			this.entidad_clickRow = function(codigo, nombre){
				switch(this.entidad_level){
					case 1: this.entidad = codigo; this.entidad_nombre = nombre;
							this.entidad_unidad_ejecutora=null; this.entidad_proyecto = null; this.entidad_actividad=null; this.entidad_renglon=null;
							break;
					case 2: this.entidad_unidad_ejecutora = codigo; this.entidad_unidad_ejecutora_nombre = nombre; 
							this.entidad_proyecto = null; this.entidad_actividad=null; this.entidad_renglon=null;
							break;
					case 3: this.entidad_proyecto=codigo; this.entidad_proyecto_nombre = nombre;
							this.entidad_actividad=null;this.entidad_renglon=null;
							break;
					case 4: this.entidad_actividad=codigo; this.entidad_actividad_nombre = nombre; 
							this.entidad_renglon=null;
							break;	
					case 5: this.entidad_renglon=codigo; this.entidad_renglon_nombre=nombre;
							break;
				}
				this.entidad_level = this.entidad_level<5 ? this.entidad_level+1 : this.entidad_level;
				this.entidad_goLevel(this.entidad_level);
			};
			
			this.entidad_goLevel=function(entidad_level){			
				this.entidad_level = entidad_level;
				switch(this.entidad_level){
					case 1: this.entidad=null; this.entidad_unidad_ejecutora=null; this.entidad_proyecto=null; this.entidad_actividad=null; this.entidad_renglon=null; break;
					case 2: this.entidad_unidad_ejecutora=null; this.entidad_proyecto=null; this.entidad_actividad=null; this.entidad_renglon=null; break;
					case 3: this.entidad_proyecto=null; this.entidad_actividad=null; this.entidad_renglon=null; break;
					case 4: this.entidad_actividad=null; this.entidad_renglon=null; break;
					case 5: this.entidad_renglon=null;
				}
				var data = { tipo: "entidad", level:this.entidad_level, ejercicio:current_year, entidad: this.entidad, unidad_ejecutora: this.entidad_unidad_ejecutora, programa:this.entidad_programa, subprograma:this.entidad_subprograma,
							proyecto:this.entidad_proyecto, actividad:this.entidad_actividad, renglon:this.entidad_renglon};				
    			this.entidad_showloading = true;
				$http.post('/SEjecucionFF', data).then(function(response){
	    			if(response.data.success){
	    				this.entidad_ejecucion_data_original = response.data.datos;
	    				this.entidad_ejecucion_data = this.entidad_ejecucion_data_original.length > 0 ? this.entidad_ejecucion_data_original : [];
	    				this.entidad_ejecucion_totales[0] = 0;
    					this.entidad_ejecucion_totales[1] = 0;
    					this.entidad_ejecucion_totales[2] = 0;
    					this.entidad_ejecucion_totales[3] = 0;
    					this.entidad_ejecucion_totales[4] = 0;
    					this.entidad_ejecucion_totales[5] = 0;
	    				for (i=0; i<this.entidad_ejecucion_data.length; i++){
	    					this.entidad_ejecucion_totales[0] += this.entidad_ejecucion_data[i].vigente;
	    					this.entidad_ejecucion_totales[1] += this.entidad_ejecucion_data[i].ejecutado;
	    					this.entidad_ejecucion_totales[3] += this.entidad_ejecucion_data[i].meta;
	    					this.entidad_ejecucion_totales[4] += this.entidad_ejecucion_data[i].meta_avanzado;
	    				}
	    				this.entidad_ejecucion_totales[2] = this.entidad_ejecucion_totales[0]>0?(this.entidad_ejecucion_totales[1]/this.entidad_ejecucion_totales[0]*100):0.0;
    					this.entidad_ejecucion_totales[5] = this.entidad_ejecucion_totales[3]>0?(this.entidad_ejecucion_totales[4]/this.entidad_ejecucion_totales[3]*100):0.0;

	    				if (this.entidad_level==1){
	    					this.entidad_vigente=this.entidad_ejecucion_totales[0];
	    					this.entidad_ejecutado=this.entidad_ejecucion_totales[1];
	    					this.entidad_ejecucion_financiera = this.entidad_ejecucion_totales[2];
	    					this.entidad_ejecucion_fisica = this.entidad_ejecucion_totales[5];
					    	if (!this.gauge1_loaded){
								this.FunCall("gauge", this.entidad_ejecucion_financiera, "");
								this.FunCall("gauge1", this.entidad_ejecucion_fisica, "");
								this.gauge1_loaded=true;
							}	   
	    				}
	    			}
	    			
	    			this.entidad_showloading = false;		
	    		}.bind(this), function errorCallback(response){	 		
			 	});		
			};
			
			//////
			
			this.programa_clickRow = function(codigo, nombre){
				switch(this.programa_level){
					case 1: this.programa = codigo; this.programa_nombre = nombre;
							this.programa_proyecto = null; this.programa_actividad=null; this.programa_renglon=null;
							break;
					case 2: this.programa_subprograma = codigo; this.programa_subprograma_nombre = nombre; 
							this.programa_proyecto=null; this.programa_actividad=null; this.programa_renglon=null;
							break;
					case 3: this.programa_proyecto=codigo; this.programa_proyecto_nombre = nombre;
							this.programa_actividad=null;this.programa_renglon=null;
							break;
					case 4: this.programa_actividad=codigo; this.programa_actividad_nombre = nombre; 
							this.programa_renglon=null;
							break;	
					case 5: this.programa_renglon=codigo; this.programa_renglon_nombre=nombre;
							break;
				}
				this.programa_level = this.programa_level<5 ? this.programa_level+1 : this.programa_level;
				this.programa_goLevel(this.programa_level);
			};
			
			this.programa_goLevel=function(programa_level){
				
				this.programa_level = programa_level;
				switch(this.programa_level){
					case 1: this.programa_proyecto=null; this.programa_actividad=null; this.programa_renglon=null; break;
					case 2: this.programa_proyecto=null; this.programa_actividad=null; this.programa_renglon=null; break;
					case 3: this.programa_proyecto=null; this.programa_actividad=null; this.programa_renglon=null; break;
					case 4: this.programa_actividad=null; this.programa_renglon=null; break;
					case 5: this.programa_renglon=null; break;
				}
				var data = { tipo: "programa", level:programa_level, entidad: null, unidad_ejecutora: null, programa:this.programa, subprograma:this.programa_subprograma,
							proyecto:this.programa_proyecto, actividad:this.programa_actividad, renglon:this.programa_renglon};				
    			this.programa_showloading = true;
				$http.post('/SEjecucionFF', data).then(function(response){
	    			if(response.data.success){
	    				this.programa_ejecucion_data_original = response.data.datos;
	    				this.programa_ejecucion_data = this.programa_ejecucion_data_original.length > 0 ? this.programa_ejecucion_data_original : [];
	    				this.programa_ejecucion_totales[0] = 0;
    					this.programa_ejecucion_totales[1] = 0;
    					this.programa_ejecucion_totales[2] = 0;
    					this.programa_ejecucion_totales[3] = 0;
    					this.programa_ejecucion_totales[4] = 0;
    					this.programa_ejecucion_totales[5] = 0;
    					
    					for (i=0; i<this.programa_ejecucion_data.length; i++){
	    					this.programa_ejecucion_totales[0] += this.programa_ejecucion_data[i].vigente;
	    					this.programa_ejecucion_totales[1] += this.programa_ejecucion_data[i].ejecutado;
	    					this.programa_ejecucion_totales[3] += this.programa_ejecucion_data[i].meta;
	    					this.programa_ejecucion_totales[4] += this.programa_ejecucion_data[i].meta_avanzado;
	    				}
	    				this.programa_ejecucion_totales[2] = this.programa_ejecucion_totales[0]>0?(this.programa_ejecucion_totales[1]/this.programa_ejecucion_totales[0]*100):0.0;
    					this.programa_ejecucion_totales[5] = this.programa_ejecucion_totales[3]>0?(this.programa_ejecucion_totales[4]/this.programa_ejecucion_totales[3]*100):0.0;

	    			}
	    			this.programa_showloading = false;		
	    		}.bind(this), function errorCallback(response){			 		
			 	});					
			};
			
			this.entidad_goLevel(1);
			this.programa_goLevel(1);

			this.FunCall=function(gauge, val, title){
				var g = new JustGage({
				    id: gauge,
				    value: val,
				    titleFontSize: 16,
				    titleFontColor:"#000000",
				    min: 0,
				    max: 100,
				    title: title,
				    customSectors : [{"lo":0,"hi":33,"color":"#B0171F"},
				                     {"lo":34,"hi":66,"color":"	#FFFF00"},
				                     {"lo":67,"hi":100,"color":"#00EE00"}],
				    levelColorsGradient: true
				  });
			};
			
			
			
			
		}
	]);
