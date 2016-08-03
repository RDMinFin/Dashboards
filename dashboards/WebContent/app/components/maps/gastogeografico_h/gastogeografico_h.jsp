<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="gastogeograficomap" ng-controller="mapsGastoGeograficoController as mapsGG" class="all_page">
	<div style="position: relative; height: 100%;" id="title">
		<ui-gmap-google-map center="map.center" zoom="map.zoom" options="map.options">
			<ui-gmap-layer namespace="visualization" type="HeatmapLayer" show="map.showHeat" onCreated="map.heatLayerCallback"></ui-gmap-layer>
		</ui-gmap-google-map>
	</div>
</div>