<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <div ng-controller="jerezEjecucionController as control" class="maincontainer" id="title" class="all_page">
	<h3>Tablero de Seguimiento a Estados de Excepción - Jerez, Jutiapa</h3>
	<br/>
	<div><h4>Indicadores de Ejecución</h4></div>
	<div class="row panel panel-default" style="margin: 10px 0px 20px 0px;">
				<div class="col-sm-6">
					<div style="display: table; width: 100%;">
						<div style="display: table-cell; width: 50%; vertical-align: middle; text-align: center; font-size: 14px; font-weight: bold;">Financiera</div>
						<div class="col-sm-6"><div id="gauge" style="height:150px; width:170px; margin: 0 auto;"></div></div>
					</div>
				</div>
				<div class="col-sm-6">
					<div style="display: table; width: 100%;">
						<div style="display: table-cell; width: 50%; vertical-align: middle; text-align: center; font-size: 14px; font-weight: bold;">Física</div>
						<div class="col-sm-6"><div id="gauge1" style="height:150px; width:170px; margin: 0 auto;"></div></div>
					</div>
				</div>
	</div>
	<div><h4>Presupuesto</h4></div>
	<div class="row">
		<div>
			<div style="padding: 15px;">
				<div class="panel panel-default">
					<div>
						<div>
							<a href class="btn btn-default no-border" ng-click="control.entidad_goLevel(1, false)" ng-disabled="control.showloading"><b>Entidades Involucradas</b></a> 
							<span ng-hide="control.entidad_level<2">/ 
								<a href class="btn btn-default no-border" ng-click="control.entidad_goLevel(2, false)" ng-disabled="control.showloading">{{ control.entidad_nombre }}</a>
							</span>
							<span ng-hide="control.entidad_level<3">/ 
								<a href class="btn btn-default no-border" ng-click="control.entidad_goLevel(3, false)" ng-disabled="control.showloading">{{ control.entidad_unidad_ejecutora_nombre }}</a>
							</span>
							<span ng-hide="control.entidad_level<4">/ 
								<a href class="btn btn-default no-border" ng-click="control.entidad_goLevel(4, false)" ng-disabled="control.showloading">{{ control.entidad_proyecto_nombre }}</a>
							</span>
							<span ng-hide="control.entidad_level<5">/ 
								<a href class="btn btn-default no-border" ng-click="control.entidad_goLevel(5, false)" ng-disabled="control.showloading">{{ control.entidad_actividad_nombre }}</a>
							</span>
							<span ng-hide="control.entidad_level<6">/ &nbsp;{{ control.entidad_renglon_nombre }}</span>
						</div>
					</div> 
			  		<div style="position: relative;">
						<table class="table table-striped" st-table="control.entidad_ejecucion_data" 
							   st-safe-src="control.entidad_ejecucion_data_original" >
							 <thead>
								<tr>
									<th></th>
									<th st-sort="codigo">Código</th>
									<th st-sort="nombre">Nombre</th>
									<th st-sort="vigente" style="text-align: right; ">Vigente</th>
									<th st-sort="ejecutado" style="text-align: right;">Ejecutado</th>
									<th st-sort="eje_financiera" style="text-align: right;">% Ejecución Financiera</th>
									<th ng-hide="control.entidad_level>4" st-sort="eje_fisica" style="text-align: right; ">% Ejecucion Física</th>
								</tr>
							</thead>
							<tfoot>
							    <tr style="font-weight: bold;">
							    	<td/>
							    	<td/>
							    	<td class="text-right"><strong>Totales</strong></td>
									<td class="text-right"><strong>{{ control.entidad_ejecucion_totales[0] | currency:"Q&nbsp;":2  }}</strong></td>
									<td class="text-right"><strong>{{ control.entidad_ejecucion_totales[1] | currency:"Q&nbsp;":2 }}</strong></td>
									<td class="text-right"><strong>{{ control.entidad_ejecucion_totales[2] | number:2 }}&nbsp;%</strong></td>
									<td ng-hide="control.entidad_level>4" class="text-right"><strong>{{ control.entidad_ejecucion_totales[5] | number:2 }}&nbsp;%</strong></td>
								</tr>
							</tfoot>
							<tbody>
								<tr ng-repeat="data in control.entidad_ejecucion_data">
									<td><button ng-show="control.entidad_level<5" class="btn btn-primary" ng-click="control.entidad_clickRow(data.codigo,data.nombre)"></button></td>
									<td class="text-nowrap"><strong>{{data.codigo}}</strong></td>
									<td title="{{data.nombre}}" style="max-width: 350px; overflow: hidden;" class="text-nowrap"><strong>{{data.nombre}}</strong></td>
									<td class="text-right">{{ data.vigente | currency:"Q&nbsp;":2 }}</td>
									<td class="text-right">{{ data.ejecutado | currency:"Q&nbsp;":2 }}</td>
									<td class="text-right">{{ data.ejecucion_financiera | number:2}}&nbsp;%</td>
									<td ng-hide="control.entidad_level>4" class="text-right">{{ data.ejecucion_fisica | number:2}}&nbsp;%</td>
								</tr>
							</tbody>
						</table>	
						<div class="grid_loading" ng-hide="!control.entidad_showloading">
				  			<div class="msg">
				      			<span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  			<br /><br />
						  			<b>Cargando, por favor espere...</b>
					  			</span>
							</div>
			  			</div>					
					</div>
				</div >
				<div class="panel panel-default">
					<div>
						<div>
							<a href class="btn btn-default no-border" ng-click="control.programa_goLevel(1, false)" ng-disabled="control.programa_showloading"><b>Programas</b></a> 
							<span ng-hide="control.programa_level<2">/ 
								<a href class="btn btn-default no-border" ng-click="control.programa_goLevel(2, false)" ng-disabled="control.programa_showloading">{{ control.programa_nombre }}</a>
							</span>
							<span ng-hide="control.programa_level<3">/ 
								<a href class="btn btn-default no-border" ng-click="control.programa_goLevel(3, false)" ng-disabled="control.programa_showloading">{{ control.programa_subprograma_nombre }}</a>
							</span>
							<span ng-hide="control.programa_level<4">/ 
								<a href class="btn btn-default no-border" ng-click="control.programa_goLevel(4, false)" ng-disabled="control.programa_showloading">{{ control.programa_proyecto_nombre }}</a>
							</span>
							<span ng-hide="control.programa_level<5">/ 
								<a href class="btn btn-default no-border" ng-click="control.programa_goLevel(5, false)" ng-disabled="control.programa_showloading">{{ control.programa_actividad_nombre }}</a>
							</span>
							<span ng-hide="control.programa_level<6">/ &nbsp;{{ control.programa_renglon_nombre }}</span>
						</div>					
					</div> 
					<div style="position: relative;">
						<table class="table table-striped" st-table="control.programa_ejecucion_data" 
							   st-safe-src="control.programa_ejecucion_data_original" >
						 <thead>
							<tr>
								<th ></th>
								<th st-sort="codigo">Código</th>
								<th st-sort="nombre">Nombre</th>
								<th st-sort="vigente" style="text-align: right;">Vigente</th>
								<th st-sort="ejecutado" style="text-align: right;">Ejecutado</th>
								<th st-sort="eje_financiera" style="text-align: right;">% Ejecución Financiera</th>
								<th ng-hide="control.programa_level>4" st-sort="eje_fisica" style="text-align: right; ">% Ejecucion Física</th>
							</tr>
						</thead>
						<tfoot>
						    <tr style="font-weight: bold;">
						    	<td/>
						    	<td/>
						    	<td class="text-right"><strong>Totales</strong></td>
								<td class="text-right"><strong>{{ control.programa_ejecucion_totales[0] | currency:"Q&nbsp;":2  }}</strong></td>
								<td class="text-right"><strong>{{ control.programa_ejecucion_totales[1] | currency:"Q&nbsp;":2 }}</strong></td>
								<td class="text-right"><strong>{{ control.programa_ejecucion_totales[2] | number:2 }}&nbsp;%</strong></td>
								<td ng-hide="control.programa_level>4" class="text-right"><strong>{{ control.programa_ejecucion_totales[5] | number:2 }}&nbsp;%</strong></td>
							</tr>
						</tfoot>
						<tbody>
							<tr ng-repeat="data in control.programa_ejecucion_data">
								<td><button class="btn btn-primary" ng-show="control.programa_level<5" ng-click="control.programa_clickRow(data.codigo,data.nombre)"></button></td>
								<td class="text-nowrap"><strong>{{data.codigo}}</strong></td>
								<td title="{{data.nombre}}" style="max-width: 350px; overflow: hidden;" class="text-nowrap"><strong>{{data.nombre}}</strong></td>
								<td class="text-right">{{ data.vigente | currency:"Q&nbsp;":2 }}</td>
								<td class="text-right">{{ data.ejecutado | currency:"Q&nbsp;":2 }}</td>
								<td class="text-right">{{ data.ejecucion_financiera | number:2}}&nbsp;%</td>
								<td ng-hide="control.programa_level>4"  class="text-right">{{ data.ejecucion_fisica | number:2}}&nbsp;%</td>
							</tr>
						</tbody>
					</table>
					<div class="grid_loading" ng-hide="!control.programa_showloading">
			  			<div class="msg">
			      			<span><i class="fa fa-spinner fa-spin fa-4x"></i>
					  			<br /><br />
					  			<b>Cargando, por favor espere...</b>
				  			</span>
						</div>
		  			</div>	
					</div>
	
				</div>
			
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-sm-12">
			<div class="col-sm-6">Última actualización: {{ control.lastupdate }}</div>
		</div>		
	</div>
</div>

