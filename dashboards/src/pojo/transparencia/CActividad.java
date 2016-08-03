package pojo.transparencia;

import java.sql.Timestamp;
import java.util.ArrayList;

public class CActividad {
	int id;
	String nombre;
	String descripcion;
	Timestamp fecha_inicio;
	Timestamp fecha_fin;
	double porcentaje_ejecucion;
	String coord_lat;
	String coord_long;
	String entidades;
	CResponsable responsable;
	CEstructura estructura;
	ArrayList<CDocumento> documentos;
	
	public CActividad(int id, String nombre, String descripcion, Timestamp fecha_inicio, Timestamp fecha_fin,
			double porcentaje_ejecucion, String coord_lat, String coord_long, String entidades,
			CResponsable responsable, CEstructura estructura, ArrayList<CDocumento> documentos) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
		this.porcentaje_ejecucion = porcentaje_ejecucion;
		this.coord_lat = coord_lat;
		this.coord_long = coord_long;
		this.entidades = entidades;
		this.responsable = responsable;
		this.estructura = estructura;
		this.documentos = documentos;
	}

	public CActividad(int id, double porcentaje_ejecucion) {
		super();
		this.id = id;
		this.porcentaje_ejecucion = porcentaje_ejecucion;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Timestamp getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(Timestamp fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public Timestamp getFecha_fin() {
		return fecha_fin;
	}

	public void setFecha_fin(Timestamp fecha_fin) {
		this.fecha_fin = fecha_fin;
	}

	public double getPorcentaje_ejecucion() {
		return porcentaje_ejecucion;
	}

	public void setPorcentaje_ejecucion(double porcentaje_ejecucion) {
		this.porcentaje_ejecucion = porcentaje_ejecucion;
	}

	public String getCoord_lat() {
		return coord_lat;
	}

	public void setCoord_lat(String coord_lat) {
		this.coord_lat = coord_lat;
	}

	public String getCoord_long() {
		return coord_long;
	}

	public void setCoord_long(String coord_long) {
		this.coord_long = coord_long;
	}

	public String getEntidades() {
		return entidades;
	}

	public void setEntidades(String entidades) {
		this.entidades = entidades;
	}

	public CResponsable getResponsable() {
		return responsable;
	}

	public void setResponsable(CResponsable responsable) {
		this.responsable = responsable;
	}

	public CEstructura getEstructura() {
		return estructura;
	}

	public void setEstructura(CEstructura estructura) {
		this.estructura = estructura;
	}

	public ArrayList<CDocumento> getDocumentos() {
		return documentos;
	}

	public void setDocumentos(ArrayList<CDocumento> documentos) {
		this.documentos = documentos;
	}


	
}
