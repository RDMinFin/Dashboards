package pojo;

public class CMunicipio {
	Integer codigo;
	String nombre_mayusculas;
	String nombre;
	String latitud;
	String logitud;
	int codigo_departamento;
	
	public CMunicipio(Integer codigo, String nombre_caps, String nombre, String latitud, String logitud, int codigo_departamento) {
		this.codigo = codigo;
		this.nombre_mayusculas = nombre_caps;
		this.nombre = nombre;
		this.latitud = latitud;
		this.logitud = logitud;
		this.codigo = codigo;
		this.codigo_departamento = codigo_departamento;
	}

	public Integer getId() {
		return codigo;
	}

	public void setId(Integer id) {
		this.codigo = id;
	}

	public String getNombre_mayuscula() {
		return nombre_mayusculas;
	}

	public void setNombre_mayuscula(String nombre_mayuscula) {
		this.nombre_mayusculas = nombre_mayuscula;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getLatitud() {
		return latitud;
	}

	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}

	public String getLongitud() {
		return logitud;
	}

	public void setLongitud(String logitud) {
		this.logitud = logitud;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getCodigo_departamento() {
		return codigo_departamento;
	}

	public void setCodigo_departamento(int codigo_departamento) {
		this.codigo_departamento = codigo_departamento;
	} 
	
	
}
