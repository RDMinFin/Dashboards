<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div ng-controller="metasController as control" class="maincontainer" id="title" class="all_page">
	<h3>Metas Presidenciales</h3>
	<br/>
	<h4 style="width: 300px;">Ejecución Física y Financiera</h4>
	<br/>
	<div class="row">
		<div class="col-sm-12 col-centered">
			<table st-table="control.metas_entidades" st-safe-src="control.original_metas_entidades" class="table table-striped">
				<thead>
					<tr>
						<th>Entidad</th>
						<th style="text-align: center;">Asignado (Metas)</th>
						<th style="text-align: center;">Modificaciones (Metas)</th>
						<th style="text-align: center;">Vigente (Metas)</th>
						<th style="text-align: center;">Vigente (Entidad)</th>
						<th style="text-align: center;">Ejecutado Acumulado (Metas)</th>
						<th style="text-align: center;">% Ejecución Financiera (Metas)</th>
						<th style="text-align: center;">% Ejecución Física (Metas)</th>
						<th style="text-align: center;">Indice Alineamiento</th>
					</tr>
				</thead>
				<tbody style="white-space: nowrap;">
					<tr ng-repeat="row in control.metas_entidades">
						<td>{{ row.nombre }}</td>
						<td style="text-align: right;">{{ row.asignado | currency:"Q&nbsp;":2 }}</td>
						<td style="text-align: right;">{{ row.modificaciones | currency:"Q&nbsp;":2 }}</td>
						<td style="text-align: right;">{{ row.vigente | currency:"Q&nbsp;":2 }}</td>
						<td style="text-align: right;">{{ row.vigente_entidad | currency:"Q&nbsp;":2 }}</td>
						<td style="text-align: right;">{{ row.gasto | currency:"Q&nbsp;":2 }}</td>
						<td style="text-align: right;">{{ (row.gasto / row.vigente) | number:2 }}&nbsp;%</td>
						<td style="text-align: right;">{{ (row.avance / row.meta) | number:2 }}&nbsp;%</td>
						<td style="text-align: center;"><span class="glyphicon glyphicon glyphicon-flag dot_{{ row.indice_alineamiento }}"></span></td>
					</tr>
				</tbody>
				<tfoot style="font-weight: bold;">
					<tr>
						<td style="text-align: right;">Totales</td>
						<td style="text-align: right;">{{ control.totales_entidades[0] | currency:"Q&nbsp;":2 }}</td>
						<td style="text-align: right;">{{ control.totales_entidades[1] | currency:"Q&nbsp;":2 }}</td>
						<td style="text-align: right;">{{ control.totales_entidades[2] | currency:"Q&nbsp;":2 }}</td>
						<td style="text-align: right;">{{ control.totales_entidades[6] | currency:"Q&nbsp;":2 }}</td>
						<td style="text-align: right;">{{ control.totales_entidades[3] | currency:"Q&nbsp;":2 }}</td>
						<td style="text-align: right;">{{ ((control.totales_entidades[3]/control.totales_entidades[2])*100) | number:2  }}&nbsp;%</td>
						<td style="text-align: right;">{{ ((control.totales_entidades[4]/control.totales_entidades[5])*100) | number:2  }}&nbsp;%</td>
						<td style="text-align: center;"><span class="glyphicon glyphicon glyphicon-flag dot_{{ control.indice_alineamiento_total }}"></span></td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
	<br>
	<div class="row">
		<div class="col-sm-12 col-centered">
			<div class="panel panel-default div-center" style="width: 650px;">
					<h5 class="text-center">Ejecución Financiera</h5>
					<h6 class="text-center">-Millones de quetzales-</h6>
					<div style="width: 600px; height: 350px;" class="div-center">
						<canvas id="ejecucion_entidades" height="350" width="600" class="chart chart-bar" chart-data="control.chart_ejecucion_metas['data']"
											  chart-labels="control.chart_ejecucion_metas['labels']" chart-legend="false" chart-series="control.chart_ejecucion_metas['series']" 
											  chart-options="control.chart_ejecucion_metas['options']">
						</canvas>
					</div>
					<div class="chart-legend">
							<ul class="line-legend">
								<li ng-repeat="serie in control.chart_ejecucion_metas['series'] track by $index">
								<div class="img-rounded" style="float: left; margin-right: 5px; width: 15px; height: 15px; background-color : {{ control.chart_colors[$index] }};"></div>
									{{ control.chart_ejecucion_metas['series'][$index] }} 
								</li>
							</ul>
					</div>
				</div>
			</div>
	</div>
	<br/><br/>
	<div class="row">
		<div class="col-sm-12 col-centered">
			<table st-table="control.metas_metas" st-safe-src="control.original_metas_metas" class="table table-striped">
				<thead>
					<tr>
						<th>Meta</th>
						<th style="text-align: center;">Asignado</th>
						<th style="text-align: center;">Modificaciones</th>
						<th style="text-align: center;">Ejecutado Acumulado</th>
						<th style="text-align: center;">Vigente</th>
						<th style="text-align: center;">% Ejecución Financiera</th>
						<th style="text-align: center;">% Ejecución Física</th>
					</tr>
				</thead>
				<tbody style="white-space: nowrap;">
					<tr ng-repeat="row in control.metas_metas">
						<td>{{ row.nombre }}</td>
						<td style="text-align: right;">{{ row.asignado | currency:"Q&nbsp;":2 }}</td>
						<td style="text-align: right;">{{ row.modificaciones | currency:"Q&nbsp;":2 }}</td>
						<td style="text-align: right;">{{ row.vigente | currency:"Q&nbsp;":2 }}</td>
						<td style="text-align: right;">{{ row.gasto | currency:"Q&nbsp;":2 }}</td>
						<td style="text-align: right;">{{ (row.gasto / row.vigente) | number:2 }}&nbsp;%</td>
						<td style="text-align: right;">{{ (row.avance / row.meta) | number:2 }}&nbsp;%</td>
					</tr>
				</tbody>
				<tfoot style="font-weight: bold;">
					<tr>
						<td style="text-align: right;">Totales</td>
						<td style="text-align: right;">{{ control.totales_metas[0] | currency:"Q&nbsp;":2 }}</td>
						<td style="text-align: right;">{{ control.totales_metas[1] | currency:"Q&nbsp;":2 }}</td>
						<td style="text-align: right;">{{ control.totales_metas[2] | currency:"Q&nbsp;":2 }}</td>
						<td style="text-align: right;">{{ control.totales_metas[3] | currency:"Q&nbsp;":2 }}</td>
						<td style="text-align: right;">{{ ((control.totales_metas[3]/control.totales_metas[2])*100) | number:2  }}&nbsp;%</td>
						<td style="text-align: right;">{{ ((control.totales_metas[4]/control.totales_metas[5])*100) | number:2  }}&nbsp;%</td>
					</tr>	
				</tfoot>
			</table>
		</div>
	</div>
	<br/>
	<div class="row">
		<div class="col-sm-12 col-centered">
			<div class="panel panel-default div-center" style="width: 650px;">
					<h5 class="text-center">Ejecución Financiera y Financiera</h5>
					<h6 class="text-center">-% de Avance-</h6>
					<div style="width: 600px; height: 350px;" class="div-center">
						<canvas id="ejecucion_metas" height="350" width="600" class="chart chart-line" chart-data="control.chart_metasff['data']"
											  chart-labels="control.chart_metasff['labels']" chart-legend="false" chart-series="control.chart_metasff['series']" 
											  chart-options="control.chart_metasff['options']">
						</canvas>
					</div>
					<div class="chart-legend">
							<ul class="line-legend">
								<li ng-repeat="serie in control.chart_metasff['series'] track by $index">
								<div class="img-rounded" style="float: left; margin-right: 5px; width: 15px; height: 15px; background-color : {{ control.chart_colors[$index] }};"></div>
									{{ control.chart_metasff['series'][$index] }} 
								</li>
							</ul>
					</div>
				</div>
			</div>
	</div>
	<br/>
	<div class="row">
		<div class="col-sm-12">
			<div class="col-sm-6">Última actualización: {{ control.lastupdate }}</div>
		</div>
	</div>
</div>