<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <div ng-controller="ComprasCtrl as control" class="maincontainer" id="title">
	<h3>Tablero de Seguimiento a Estados de Excepción - Jerez, Jutiapa</h3>
	<br/>
	<div class="row">
		<div class="col-sm-6">
			<h4>Compras</h4>
		</div>
	</div>
	
	<div class="row panel panel-default" style="margin: 10px 0px 20px 0px; height: 500px; position: relative; overflow: auto;">
		<table st-table="control.compras" st-safe-src="control.original_compras" class="table table-striped">
			<thead>
				<tr>
					<th st-sort="entidad">Entidad</th>
					<th st-sort="unidad">Unidad</th>
					<th st-sort="id">NPG/NOG</th>
					<th st-sort="fecha">Publicación</th>
					<th st-sort="descripcion">Descripción</th>
					<th st-sort="modalidad">Modalidad</th>
					<th st-sort="estatus">Estado</th>
					<th st-sort="nit">Nit</th>
					<th st-sort="nombre">Nombre</th>
					<th st-sort="monto">Monto</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="row in control.compras">
					<td>{{ row.entidad }}</td>
					<td>{{ row.unidad }}</td>
					<td><a target="_blank" href="{{row.tipo=='NOG' ? 'http://www.guatecompras.gt/concursos/consultaDetalleCon.aspx?nog='+row.id : 'http://www.guatecompras.gt/PubSinConcurso/ConsultaAnexosPubSinConcurso.aspx?op=4&n='+row.id }}">{{ row.id }}</a></td>
					<td>{{ row.fecha | date }}</td>
					<td>{{ row.descripcion }}</td>
					<td>{{ row.modalidad }}</td>
					<td>{{ row.estado }}</td>
					<td>{{ row.nit }}</td>
					<td>{{ row.nombre }}</td>
					<td style="min-width: 100px;">Q {{ row.monto|number:2 }}</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	</div>

