package pojo.transparencia;

import java.sql.Timestamp;

public class CDocumento {
	int id;
	int id_actividad;
	String titulo;
	String nombre;
	String ruta;
	int tipo;
	Timestamp fecha_creacion;
	String usuario_creacion;
	
	public CDocumento(int id, int id_actividad,String nombre,String titulo, String ruta, int tipo, Timestamp fecha_creacion, String usuario_creacion) {
		super();
		this.id = id;
		this.id_actividad = id_actividad;
		this.nombre = nombre;
		this.titulo = titulo;
		this.ruta = ruta;
		this.tipo = tipo;
		this.fecha_creacion = fecha_creacion;
		this.usuario_creacion = usuario_creacion;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_actividad() {
		return id_actividad;
	}
	public void setId_actividad(int id_actividad) {
		this.id_actividad = id_actividad;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getNombre(){
		return nombre;
	}
	
	public void setNombre(String nombre){
		this.nombre = nombre;
	}
	
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public Timestamp getFecha_creacion() {
		return fecha_creacion;
	}
	public void setFecha_creacion(Timestamp fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}

	public String getUsuario_creacion() {
		return usuario_creacion;
	}

	public void setUsuario_creacion(String usuario_creacion) {
		this.usuario_creacion = usuario_creacion;
	}
	
	
}
