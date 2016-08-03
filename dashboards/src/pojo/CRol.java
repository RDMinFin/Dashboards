package pojo;

import java.util.ArrayList;

public class CRol {
	private Integer id;
	private String nombre;
	private ArrayList<Integer> permisos;
	
	public CRol(Integer id, String nombre, ArrayList<Integer> permisos){
		this.id = id;
		this.nombre = nombre;
		this.permisos = permisos;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public ArrayList<Integer> getPermisos(){
		return permisos;
	}
	
	public void setPermissions(ArrayList<Integer> permisos){
		this.permisos = permisos;
	}
}
