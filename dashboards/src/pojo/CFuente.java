package pojo;

public class CFuente {
	private Integer ejercicio;
	private Integer fuente;
	private String nombre;
	
	public CFuente(Integer ejercicio, Integer fuente, String nombre) {
		super();
		this.ejercicio = ejercicio;
		this.setFuente(fuente);
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

	public Integer getFuente() {
		return fuente;
	}

	public void setFuente(Integer fuente) {
		this.fuente = fuente;
	}
	
}
