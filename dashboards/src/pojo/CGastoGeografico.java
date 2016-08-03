package pojo;

public class CGastoGeografico {
	private Integer ejercicio;
	private Integer geografico;
	private Integer mes;
	private Integer puntos;
	private String latitud;
	private String longitud;
	
	public CGastoGeografico(Integer ejercicio, Integer geografico, Integer mes, Integer puntos, String latitud,
			String longitud) {
		this.ejercicio = ejercicio;
		this.geografico = geografico;
		this.mes = mes;
		this.puntos = puntos;
		this.latitud = latitud;
		this.longitud = longitud;
	}

	public Integer getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(Integer ejercicio) {
		this.ejercicio = ejercicio;
	}

	public Integer getGeografico() {
		return geografico;
	}

	public void setGeografico(Integer geografico) {
		this.geografico = geografico;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getPuntos() {
		return puntos;
	}

	public void setPuntos(Integer puntos) {
		this.puntos = puntos;
	}

	public String getLatitud() {
		return latitud;
	}

	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}

	public String getLongitud() {
		return longitud;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
	
	
	
}
