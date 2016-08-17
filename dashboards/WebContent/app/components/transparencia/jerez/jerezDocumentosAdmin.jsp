<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <style>
    form .progress {
	    line-height: 15px;
	}
	
	.progress {
	    display: inline-block;
	    width: 100px;
	    border: 3px groove #CCC;
	}

	.progress div {
	    font-size: smaller;
	    background: green;
	    width: 0
	}
	</style>
    <div ng-controller="jerezDocumentosController as control" class="maincontainer" id="title">
	<h3>Tablero de Seguimiento a Estados de Excepción - Jerez, Jutiapa</h3>
	<br/>
	<div class="row">
		<div class="col-sm-6">
			<h4>Documentos</h4>
		</div>
	</div>
	<uib-accordion>
    <div uib-accordion-group class="panel-info row" heading="Agregar documento">
		<form name="myForm" class="css-form" novalidate style="margin-top: 15px;">
			<div class="form-group row">
		    	Documento:
		    	<input type="file" ngf-select ng-model="control.docFile" name="file"    
		             ngf-max-size="100MB" required
		             ngf-model-invalid="errorFile"
		             class="form-control"/>
		    	<i ng-show="myForm.file.$error.required">*obligatorio</i><br>
		    	<i ng-show="myForm.file.$error.maxSize">Archivo excede el tamaño -
		          {{errorFile.size / 1000000|number:1}} MB: max 100MB</i>
			</div>
			<div class="form-group row" style="text-align:center;">
				<div class="btn-group" role="group"  >
		      		<input type="button" value="Remover" class="btn btn-warning"  ng-click="control.docFile = null" ng-show="control.docFile"/>
		      		<input type="button" value="Agregar" ng-disabled="!myForm.$valid" class="btn btn-success" ng-click="control.uploadFile(control.docFile)"/>
				</div>	
			</div>
			<div class="form-group row" style="text-align:center;">
		    	<div class="progress" ng-show="control.docFile.progress >= 0">
		        	<span style="width:{{control.docFile.progress}}%" ng-bind="control.docFile.progress + '%'"></span>
		      	</div>
		      	<span class="err" ng-show="errorMsg">{{errorMsg}}</span>
			</div>
		  </form>
	</div>
	</uib-accordion>
	<div class="row panel panel-default" style="margin: 10px 0px 20px 0px; height: 500px; position: relative; overflow: auto;">
		<table st-table="control.documentos" st-safe-src="control.original_documentos" class="table table-striped">
			<thead>
				<tr>
					<th st-sort="id">ID</th>
					<th st-sort="nombre">Nombre</th>
					<th st-sort="actividad">Actividad</th>
					<th st-sort="usuario_creacion">Usuario Creación</th>
					<th st-sort="fecha_creacion">Fecha de Creación</th>
					<th>Link</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="row in control.documentos">
					<td>{{ row.id }}</td>
					<td>{{ row.nombre }}</td>
					<td>{{ row.actividad }}</td>
					<td>{{ row.usuario_creacion }}</td>
					<td>{{ row.fecha_creacion | date }}</td>
					<td><a href="/SDownload?place=jerez&iddoc={{ row.id }}"><span class="glyphicon glyphicon-save"></span></a></td>
				</tr>
			</tbody>
		</table>
	</div>
	
	</div>

