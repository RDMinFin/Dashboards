<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="modal-header">
	<h3 class="modal-title">Edición de Compras</h3>
</div>
<div class="modal-body" style="margin-left: 15px; margin-right: 15px;">
	<uib-accordion>
	<div style="padding-top: 5px;">
		<div uib-accordion-group class="panel-info row"
			heading="Agregar proceso de compra">
			<form name="myForm" class="css-form" novalidate style="margin-top: 15px;">
				<div class="form-group row" style="text-align: center;">
					<label class="radio-inline"><input type="radio" ng-model="tipoCompra" ng-required="!tipoCompra" value="NOG" ng-change="clearError()" />NOG</label>
					<label class="radio-inline"><input type="radio" ng-model="tipoCompra" ng-required="!tipoCompra" value="NPG" ng-change="clearError()" />NPG</label>
				</div>
				<div class="form-group row" style="text-align: center;">
					<input type="text" ng-model="idCompra" ng-change="clearError()" required	/>
				</div>
				<div class="form-group row" style="text-align: center;">
					<div class="btn-group" role="group">
						<input type="button" value="Agregar" ng-disabled="!myForm.$valid" class="btn btn-success" ng-click="addCompra()" />
					</div>
				</div>
				<div class="row" style="font-size:16px; text-align: center; color: red;" ng-show="error">
					<label><span class="glyphicon glyphicon-exclamation-sign">{{errorMessage}}</span> </label>
				</div>
			</form>
		</div>
	</div>
	</uib-accordion>

	<div class="row panel panel-default"
		style="margin: 10px 0px 20px 0px; height: 250px; position: relative; overflow: auto;">
		<table st-table="compras" st-safe-src="original_compras"
			class="table table-striped">
			<thead>
				<tr>
					<th st-sort="entidad">Entidad</th>
					<th st-sort="id">NPG/NOG</th>
					<th st-sort="fecha">Publicación</th>
					<th st-sort="monto">Monto</th>
					<th>Borrar</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="row in original_compras">
					<td>{{ row.entidad }}</td>
					<td><a target="_blank" href="{{row.tipo=='NOG' ? 'http://www.guatecompras.gt/concursos/consultaDetalleCon.aspx?nog='+row.id : 'http://www.guatecompras.gt/PubSinConcurso/ConsultaAnexosPubSinConcurso.aspx?op=4&n='+row.id }}">{{ row.id }}</a></td>
					<td>{{ row.fecha | date }}</td>
					<td style="min-width: 100px;">Q {{ row.monto|number:2 }}</td>
					<td style="text-align: center;"><a
						ng-click="deleteCompra(row.tipo, row.id)"><span style="color: red;"
							class="glyphicon glyphicon-remove"></span></a></td>
				</tr>
			</tbody>
		</table>
		<div class="grid_loading" ng-hide="!showloading">
 			<div class="msg">
     			<span><i class="fa fa-spinner fa-spin fa-4x"></i>
	  			<br /><br />
	  			<b>Cargando, por favor espere...</b>
  				</span>
			</div>
		</div>
	</div>
</div>
<div class="modal-footer">
	<button class="btn btn-danger" type="button" ng-click="cancel()">Cerrar</button>
</div>