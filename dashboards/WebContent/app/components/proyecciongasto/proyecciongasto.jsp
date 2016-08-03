<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div ng-controller="proyecciongastoController as proyeccion" class="maincontainer" id="title">
	<div class="row">
		<div class="col-sm-6">
			<div class="col-sm-6">
				<h3>Proyección del Gasto</h3>
			</div>
			<div class="col-sm-6">
				<div class="input-group" style="margin-top: 18px;">
				  <input type="text" class="form-control" aria-describedby="basic-addon2">
				  <span class="input-group-addon" id="basic-addon2">Vigente</span>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-sm-6">
			<div class="col-sm-6">
			</div>
			<div class="col-sm-6">
				Fuente
			</div>
		</div>
	</div>
	<br/>
	<div>
		<div class="row">
			<div class="col-sm-6">
				<div>
					<div style="width: 80%; height: 100%; margin: 0 auto;">
						<div>Gasto en millones de quetzales</div>
						<canvas id="total" height="170" class="chart chart-line" chart-data="proyeccion.data"
						  chart-labels="proyeccion.labels" chart-legend="false" chart-series="proyeccion.series" chart-options="proyeccion.options"
						  chart-click="proyeccion.onClick">
						</canvas>
						<div class="chart-legend">
					    <ul class="line-legend">
					        <li ng-repeat="year in proyeccion.series track by $index">
					            <span ng-style="{ 'background' : ((proyeccion.show[$index] === true) ? proyeccion.colors[$index] : 'silver'), 'cursor': 'pointer'}" 
					            title="Mostrar/Ocultar"
					            ng-click="proyeccion.show[$index] = !proyeccion.show[$index]; proyeccion.seriesShow($index)">
					                </span>
					            {{ proyeccion.series[$index] }} 
					        </li>
					    </ul>
					</div>
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div style="width: 80%; margin: 0 auto;">
					<div style="position: relative; height: 400px;">
						<ui-gmap-google-map id="mainmap" center="map.center" zoom="map.zoom" options="map.options">
							<ui-gmap-circle ng-repeat="c in towns track by c.id" center="c.center" stroke="map.circle_stroke" fill="map.circle_fill" radius="map.circle_radius"
				                visible="true" events="c.events">
				                <ui-gmap-window show="c.showinfowindow" coords="c.center" isIconVisibleOnClick="false" options=""  ng-cloak>
						            <div>{{ c.name }}</div>
						        </ui-gmap-window>
				            </ui-gmap-circle>
						</ui-gmap-google-map>
					</div>
				</div>
			</div> 
		</div>
		<br/>
		<br/>
		<div class="row">
			<div class="col-sm-6">
				<div style="width: 80%; height: 80%; margin: 0 auto;">
					<h4>Listado de Entidades</h4>
					<div>Gasto en millones de quetzales</div>
					<div ui-i18n="es">
						<div id="grid1" ui-grid="proyeccion.entidades_gridOptions" ui-grid-selection ui-grid-tree-view ui-grid-move-columns class="grid"></div>
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div style="width: 80%; height: 80%; margin: 0 auto;">
					<h4>Objetos del Gasto</h4>
					<div>Gasto en millones de quetzales</div>
					<div ui-i18n="es">
						<div id="grid2" ui-grid="proyeccion.renglones_gridOptions" ui-grid-tree-view ui-grid-selection ui-grid-move-columns class="grid"></div>
					</div>
				</div>
			</div>
		</div>
		<br/><br/>
		<div class="row">
			<div class="col-sm-6">
				<div style="width: 80%; margin: 0 auto; ">
					<h4>Entidades</h4>
					<div>Gasto en millones de quetzales</div>
					<canvas id="entidad" class="chart chart-line" chart-data="proyeccion.entidad_data"
					  chart-labels="proyeccion.labels" chart-legend="false" chart-series="proyeccion.entidad_series" chart-options="proyeccion.options">
					</canvas>
					<div class="chart-legend">
					    <ul class="line-legend">
					        <li ng-repeat="year in proyeccion.entidad_series track by $index">
					            <span ng-style="{ 'background' : ((proyeccion.entidad_show[$index] === true) ? proyeccion.colors[$index] : 'silver'), 'cursor': 'pointer'}" 
					            title="Mostrar/Ocultar"
					            ng-click="proyeccion.entidad_show[$index] = !proyeccion.entidad_show[$index]; proyeccion.entidad_seriesShow($index)">
					                </span>
					            {{ proyeccion.entidad_series[$index] }} 
					        </li>
					    </ul>
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div style="width: 80%; margin: 0 auto; ">
					<h4>Objetos del Gasto</h4>
					<div>Gasto en millones de quetzales</div>
					<canvas id="renglon" class="chart chart-line" chart-data="proyeccion.renglon_data"
					  chart-labels="proyeccion.labels" chart-legend="false" chart-series="proyeccion.renglon_series" chart-options="proyeccion.options">
					</canvas>
					<div class="chart-legend">
					    <ul class="line-legend">
					        <li ng-repeat="year in proyeccion.renglon_series track by $index">
					            <span ng-style="{ 'background' : ((proyeccion.renglon_show[$index] === true) ? proyeccion.colors[$index] : 'silver'), 'cursor': 'pointer'}" 
					            title="Mostrar/Ocultar"
					            ng-click="proyeccion.renglon_show[$index] = !proyeccion.renglon_show[$index]; proyeccion.renglon_seriesShow($index)">
					                </span>
					            {{ proyeccion.renglon_series[$index] }} 
					        </li>
					    </ul>
					</div>
				</div>
			</div>
		</div>
		<br/><br/>
	</div>
</div>