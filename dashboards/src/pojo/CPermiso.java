package pojo;

import java.util.ArrayList;

public class CPermiso {
	private Integer id;
	private String nombre;
	private ArrayList<Integer> roles;
	
	public CPermiso(Integer id, String nombre, ArrayList<Integer> roles){
		this.id = id;
		this.nombre = nombre;
		this.roles = roles;
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
	
	public ArrayList<Integer> getRoles(){
		return roles;
	}
	
	public void setRoles(ArrayList<Integer> roles){
		this.roles = roles;
	}
}
