<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div ng-controller="paptn_ejecucionfinancieraController as ejecucion" class="maincontainer" id="title" class="all_page">
	<h3>Plan de la Alianza para la Prosperidad del Tríangulo Norte</h3>
	<br/>
	<h4 style="width: 300px;">Ejecución Financiera</h4>
	<br/>
	<div class="row">
		<div class="col-sm-12 col-centered">
			<h5 class="text-center">Ejecución de PAPTN según eje estratégico y linea de acción</h5>
			<br/>
			<table class="table table-hover" style="width: 90%; margin: 0 auto;">
				<thead>
					<tr>
						<th>Ejes Estratégicos</th>
						<th>Presupuesto Aprobado</th>
						<th>Modificaciones</th>
						<th>Vigente</th>
						<th>Ejecución</th>
						<th>% de Ejecución</th>
					</tr>
				</thead>
				<tbody>
					<tr ng-repeat="entidad in ejecucion.tabla_ejes track by $index" style="{{ ejecucion.tabla_ejes[$index].eje_nombre!=null ? 'font-weight: bold;' : '' }}">
						<td class="text-nowrap" ng-if="ejecucion.tabla_ejes[$index].eje_nombre!=null">{{ ejecucion.tabla_ejes[$index].eje_nombre }} {{ ejecucion.tabla_ejes[$index].eje==3 ? ' * ' : ''}}</td>
						<td class="text-nowrap" ng-if="ejecucion.tabla_ejes[$index].eje_nombre==null">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{ ejecucion.tabla_ejes[$index].linea_nombre }}</td>
						<td class="text-right">{{ ejecucion.tabla_ejes[$index].aprobado | currency:"Q&nbsp;":2 }}</td>
						<td class="text-right">{{ ejecucion.tabla_ejes[$index].modificaciones | currency:"Q&nbsp;":2  }}</td>
						<td class="text-right">{{ ejecucion.tabla_ejes[$index].vigente | currency:"Q&nbsp;":2  }}</td>
						<td class="text-right">{{ ejecucion.tabla_ejes[$index].ejecucion | currency:"Q&nbsp;":2  }}</td>
						<td class="text-center">{{ ejecucion.tabla_ejes[$index].porcentaje | number:2}}&nbsp;%</td>
					</tr>
					<tr style="font-weight: bold;">
						<td class="text-right"><strong>Totales</strong></td>
						<td class="text-right"><strong>{{ ejecucion.tabla_ejes_totales[0] | currency:"Q&nbsp;":2  }}</strong></td>
						<td class="text-right"><strong>{{ ejecucion.tabla_ejes_totales[1] | currency:"Q&nbsp;":2 }}</strong></td>
						<td class="text-center"><strong>{{ ejecucion.tabla_ejes_totales[2] | currency:"Q&nbsp;":2 }}</strong></td>
						<td class="text-right"><strong>{{ ejecucion.tabla_ejes_totales[3] | currency:"Q&nbsp;":2 }}</strong></td>
						<td class="text-center"><strong>{{ ((ejecucion.tabla_ejes_totales[3]/ejecucion.tabla_ejes_totales[2])*100)  | number:2}}&nbsp;%</strong></td>
					</tr>
				</tbody>
			</table>
			<div style="width: 90%; margin: 0 auto;">(*) Excluye el sector justicia a cargo de: OJ, MP, CC, IDPP, INACIF y SEICMSJ</div>
		</div>
	</div>
	<br/><br/>
	<div class="row">
		<div class="col-sm-12 col-centered">
			<div class="panel panel-default div-center" style="width: 650px;">
				<h5 class="text-center">Ejecución por Eje Estratégico</h5>
				<h6 class="text-center">-Millones de quetzales-</h6>
				<div style="width: 600px; height: 350px;" class="div-center">
					<canvas id="ejecucion_ejes" height="350" width="600" class="chart chart-bar" chart-data="ejecucion.chart_eje_estrategico['data']"
										  chart-labels="ejecucion.chart_eje_estrategico['labels']" chart-legend="false" chart-series="ejecucion.chart_eje_estrategico['series']" 
										  chart-options="ejecucion.chart_eje_estrategico['options']"
										  chart-click="ejecucion.onClick_chart_eje_estrategico">
					</canvas>
				</div>
				<div class="chart-legend">
						<ul class="line-legend">
							<li ng-repeat="year in ejecucion.chart_eje_estrategico['series'] track by $index">
							<div class="img-rounded" style="float: left; margin-right: 5px; width: 15px; height: 15px; background-color : {{ ejecucion.chart_colors[$index] }};"></div>
								{{ ejecucion.chart_eje_estrategico['series'][$index] }} 
							</li>
						</ul>
				</div>
			</div>
		</div>
	</div>
	<br/><br/>
	<div class="row">
		<div class="col-sm-12">
			<h5 class="text-center">Ejecución Institucional general y del PAPTN</h5>
			<br/>
			<table class="table table-hover" style="width: 90%; margin: 0 auto;">
				<thead>
					<tr>
						<th rowspan="2">Entidad</th>
						<th colspan="3" class="text-center">Gasto Público</th>
						<th colspan="3" class="text-center">Asociado al PAPTN</th>
					</tr>
					<tr>
						<th class="text-center">Presupuesto Vigente</th>
						<th class="text-center">Ejecución</th>
						<th class="text-center">% de Ejecución</th>
						<th class="text-center">Presupuesto Vigente</th>
						<th class="text-center">Ejecución</th>
						<th class="text-center">% de Ejecución</th>
					</tr>
				</thead>
				<tbody>
					<tr ng-repeat="entidad in ejecucion.tabla_entidades track by $index">
						<td class="text-nowrap">{{ ejecucion.tabla_entidades[$index].nombre }} {{ ejecucion.tabla_entidades[$index].entidad==11130016 ? ' **' : '' }}</td>
						<td class="text-right">{{ ejecucion.tabla_entidades[$index].gp_vigente | currency:"Q&nbsp;":2 }}</td>
						<td class="text-right">{{ ejecucion.tabla_entidades[$index].gp_ejecucion | currency:"Q&nbsp;":2  }}</td>
						<td class="text-center">{{ ejecucion.tabla_entidades[$index].gp_porcentaje | number:2}}&nbsp;%</td>
						<td class="text-right">{{ ejecucion.tabla_entidades[$index].paptn_vigente>0 ? (ejecucion.tabla_entidades[$index].paptn_vigente | currency:"Q&nbsp;":2)  : '--' }}</td>
						<td class="text-right">{{ ejecucion.tabla_entidades[$index].paptn_ejecucion>0 ? (ejecucion.tabla_entidades[$index].paptn_ejecucion | currency:"Q&nbsp;":2) : '--'  }}</td>
						<td class="text-center">{{ ejecucion.tabla_entidades[$index].paptn_porcentaje>0 ? (ejecucion.tabla_entidades[$index].paptn_porcentaje | number:2)+'&nbsp;%' : '--'}}</td>
					</tr>
					<tr>
						<td class="text-right"><strong>Totales</strong></td>
						<td class="text-right"><strong>{{ ejecucion.tabla_entidades_totales[0] | currency:"Q&nbsp;":2  }}</strong></td>
						<td class="text-right"><strong>{{ ejecucion.tabla_entidades_totales[1] | currency:"Q&nbsp;":2 }}</strong></td>
						<td class="text-center"><strong>{{ (ejecucion.tabla_entidades_totales[1]/ejecucion.tabla_entidades_totales[0])*100 | number:2 }}&nbsp;%</strong></td>
						<td class="text-right"><strong>{{ ejecucion.tabla_entidades_totales[3] | currency:"Q&nbsp;":2 }}</strong></td>
						<td class="text-right"><strong>{{ ejecucion.tabla_entidades_totales[4] | currency:"Q&nbsp;":2 }}</strong></td>
						<td class="text-center"><strong>{{ ((ejecucion.tabla_entidades_totales[4]/ejecucion.tabla_entidades_totales[3])*100)  | number:2}}&nbsp;%</strong></td>
					</tr>
				</tbody>
			</table>
			<div style="width: 90%; margin: 0 auto;">(**) La ejecución del PAPTN solo hace referencia a la ejecución de la Secreataría contra la violencia sexual, explotación y trata de personas</div>
		</div>
	</div>
	<br/><br/>
	<div class="row">
		<div class="col-sm-6">
			<div class="panel panel-default div-center" style="width: 600px; height: 450px;">
				<h5 class="text-center">Ejecución Institucional total y de PAPTN</h5>
				<h6 class="text-center">-Millones de quetzales-</h6>
				<div style="width: 400px; height: 350px;" class="div-center">
					<canvas width="400" height="350" class="chart chart-bar" chart-data="ejecucion.chart_entidades['data']"
						chart-labels="ejecucion.chart_entidades['labels']" chart-legend="false" chart-series="ejecucion.chart_entidades['series']" chart-options="ejecucion.chart_entidades['options']">
					</canvas>
				</div>
			</div>
		</div>
		<div class="col-sm-6">
			<div class="panel panel-default div-center" style="width: 600px; height: 450px;">
				<h5 class="text-center">Estructura del financiamiento del PAPTN</h5>
				<h6 class="text-center">-Presupuesto Vigente-</h6>
				<div style="width: 380px; height: 330px;" class="div-center">
				<canvas height="250" width="300" class="chart chart-pie" chart-data="ejecucion.chart_financiamiento['data']"
					chart-labels="ejecucion.chart_financiamiento['labels']" chart-legend="false" chart-series="ejecucion.chart_financiamiento['series']" chart-options="ejecucion.chart_financiamiento['options']">
				</canvas>
				</div>
				<div class="chart-legend">
					<ul class="line-legend">
						<li ng-repeat="year in ejecucion.chart_financiamiento['labels'] track by $index">
						<div class="img-rounded" style="float: left; margin-right: 5px; width: 15px; height: 15px; background-color : {{ ejecucion.chart_colors[$index] }};"></div>
							{{ ejecucion.chart_financiamiento['labels'][$index] }} 
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<br/>
	<div class="row">
		<div class="col-sm-12">
			<div class="col-sm-6">Última actualización: {{ ejecucion.lastupdate }}</div>
		</div>
	</div>
</div>