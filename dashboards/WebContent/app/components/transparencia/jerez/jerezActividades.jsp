<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <style>
	.event_title {
		font-size: 14px;
		font-weight: bold;
	}
	
	.carousel-control.right{
		background-image: none;
	}
	
	.carousel-control.left{
		background-image: none;
	}
	</style>
	<div ng-controller="jerezActividadesController as control" class="maincontainer" id="title" class="all_page">
	<script type="text/ng-template" id="map.html">
        <div class="modal-header">
            <h3 class="modal-title">Mapa de Ubicación</h3>
        </div>
        <div class="modal-body" style="height: 400px;">
            			<ui-gmap-google-map id="mainmap" ng-if="refreshMap" center="map.center" zoom="map.zoom" options="map.options">
							<ui-gmap-marker idkey="1" coords="map.center"></ui-gmap-marker>
						</ui-gmap-google-map>
		</div>
        <div class="modal-footer">
            <button class="btn btn-primary" type="button" ng-click="ok()">OK</button>
        </div>
    </script>
	<h3>Tablero de Seguimiento a Estados de Excepción - Jerez, Jutiapa</h3>
	<br/>
	<div><h4>Línea de Tiempo</h4></div>
	<div style="width: 100%; height: 500px; padding: 30px;" class="panel panel-default">
		<div class="row" style="width: 100%; height: 440px; position: relative; bottom: 0px; right: 0px; float: left;" ng-show="control.side!=''">
				<div class="col-sm-6"></div>
				<div class="col-sm-6" style="padding-left: 80px; height: 100%;">
					<div style="height: 100%; display: table; padding: 25px;" class="panel panel-default">
						<div style="display: table-cell; vertical-align: middle;">
							<div class="row">
								<div class="col-sm-10" style="font-size: 18px; font-weight: bold;">Actividad</div> 
								<div class="col-sm-2" style="text-align: right;"></div>
							</div>
							<br/>
							<div class="event_title">{{ control.actividad.nombre }}</div>
							<div class="event_title" style="text-align: right;">{{ control.actividad.porcentaje_ejecucion }} % de avance</div>
							<br/>
							<div class="row">
								<div class="col-sm-3" style="white-space: nowrap;"><i class="glyphicon glyphicon-globe"></i> Geoposición:</div><div class="col-sm-9 event_info">{{ control.actividad.latitude }}, {{ control.actividad.longitude}}</div>
								<div class="col-sm-12" style="height: 10px;"></div>
								<div class="col-sm-3" style="white-space: nowrap;"><i class="glyphicon glyphicon-time"></i> Inicio:</div><div class="col-sm-9 event_info">{{ control.actividad.fecha_inicio }}</div>
								<div class="col-sm-12" style="height: 10px;"></div>
								<div class="col-sm-3" style="white-space: nowrap;"><i class="glyphicon glyphicon-time"></i> Fin:</div><div class="col-sm-9 event_info">{{ control.actividad.fecha_fin }}</div>
							</div>
							<br/>
							<div>
								<uib-carousel active="active" interval="5000" no-wrap="false" style="height: 125px;">
							      <uib-slide ng-repeat="foto in control.actividad.fotos track by $index" index="$index">
							        <img ng-src="/SPicture?subp=jerez&idevento={{ control.actividad.id }}&pic={{ foto }}&pic_h=125" style="margin:auto;">
							        <div class="carousel-caption">
							          <h4 style="background-color: transparent;">{{  }}</h4>
							        </div>
							      </uib-slide>
							    </uib-carousel>
							</div>
						</div>
					</div>
				</div>	
			</div>
		<div>
			<div class="row" style="position: relative;">
				<div style="float: left; position: absolute; left:0px; top: 0px; width: 100%; height: 440px; overflow-y: scroll ">
					<timeline>
					    <!-- can also hard-code to side="left" or side="right" -->
					    <timeline-event ng-repeat="actividad in control.actividades track by $index" side="{{ control.side }}">
					      <!-- uses angular-scroll-animate to give it some pop -->
					      <timeline-badge class="success timeline-hidden"
					                      when-visible="control.animateElementIn" when-not-visible="control.animateElementOut">
					        <i class="glyphicon glyphicon-check"></i>
					      </timeline-badge>
					
					      <!-- uses angular-scroll-animate to give it some pop -->
					      <timeline-panel class="success timeline-hidden"
					                      when-visible="control.animateElementIn" when-not-visible="control.animateElementOut" ng-click="control.selectActividad($index)" style="cursor: pointer;">
					        <timeline-heading>
					          <div style="width: 100%" class="event_title">{{actividad.nombre}}</div>
							  <p style="width: 100%" ng-if="actividad.fecha_inicio">
					            <small class="text-muted"><i class="glyphicon glyphicon-time"></i>{{actividad.fecha_inicio}} al {{ actividad.fecha_fin}}</small>
					          </p>
					          <p style="width: 100%" ng-if="actividad.entidades" ng-bind-html="control.trustAsHtml(actividad.entidades)"></p>
					          <p style="width: 100%" ng-if="actividad.descripcion" ng-bind-html="control.trustAsHtml(actividad.descripcion)"></p>
					        </timeline-heading>
					        <p ng-bind-html="control.trustAsHtml(actividad.contentHtml)"></p>
					        <timeline-footer ng-if="actividad.footerContentHtml">
					          <span ng-bind-html="actividad.trustAsHtml(evento.footerContentHtml)"></span>
					        </timeline-footer>
					        <div class="text-right" ng-show="control.actividad_seleccionada==$index"><button type="button" class="btn btn-danger" ng-click="control.cerrarActividad(); $event.stopPropagation();" >Cerrar</button>&nbsp;<button type="button" class="btn btn-info" ng-click="control.open(actividad.latitude, actividad.longitude); $event.stopPropagation();">Mapa</button></div>
					      </timeline-panel>
					    </timeline-event>
					  </timeline>
				</div>
			</div>
		</div>
	</div>
	<div style="text-align: right">Total de {{ control.actividades.length }} Actividades</div>
	<div class="row">
		<div class="col-sm-12">
			<div class="col-sm-6">Última actualización: {{ control.lastupdate }}</div>
		</div>		
	</div>
</div>

