package pojo.metas;

public class CMeta {
	private int id;
	private String nombre;
	private Double asignado;
	private Double vigente;
	private Double gasto;
	private Double modificaciones;
	private Long avance;
	private Long meta;
	private Double vigente_entidad;
	private int nivel;
	
	public CMeta(int id, String nombre, Double asignado, Double vigente, Double gasto, Double modificaciones,
			Long avance, Long meta, Double vigente_entidad, int nivel) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.asignado = asignado;
		this.vigente = vigente;
		this.gasto = gasto;
		this.modificaciones = modificaciones;
		this.avance = avance;
		this.meta = meta;
		this.vigente_entidad = vigente_entidad;
		this.nivel = nivel;
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
	
	public Double getAsignado() {
		return asignado;
	}

	public void setAsignado(Double asignado) {
		this.asignado = asignado;
	}

	public Double getVigente() {
		return vigente;
	}

	public void setVigente(Double vigente) {
		this.vigente = vigente;
	}

	public Double getGasto() {
		return gasto;
	}

	public void setGasto(Double gasto) {
		this.gasto = gasto;
	}


	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public Long getAvance() {
		return avance;
	}

	public void setAvance(Long avance) {
		this.avance = avance;
	}

	public Long getMeta() {
		return meta;
	}

	public void setMeta(Long meta) {
		this.meta = meta;
	}

	public Double getModificaciones() {
		return modificaciones;
	}

	public void setModificaciones(Double modificaciones) {
		this.modificaciones = modificaciones;
	}

	public Double getVigente_entidad() {
		return vigente_entidad;
	}

	public void setVigente_entidad(Double vigente_entidad) {
		this.vigente_entidad = vigente_entidad;
	}
	
}
