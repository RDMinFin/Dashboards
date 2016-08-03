<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <nav ng-class="{'showbar' : !hidebar, 'hidebar': hidebar}" class="navbar navbar-inverse navbar-fixed-top">
	    <div class="container">
	        <input type="checkbox" id="navbar-toggle-cbox">
	        <div class="navbar-header">
	            <label for="navbar-toggle-cbox" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
	                <span class="sr-only">Toggle navigation</span>
	                <span class="icon-bar"></span>
	                <span class="icon-bar"></span>
	                <span class="icon-bar"></span>
	            </label>
	            <a class="navbar-brand" href="/main.jsp"><span class="glyphicon glyphicon-home" aria-hidden="true"></span> Inicio</a>
	            <ul class="nav navbar-nav">
	                <li uib-dropdown>
	                    <a href="#" uib-dropdown-toggle><span class="glyphicon glyphicon-stats" aria-hidden="true"></span> Tableros <b class="caret"></b></a>
	                     <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
	                     	<li role="menuitem"><a href="#!/dashboards/ejecucionpresupuestaria">Ejecución Presupuestaria</a></li>
	                     	<li role="menuitem"><a href="#!/dashboards/ejecucionrenglon">Ejecución Presupuestaria por Renglón</a></li>
	                     	<li role="menuitem"><a href="#!/dashboards/ejecucionprograma">Ejecución programática (Entidad)</a></li>
	                     	<li role="menuitem"><a href="#!/dashboards/ejecucionprogramaUE">Ejecución programática (Unidad Ejecutora) </a></li>
	                     	<li role="menuitem"><a href="#!/dashboards/copep">Cuotas COPEP</a></li>
	                     	<li role="menuitem"><a href="#!/dashboards/copeprenglon">Cuotas COPEP por Renglón</a></li>
	                     	<!--  <li role="menuitem"><a href="#!/dashboards/proyecciongasto">Test</a></li> -->
	                        <!--  <li role="menuitem"><a href="#">Another action</a></li>
	                        <li role="menuitem"><a href="#">Something else here</a></li>
	                        <li class="divider"></li>
	                        <li role="menuitem"><a href="#">Separated link</a></li>  -->
	                    </ul>
	                </li>
	                <li uib-dropdown>
	                    <a href="#" uib-dropdown-toggle><span class="glyphicon glyphicon glyphicon-flag" aria-hidden="true"></span>Metas Presidenciales <b class="caret"></b></a>
	                     <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
	                     	<li role="menuitem"><a href="#!/presidenciales/metas">Metas Presidenciales</a></li>
	                        <!--  <li role="menuitem"><a href="#">Another action</a></li>
	                        <li role="menuitem"><a href="#">Something else here</a></li>
	                        <li class="divider"></li>
	                        <li role="menuitem"><a href="#">Separated link</a></li>  -->
	                    </ul>
	                </li>
	                <li uib-dropdown>
	                    <a href="#" uib-dropdown-toggle><span class="glyphicon glyphicon-search" aria-hidden="true"></span>Transparencia <b class="caret"></b></a>
	                     <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
	                     	<li role="menuitem"><a href="#!/transparencia/jerez">Jerez, Jutiapa</a></li>
	                        <!--  <li role="menuitem"><a href="#">Another action</a></li>
	                        <li role="menuitem"><a href="#">Something else here</a></li>
	                        <li class="divider"></li>
	                        <li role="menuitem"><a href="#">Separated link</a></li>  -->
	                    </ul>
	                </li>
	                <li uib-dropdown>
	                    <a href="#" uib-dropdown-toggle><span class="glyphicon glyphicon-link" aria-hidden="true"></span>PAPTN <b class="caret"></b></a>
	                     <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
	                     	<li role="menuitem"><a href="#!/paptn/ejecucionfinanciera">Ejecución Financiera</a></li>
	                        <!--  <li role="menuitem"><a href="#">Another action</a></li>
	                        <li role="menuitem"><a href="#">Something else here</a></li>
	                        <li class="divider"></li>
	                        <li role="menuitem"><a href="#">Separated link</a></li>  -->
	                    </ul>
	                </li>
	                <!-- <li class="active"><a href="/view1">Home</a></li>
	                <li><a href="/view2">About</a></li>
	                <li><a href="#">Contact</a></li>  -->
	                <li uib-dropdown>
	                    <a href="#" uib-dropdown-toggle><span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span> Mapas <b class="caret"></b></a>
	                     <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
	                        <li role="menuitem"><a href="#!/maps/geograficogasto">Mapa de Calor del Gasto</a></li>
	                        <!--  <li role="menuitem"><a href="#">Another action</a></li>
	                        <li role="menuitem"><a href="#">Something else here</a></li>
	                        <li class="divider"></li>
	                        <li role="menuitem"><a href="#">Separated link</a></li>  -->
	                    </ul>
	                </li>
	            </ul>
	        </div>
	        <div class="collapse navbar-collapse">
	        	<ul class="nav navbar-nav navbar-right">
		          <li><a href="/SLogout"><span class="glyphicon glyphicon-log-out" aria-hidden="true"></span> Salir</a></li>
		          <li><a><span class="glyphicon glyphicon-chevron-up" aria-hidden="true" ng-click="hideBarFromMenu()"></span> </a></li>
		        </ul>
	        </div>
	    </div>
	</nav>