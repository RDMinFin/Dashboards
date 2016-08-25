<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
.thumb {
	width: 150px;
	height: 150px;
	float: none;
	top: 7px;
	bottom: 7px;
}

.angular-google-map-container {
	height: 400px;
	width: 100%;
	position: inherit;
	z-index: 0;
	left: 0;
	top: 0;
	margin-top: 5px;
	padding-top: 5px;
}

form .progress {
	line-height: 15px;
}

.progress {
	display: inline-block;
	width: 200px;
	border: 3px groove #CCC;
}

.progress div {
	font-size: smaller;
	background: green;
	width: 0
}

input::-webkit-outer-spin-button, input::-webkit-inner-spin-button {
	-webkit-appearance: none;
	margin: 0;
}
</style>

<script type="text/ng-template" id="edit.html">
	<%@ include file="/app/components/transparencia/jerez/actividad.jsp"%>
</script>

<script type="text/ng-template" id="editCompras.html">
	<%@ include file="/app/components/transparencia/jerez/compras.jsp"%>
</script>

<div ng-controller="adminCtrl as control" class="maincontainer"
	id="title" class="all_page">

	<h3>Actividades - Seguimiento Jerez, Jutiapa</h3>
	<br />
	<div style="position: relative;">
		<div style="text-align: right;">

			<div class="btn-group" role="group">
				<a class="btn btn-default" role="button"
					ng-click="control.addCompras()" uib-tooltip="Procesos de Compra"
					tooltip-placement="left"> <span
					class="glyphicon glyphicon-shopping-cart" aria-hidden="true"></span>
				</a>
			</div>

			<div class="btn-group" role="group">
				<a class="btn btn-default" role="button"
					ng-click="control.addActivity()" uib-tooltip="Agregar actividad"
					tooltip-placement="left"> <span
					class="glyphicon glyphicon-plus" aria-hidden="true"></span>
				</a>
			</div>
		</div>
		
		<table ng-init="control.loadList()" class="table table-striped"
			st-table="control.actividades_data"
			st-safe-src="control.actividades_data_original" style="width: 100%;">
			<thead>
				<tr>
					<th></th>
					<th>ID</th>
					<th st-sort="nombre">Nombre</th>
					<th st-sort="descripcion">Descripcion</th>
					<th>Fecha de inicio</th>
					<th>Fecha de fin</th>
					<th>Coordenadas</th>
					<th>Entidades</th>
					<th>% de avance</th>
				</tr>
				<tr>
					<th></th>
					<th></th>
					<th><input st-search="nombre" placeholder="Buscar por nombre"
						class="input-sm form-control" type="search" /></th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="data in control.actividades_data track by $index">
					<td><button class="btn btn-primary"
							ng-click="control.select($index)"></button></td>
					<td class="text-nowrap"><strong>{{data.id}}</strong></td>
					<td title="{{data.nombre}}"
						style="max-width: 350px; overflow: hidden;" class="text-nowrap"><strong>{{data.nombre}}</strong></td>
					<td title="{{data.descripcion}}"
						style="max-width: 350px; overflow: hidden;" class="text-nowrap"><strong>{{data.descripcion}}</strong></td>
					<td class="text-nowrap">{{data.fecha_inicio}}</td>
					<td class="text-nowrap">{{data.fecha_fin}}</td>
					<td style="max-width: 150px; overflow: hidden;" class="text-nowrap">({{data.latitude
						| number:4 }},{{data.longitude | number:4 }})</td>
					<td title="{{data.entidades}}"
						style="max-width: 150px; overflow: hidden;" class="text-nowrap">{{data.entidades}}</td>
					<td class="text-nowrap">{{data.porcentaje_ejecucion|number:2}}%</td>
				</tr>
			</tbody>
		</table>
		<div class="grid_loading" ng-hide="!control.showloading">
			<div class="msg">
				<span><i class="fa fa-spinner fa-spin fa-4x"></i> <br />
				<br /> <b>Cargando, por favor espere...</b> </span>
			</div>
		</div>
	</div>
</div>

