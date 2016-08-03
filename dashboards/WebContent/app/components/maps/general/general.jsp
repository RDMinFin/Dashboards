<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="mainmap" ng-controller="mapsGeneralController as mapsGeneral" class="all_page">
	<div style="position: relative; height: 100%;" id="title">
		<ui-gmap-google-map center="map.center" zoom="map.zoom" options="map.options">
			<ui-gmap-circle ng-repeat="c in towns track by c.id" center="c.center" radius="c.radius"
                visible="true" events="c.events" stroke="c.stroke" fill="c.fill">
                <ui-gmap-window show="c.showinfowindow" coords="c.center" isIconVisibleOnClick="false" options=""  ng-cloak>
		            <div>{{ c.name }}</div>
		        </ui-gmap-window>
            </ui-gmap-circle>
		</ui-gmap-google-map>
	</div>
</div>