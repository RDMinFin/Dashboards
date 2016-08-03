<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<style>
	.bwindow {
		cursor:pointer; 
		margin: 0 auto;
	}
	
	.bwindow_title{
		text-align: center;
		font-weight: bold;
		font-size: 14px;
		margin-bottom: 15px;
	}
	
	table td{
		vertical-align: middle;
		padding: 15px;
	}
</style>
<div ng-controller="jerezController as control" class="maincontainer all_page" id="title">
	<h3>Tablero de Seguimiento a Estados de Excepción - Jerez, Jutiapa</h3>
	<br/>
	<div class="container container-fluid" style="height: 100%;">
		<div style="text-align: left; width: 100%; margin: auto;">
			<table style="width: 100%;">
				<tr>
					<td ng-click="go('/transparencia/jerez/ejecucion')">
						<div class="bwindow_title">Ejecución Física y Financiera</div>
						<div class="panel panel-default bwindow" style="position: relative; width: 300px; height: 200px; overflow: hidden;">
							<div style="position: absolute; float: left; width: 100%; overflow: hidden;"><img src="/SPicture?subp=jerez&idevento=-1&pic=b1.png&pic_w=300" alt="Ejecución Física y Financiera" class="img-rounded"></div>
							<div style="position: absolute; width:299px; bottom: 0px;" class="btn-group">
								<button type="button" class="btn btn-default" style="width: 50%; height: 40px;">{{ control.ejecucion_financiera | number:2 }}% Financiera</button>
	  							<button type="button" class="btn btn-default" style="width: 50%; height: 40px;">{{ control.ejecucion_fisica | number:2 }}% Física</button>
							</div>
						</div>
					</td>
					<td ng-click="go('/transparencia/jerez/actividades')">
						<div class="bwindow_title">Actividades</div>
						<div class="panel panel-default bwindow" style="position: relative; width: 300px; height: 200px; overflow: hidden;">
							<div style="position: absolute; float: left; width: 100%; overflow: hidden;"><img src="/SPicture?subp=jerez&idevento=-1&pic=b2.png&pic_w=300" alt="Actividades" class="img-rounded"></div>
							<div style="position: absolute; width:299px; bottom: 0px;" class="btn-group">
								<button type="button" class="btn btn-default" style="width: 100%; height: 40px;">{{ control.num_actividades }} actividades</button>
	  						</div>
						</div>
					</td>
				</tr>
				<tr>
					<td ng-click="go('/transparencia/jerez/mapa')">
						<div class="bwindow_title">Mapa</div>
						<div class="panel panel-default bwindow" style="position: relative; width: 300px; height: 200px; overflow: hidden;">
							<div style="position: absolute; float: left; width: 100%; overflow: hidden; height: 190px;">
								<ui-gmap-google-map id="mapjerez" center="mapjerez.center" zoom="mapjerez.zoom" options="mapjerez.options">
								</ui-gmap-google-map>
							</div>
							<div style="position: absolute; width:299px; bottom: 0px;" class="btn-group">
								<button type="button" class="btn btn-default" style="width: 100%; height: 40px;">Jerez Jutiapa</button>
							</div>
						</div>
					</td>
					<td ng-click="go('/transparencia/jerez/documentos')">
						<div class="bwindow_title">Documentos</div>
						<div class="panel panel-default bwindow" style="position: relative; width: 300px; height: 200px; overflow: hidden;">
							<div style="position: absolute; float: left; width: 100%; overflow: hidden;"><img src="/SPicture?subp=jerez&idevento=-1&pic=b4.png&pic_w=300" alt="Actividades" class="img-rounded"></div>
							<div style="position: absolute; width:299px; bottom: 0px;" class="btn-group">
								<button type="button" class="btn btn-default" style="width: 100%; height: 40px;">{{ control.num_documentos }} documentos</button>
	  						</div>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>

