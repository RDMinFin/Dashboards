<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="modal-header">
	<h3 class="modal-title">Edición de actividad</h3>
</div>
<div class="modal-body" style="margin-left: 15px; margin-right: 15px;">
	<uib-tabset active="activeTab"> <uib-tab index="0"
		heading="Detalle">
	<form name="form" class="css-form" novalidate style="margin-top: 15px;">
		<div class="form-group row">
			<div class="input-group col-md-12"
				ng-class="{ 'has-error' : form.uNombreA.$touched && form.uNombreA.$invalid }">
				<span class="input-group-addon" id="basic-addon1"><i
					class="glyphicon glyphicon-briefcase"></i></span> <input
					aria-describedby="basic-addon1" ng-disabled="$root.id>0"
					type="text" name="uNombreA" class="form-control"
					ng-model="$root.nombre" placeholder="Nombre de la actividad"
					required />
			</div>
		</div>
		<div class="form-group row">
			<div class="input-group col-md-12">
				<span class="input-group-addon" id="basic-addon2"><i
					class="glyphicon glyphicon-list-alt"></i></span> <input
					aria-describedby="basic-addon2" ng-disabled="$root.id>0"
					type="text" class="form-control" ng-model="$root.descripcion"
					placeholder="Descripción de la actividad" />
			</div>
		</div>
		<div class="form-group row">
			<div style="padding-left: 0" class="col-md-6">
				<div class="input-group"
					ng-class="{ 'has-error' : form.ufInicio.$touched && form.ufInicio.$invalid }">
					<span class="input-group-addon" id="basic-addon3"><i
						class="glyphicon glyphicon-calendar"></i></span> <input
						class="form-control" aria-describedby="basic-addon3"
						ng-disabled="$root.id>0" type="date" name="ufInicio"
						ng-model="$root.fecha_inicio" required />
				</div>
			</div>
			<div style="padding-right: 0" class="col-md-6">
				<div class="input-group"
					ng-class="{ 'has-error' : form.ufFin.$touched && form.ufFin.$invalid }">
					<span class="input-group-addon" id="basic-addon4"><i
						class="glyphicon glyphicon-calendar"></i></span> <input
						class="form-control" aria-describedby="basic-addon4"
						ng-disabled="$root.id>0" type="date" name="ufFin"
						ng-model="$root.fecha_fin" required />
				</div>
			</div>
		</div>
		<div class="form-group row">
			<div class="input-group col-md-12"
				ng-class="{ 'has-error' : form.uEntidades.$touched && form.uEntidades.$invalid }">
				<span class="input-group-addon" id="basic-addon5"><i
					class="glyphicon glyphicon-list"></i></span> <input
					aria-describedby="basic-addon5" ng-disabled="$root.id>0"
					type="text" name="uEntidades" class="form-control"
					ng-model="$root.entidades" placeholder="Entidades involucradas" />
			</div>
		</div>
		<div class="form-group row">
			<div class="input-group col-md-12"
				ng-class="{ 'has-error' : form.uCoord.$invalid }">
				<span class="input-group-addon" id="basic-addon6"><i
					class="glyphicon glyphicon-globe"></i></span> <input
					aria-describedby="basic-addon6" ng-disabled="true" type="text"
					name="uCoord" class="form-control" ng-model="$root.coord"
					placeholder="Coordenadas" /> <span class="input-group-btn">
					<input type="button" class="btn btn-info" ng-click="changeTab(1)"
					value="Fijar coordenadas" />
				</span>
			</div>
		</div>
		<div class="form-group row">
			<div class="input-group col-md-12"
				ng-class="{ 'has-error' : form.uEjecucion.$touched && form.uEjecucion.$invalid }">
				<span class="input-group-addon" id="basic-addon7"><i
					class="glyphicon glyphicon-cog"></i></span> <input
					aria-describedby="basic-addon7" type="number" name="uEjecucion"
					class="form-control" ng-model="$root.porcentaje_ejecucion"
					placeholder="Avance de la actividad" required /> <span
					class="input-group-addon">%</span>
			</div>
		</div>
		<h4>Responsable</h4>
		<div class="form-group row">
			<div class="input-group col-md-12"
				ng-class="{ 'has-error' : form.uNombre.$touched && form.uNombre.$invalid }">
				<span class="input-group-addon" id="basic-addon8"><i
					class="glyphicon glyphicon-user"></i></span> <input
					aria-describedby="basic-addon8" ng-disabled="$root.id>0"
					type="text" name="uNombre" class="form-control"
					ng-model="$root.responsable_nombre"
					placeholder="Nombre del responsable" />
			</div>
		</div>
		<div class="form-group row">
			<div style="padding-left: 0" class="col-md-6">
				<div class="input-group"
					ng-class="{ 'has-error' : form.uTel.$touched && form.uTel.$invalid }">
					<span class="input-group-addon" id="basic-addon9"><i
						class="glyphicon glyphicon-phone-alt"></i></span> <input
						aria-describedby="basic-addon9" ng-disabled="$root.id>0"
						type="text" name="uTel" class="form-control"
						ng-model="$root.responsable_telefono"
						placeholder="telefono del responsable" />
				</div>
			</div>
			<div style="padding-right: 0" class="col-md-6">
				<div class="input-group"
					ng-class="{ 'has-error' : form.uEmail.$touched && form.uEmail.$invalid }">
					<span class="input-group-addon" id="basic-addon10"><i
						class="glyphicon glyphicon-envelope"></i></span> <input
						aria-describedby="basic-addon10" ng-disabled="$root.id>0"
						type="email" name="uEmail" class="form-control"
						ng-model="$root.responsable_correo"
						placeholder="correo del responsable" />
				</div>
			</div>
		</div>
		<div class="form-group row" style="text-align: center;">
			<div class="btn-group" role="group">
				<input type="button" class="btn btn-primary" ng-click="save()"
					value="Guardar" ng-disabled="form.$invalid" /> <input
					type="button" ng-show="$root.id>0" class="btn btn-danger"
					ng-click="erase()" value="Eliminar" />
			</div>
		</div>
	</form>
	</uib-tab> <uib-tab index="1" heading="Coordenadas" select="mapTabSelect()">
	<div>
		<div class="angular-google-map-container">
			<ui-gmap-google-map ng-if="$root.render" id="mainmap"
				center="map.center" zoom="map.zoom" events="map.events"
				options="map.options"> <ui-gmap-marker
				coords="map.clickedMarker" idKey="map.clickedMarker.id"
				options="map.clickedMarker.options"></ui-gmap-marker> </ui-gmap-google-map>
		</div>
	</div>
	</uib-tab> <uib-tab ng-show="$root.id>0" index="2" heading="Documentos">
	<uib-accordion>
	<div style="padding-top: 5px;">
		<div uib-accordion-group class="panel-info row"
			heading="Agregar documento">
			<form name="myForm" class="css-form" novalidate
				style="margin-top: 15px;">
				<div class="form-group row">
					Documento: <input type="file" ngf-select ng-model="$root.docFile"
						name="file" ngf-max-size="100MB" required accept="image/*"
						ngf-model-invalid="errorFile" ng-disabled="$root.id <= 0"
						class="form-control" /> <i ng-show="myForm.file.$error.required">*obligatorio</i><br>
					<i ng-show="myForm.file.$error.maxSize">Archivo excede el
						tamaño - {{errorFile.size / 1000000|number:1}} MB: max 100MB</i>
				</div>
				<div class="form-group row" style="text-align: center;">
					<img ngf-src="$root.docFile" class="thumb">
				</div>
				<div class="form-group row" style="text-align: center;">
					<div class="btn-group" role="group">
						<input type="button" value="Remover" class="btn btn-warning"
							ng-click="$root.docFile = null" ng-show="$root.docFile" /> <input
							type="button" value="Agregar" ng-disabled="!myForm.$valid"
							class="btn btn-success" ng-click="uploadPic($root.docFile)" />
					</div>
				</div>
				<div class="form-group row" style="text-align: center;">
					<div class="progress" ng-show="$root.docFile.progress >= 0">
						<span style="width: {{$root.docFile.progress"
							ng-bind="$root.docFile.progress + '%'"></span>
					</div>
					<span class="err" ng-show="errorMsg">{{errorMsg}}</span>
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
					<th st-sort="id">ID</th>
					<th st-sort="nombre">Nombre</th>
					<th st-sort="usuario">Usuario</th>
					<th st-sort="fecha">Fecha</th>
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
	</uib-tab> </uib-tabset>
</div>
<div class="modal-footer">
	<button class="btn btn-danger" type="button" ng-click="cancel()">Cerrar</button>
</div>