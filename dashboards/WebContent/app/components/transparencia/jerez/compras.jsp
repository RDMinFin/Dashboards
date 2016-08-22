<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="modal-header">
	<h3 class="modal-title">Edición de Compras</h3>
</div>
<div class="modal-body" style="margin-left: 15px; margin-right: 15px;">
	<uib-accordion>
	<div style="padding-top: 5px;">
		<div uib-accordion-group class="panel-info row"
			heading="Agregar documento">
			<form name="myForm" class="css-form" novalidate
				style="margin-top: 15px;">

				<div class="form-group row" style="text-align: center;">
					<div class="btn-group" role="group">
						<input type="button" value="Remover" class="btn btn-warning"
							ng-click="$root.docFile = null" ng-show="$root.docFile" /> <input
							type="button" value="Agregar" ng-disabled="!myForm.$valid"
							class="btn btn-success" ng-click="uploadPic($root.docFile)" />
					</div>
				</div>

			</form>
		</div>
	</div>
	</uib-accordion>

	<div class="row panel panel-default"
		style="margin: 10px 0px 20px 0px; height: 250px; position: relative; overflow: auto;">
		<table st-table="control.documentos" st-safe-src="original_documentos"
			class="table table-striped">
			<thead>
				<tr>
					<th st-sort="numero">Número</th>
					<th st-sort="tipo">Tipo</th>
					<th>Borrar</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="row in documentos">
					<td>{{ row.id }}</td>
					<td style="max-width: 150px; overflow: hidden;" class="text-nowrap">{{
						row.nombre }}</td>
					<td style="max-width: 75px; overflow: hidden;" class="text-nowrap">{{
						row.usuario_creacion }}</td>
					<td style="max-width: 125px; overflow: hidden;" class="text-nowrap">{{
						row.fecha_creacion | date }}</td>
					<td style="text-align: center;"><a
						ng-click="deleteDoc(row.id)"><span style="color: red;"
							class="glyphicon glyphicon-remove"></span></a></td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<div class="modal-footer">
	<button class="btn btn-danger" type="button" ng-click="cancel()">Cerrar</button>
</div>