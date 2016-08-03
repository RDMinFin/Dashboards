package pojo;

public class CGrupoGasto {
	private Integer ejercicio;
	private Integer grupo;
	private String nombre;
	
	public CGrupoGasto(Integer ejercicio, Integer grupoGasto, String nombre) {
		super();
		this.ejercicio = ejercicio;
		this.setGrupoGasto(grupoGasto);
		this.nombre = nombre;
	}
	
	public Integer getEjercicio() {
		return ejercicio;
	}
	
	public void setEjercicio(Integer ejercicio) {
		this.ejercicio = ejercicio;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getGrupoGasto() {
		return grupo;
	}

	public void setGrupoGasto(Integer Grupo) {
		this.grupo = Grupo;
	}
}
