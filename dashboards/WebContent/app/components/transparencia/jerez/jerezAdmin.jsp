<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <style>

	.thumb {
	    width: 75px;
	    height: 75px;
	    float: none;
	    top: 7px;
	}

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
	
	input::-webkit-outer-spin-button,
	input::-webkit-inner-spin-button {
	    -webkit-appearance: none;
    	margin: 0; 
	}
	</style>
	<div ng-controller="adminCtrl as control" class="maincontainer" id="title" class="all_page">
	<script type="text/ng-template" id="map.html">
        <div class="modal-header">
            <h3 class="modal-title">Mapa de Ubicación</h3>
        </div>
        <div class="modal-body" style="height: 400px;">
        	<ui-gmap-google-map id="mainmap" ng-if="refreshMap" center="map.center" zoom="map.zoom" events="map.events" options="map.options">
				<ui-gmap-marker coords="map.clickedMarker"  idKey="map.clickedMarker.id" options="map.clickedMarker.options">
        		</ui-gmap-marker>	
			</ui-gmap-google-map>
		</div>
        <div class="modal-footer">
            <button class="btn btn-primary" type="button" ng-click="ok()">OK</button>
        </div>
    </script>
	<h3>Actividades - Seguimiento Jerez, Jutiapa</h3>
	<br/>
	<div class="row">
		<div>
			<div style="position: relative; overflow: auto; height: 400px;" ng-init="control.loadList()">
				<table class="table table-striped" st-table="control.actividades_data" 
						st-safe-src="control.actividades_data_original" >
					 <thead>
						<tr>
							<th></th>
							<th>ID</th>
							<th st-sort="nombre" style="max-width: 50px;">Nombre</th>
							<th st-sort="descripcion" >Descripcion</th>
							<th >Fecha de inicio</th>
							<th >Fecha de fin</th>
							<th >Coordenadas</th>
							<th >Entidades</th>
							<th >% de avance</th>
						</tr>
						<tr>
							<th></th>
							<th></th>
							<th>
								<input st-search="nombre" placeholder="Buscar por nombre" class="input-sm form-control" type="search"/>
							</th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="data in control.actividades_data track by $index">
							<td><button class="btn btn-primary" ng-click="control.select($index)"></button></td>
							<td class="text-nowrap"><strong>{{data.id}}</strong></td>
							<td class="text-nowrap"><strong>{{data.nombre}}</strong></td>
							<td class="text-nowrap"><strong>{{data.descripcion}}</strong></td>
							<td class="text-nowrap">{{data.fecha_inicio}}</td>
							<td class="text-nowrap">{{data.fecha_fin}}</td>
							<td class="text-nowrap">({{data.latitude + "," +data.longitude }})</td>
							<td class="text-nowrap">{{data.entidades}}</td>
							<td class="text-nowrap">{{data.porcentaje_ejecucion|number:2}}%</td>
						</tr>
					</tbody>
				</table>	
				<div class="grid_loading" ng-hide="!control.showloading">
		  			<div class="msg">
		      			<span><i class="fa fa-spinner fa-spin fa-4x"></i>
				  			<br /><br />
				  			<b>Cargando, por favor espere...</b>
			  			</span>
					</div>
	  			</div>				
			</div>
			
			<form name="form" class="css-form" novalidate>
				<div class="col-xs-12 form-group" ng-class="{ 'has-error' : form.uNombreA.$touched && form.uNombreA.$invalid }">
		    		<label>Nombre
		    			<input ng-disabled="control.activityType == 1" type="text" name="uNombreA" class="form-control" ng-model="control.nombre" placeholder="Nombre de la actividad" required/>
		    		</label>
	    		</div>
	    		<div class="col-xs-12 form-group">
		    		<label>Descripción
		    			<input ng-disabled="control.activityType == 1" type="text" class="form-control" ng-model="control.descripcion" placeholder="Descripción de la actividad"/>
		    		</label>
		    	</div>
		    	<div class="col-xs-2 form-group" ng-class="{ 'has-error' : form.ufInicio.$touched && form.ufInicio.$invalid }">
		    	    <label>Fecha inicio
		    	    	<input ng-disabled="control.activityType == 1" type="date" name="ufInicio" class="form-control" ng-model="control.fecha_inicio" placeholder="fecha de inicio" required />
		    	    </label>
		   	    </div>
	    	    <div class="col-xs-2 form-group" ng-class="{ 'has-error' : form.ufFin.$touched && form.ufFin.$invalid }">
		    	    <label>Fecha fin
		    	    	<input ng-disabled="control.activityType == 1" type="date" name="ufFin" class="form-control" ng-model="control.fecha_fin" placeholder="fecha de finalización" required/>
		    	    </label>
		   	    </div>
	    	    <div class="col-xs-8 form-group" ng-class="{ 'has-error' : form.uEntidades.$touched && form.uEntidades.$invalid }">
		    	    <label>Entidades
		    	    	<input ng-disabled="control.activityType == 1" type="text" name="uEntidades" class="form-control" ng-model="control.entidades" placeholder="Entidades involucradas" />
		    	    </label>
	   	    	</div>
	    	    <div class="col-xs-2 form-group" ng-class="{ 'has-error' : form.uCoord_lat.$touched && form.uCoord_lat.$invalid }">
		    		 <label>Latitud
		    	    	<input ng-disabled="true" type="text" name="uCoord_lat" class="form-control"  ng-model="coord_lat" placeholder="Latitud" required/>
		    	    </label>
	    	    </div>
	    	    <div class="col-xs-2 form-group" ng-class="{ 'has-error' : form.uCoord_long.$touched && form.uCoord_long.$invalid }">
		    	    <label>Longitud
		    	    	<input ng-disabled="true" type="text" name="uCoord_long" class="form-control"  ng-model="coord_long" placeholder="Longitud" required/>
		    	    </label>
	   	    	</div>
	   	    	<div class="col-xs-2 ">
	   	    		<br/>
	   	    		<input type="button" class="btn btn-info" ng-click="control.showMap()" value="Fijar coordenadas"/>
	   	    	</div>
	   	    	<div class="col-xs-6 form-group" ng-class="{ 'has-error' : form.uEjecucion.$touched && form.uEjecucion.$invalid }">
		    	    <label>% Ejecución física 
		    	    	<input ng-disabled="control.activityType == 1" type="number" name="uEjecucion" class="form-control" ng-model="control.porcentaje_ejecucion" placeholder="Avance de la actividad" required/>
		    	    </label>
		    	</div>
		    	
	    	    <div class="col-xs-12" style="padding: 10px;">
	    	    	<h4>Responsable</h4> 
	    	    	<div class="col-xs-3 form-group" ng-class="{ 'has-error' : form.uNombre.$touched && form.uNombre.$invalid }">
			    	    <label>Nombre 
			    	    	<input ng-disabled="control.activityType == 1" type="text" name="uNombre" class="form-control" ng-model="control.responsable_nombre" placeholder="Nombre del responsable" />
			    	    </label>
		    	    </div>
		    	    <div class="col-xs-3 form-group" ng-class="{ 'has-error' : form.uTel.$touched && form.uTel.$invalid }">
			    	    <label>Telefono 
			    	    	<input ng-disabled="control.activityType == 1" type="text" name="uTel" class="form-control" ng-model="control.responsable_telefono" placeholder="telefono del responsable" />
			    	    </label>
		    	    </div>
		    	    <div class="col-xs-3 form-group" ng-class="{ 'has-error' : form.uEmail.$touched && form.uEmail.$invalid }">
			    	    <label>Correo
			    	    	<input ng-disabled="control.activityType == 1" type="email" name="uEmail" class="form-control"
			    	    	ng-model="control.responsable_correo" placeholder="correo del responsable"   />
			    	    </label>
		    	    </div>
	    	    </div>
	    	     <div class="col-xs-6">
		    		<input type="button" class="btn btn-primary" ng-click="control.clear()" value="Nuevo"/>
		    		<input type="button" class="btn btn-primary" ng-click="control.save()" value="Guardar" ng-disabled="form.$invalid" />
		    		<input type="button" class="btn btn-danger"  ng-click="control.erase()" value="Eliminar" ng-disabled="control.id<=0" />
	    		 </div>
	  		</form>
		</div>
	</div>
	<div >
			<form name="myForm">
		    <fieldset>
		      <legend>Carga de documentos a la actividad</legend>
		      <br>Documento:
		      <input type="file" ngf-select ng-model="control.docFile" name="file"    
		             ngf-max-size="100MB" required
		             accept="image/*"
		             ngf-model-invalid="control.errorFile"
		             ng-disabled="control.id <= 0"
		             class="btn btn-primary"
		             >
		      <i ng-show="myForm.file.$error.required">*obligatorio</i><br>
		      <i ng-show="myForm.file.$error.maxSize">Archivo excede el tamaño -
		          {{control.errorFile.size / 1000000|number:1}} MB: max 100MB</i>
		      <img ngf-src="control.docFile" class="thumb">
		      <button class="btn btn-warning"  ng-click="control.docFile = null" ng-show="control.id > 0 &&  control.docFile">
		      	 <span class="glyphicon glyphicon-trash"></span> Remover
		      </button>
		      <br>
		      <button ng-disabled="{{control.id <= 0}} && !myForm.$valid" class="btn btn-success"
		              ng-click="control.uploadPic(control.docFile)"> 
		              <span class="glyphicon glyphicon-upload"></span>Agregar
		      </button>
		      <div class="progress" ng-show="control.docFile.progress >= 0">
		        <span style="width:{{control.docFile.progress}}%" ng-bind="control.docFile.progress + '%'"></span>
		      </div>
		      <span ng-show="control.docFile.result">Carga exitosa</span>
		      <span class="err" ng-show="control.errorMsg">{{control.errorMsg}}</span>
		    </fieldset>
		    <br>
		  </form>
		</div>
</div>

