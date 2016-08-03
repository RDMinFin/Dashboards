package pojo;

public class CEjecucion {

	private Integer parent;
	private Integer entidad;
	private String nombre;
	private String nombre_2;
	private String nombre_3;
	private Double ano1;
	private Double ano2;
	private Double ano3;
	private Double ano4;
	private Double ano5;
	private Double cierre_estimado;
	private Double solicitado;
	private Double solicitado_acumulado;
	private Double aprobado;
	private Double aprobado_sin_anticipo;
	private Double anticipo;
	private Double aprobado_acumulado;
	private Double ejecutado;
	private Double ejecutado_acumulado;
	private Double vigente;
	
	public CEjecucion(Integer parent, Integer entidad, String nombre, Double ano1, Double ano2, Double ano3, Double ano4, Double ano5,
			Double cierre_estimado, Double aprobado, Double aprobado_acumulado,Double ejecutado, Double ejecutado_acumulado,
			Double vigente) {
		super();
		this.parent = parent;
		this.entidad = entidad;
		this.nombre = nombre;
		this.ano1 = ano1;
		this.ano2 = ano2;
		this.ano3 = ano3;
		this.ano4 = ano4;
		this.ano5 = ano5;
		this.cierre_estimado = cierre_estimado;
		this.aprobado = aprobado;
		this.aprobado_acumulado = aprobado_acumulado;
		this.ejecutado = ejecutado;
		this.ejecutado_acumulado = ejecutado_acumulado;
		this.vigente = vigente;
	}
	// para el acumulado
	public CEjecucion(Integer parent, Integer entidad, String nombre, Double ano1, Double ano2, Double ano3, Double ano4, Double ano5,
			Double cierre_estimado,  Double aprobado_sin_anticipo, Double anticipo, Double aprobado_acumulado, Double solicitado_acumulado, Double ejecutado_acumulado, Double vigente) {
		super();
		this.parent = parent;
		this.entidad = entidad;
		this.nombre = nombre;
		this.ano1 = ano1;
		this.ano2 = ano2;
		this.ano3 = ano3;
		this.ano4 = ano4;
		this.ano5 = ano5;
		this.cierre_estimado = null;
		this.solicitado = null;
		this.solicitado_acumulado = solicitado_acumulado;
		this.aprobado = null;
		this.aprobado_acumulado = aprobado_acumulado;
		this.ejecutado = null;
		this.ejecutado_acumulado = ejecutado_acumulado;
		this.vigente = vigente;
		this.aprobado_sin_anticipo = aprobado_sin_anticipo;
		this.anticipo = anticipo;
	}
	
	public CEjecucion(Integer parent, Integer entidad, String nombre, String nombre_2, String nombre_3,Double ano1, Double ano2, Double ano3, Double ano4, Double ano5,
			Double cierre_estimado, Double aprobado, Double aprobado_acumulado,Double ejecutado, Double ejecutado_acumulado,
			Double vigente) {
		super();
		this.parent = parent;
		this.entidad = entidad;
		this.nombre = nombre;
		this.nombre_2 = nombre_2;
		this.nombre_3 = nombre_3;
		this.ano1 = ano1;
		this.ano2 = ano2;
		this.ano3 = ano3;
		this.ano4 = ano4;
		this.ano5 = ano5;
		this.cierre_estimado = cierre_estimado;
		this.aprobado = aprobado;
		this.aprobado_acumulado = aprobado_acumulado;
		this.ejecutado = ejecutado;
		this.ejecutado_acumulado = ejecutado_acumulado;
		this.vigente = vigente;
	}
	
	public Integer getParent(){
		return parent;
	}
	
	public void setParent(Integer parent){
		this.parent = parent;
	}

	public Integer getEntidad() {
		return entidad;
	}

	public void setEntidad(Integer entidad) {
		this.entidad = entidad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre_2() {
		return nombre_2;
	}

	public void setNombre_2(String nombre_2) {
		this.nombre_2 = nombre_2;
	}

	public String getNombre_3() {
		return nombre_3;
	}

	public void setNombre_3(String nombre_3) {
		this.nombre_3 = nombre_3;
	}

	public Double getAno1() {
		return ano1;
	}

	public void setAno1(Double ano1) {
		this.ano1 = ano1;
	}

	public Double getAno2() {
		return ano2;
	}

	public void setAno2(Double ano2) {
		this.ano2 = ano2;
	}

	public Double getAno3() {
		return ano3;
	}

	public void setAno3(Double ano3) {
		this.ano3 = ano3;
	}

	public Double getAno4() {
		return ano4;
	}

	public void setAno4(Double ano4) {
		this.ano4 = ano4;
	}

	public Double getAno5() {
		return ano5;
	}

	public void setAno5(Double ano5) {
		this.ano5 = ano5;
	}

	public Double getCierre_estimado() {
		return cierre_estimado;
	}

	public void setCierre_estimado(Double cierre_estimado) {
		this.cierre_estimado = cierre_estimado;
	}

	public Double getSolicitado() {
		return solicitado;
	}

	public void setSolicitado(Double solicitado) {
		this.solicitado = solicitado;
	}
	
	public Double getSolicitado_acumulado() {
		return solicitado_acumulado;
	}

	public void setSolicitado_acumulado(Double solicitado_acumulado) {
		this.solicitado_acumulado = solicitado_acumulado;
	}
	
	public Double getAprobado() {
		return aprobado;
	}

	public void setAprobado(Double aprobado) {
		this.aprobado = aprobado;
	}
	
	public Double getAprobado_sin_anticipo() {
		return aprobado_sin_anticipo;
	}

	public void setAprobado_sin_anticipo(Double aprobado_sin_anticipo) {
		this.aprobado_sin_anticipo = aprobado_sin_anticipo;
	}
	
	public Double getAnticipo() {
		return anticipo;
	}

	public void setAnticipo(Double anticipo) {
		this.anticipo = anticipo;
	}
	
	public Double getAprobado_acumulado() {
		return aprobado_acumulado;
	}

	public void setAprobado_acumulado(Double aprobado_acumulado) {
		this.aprobado_acumulado = aprobado_acumulado;
	}

	public Double getEjecutado() {
		return ejecutado;
	}

	public void setEjecutado(Double ejecutado) {
		this.ejecutado = ejecutado;
	}

	public Double getEjecutado_acumulado() {
		return ejecutado_acumulado;
	}

	public void setEjecutado_acumulado(Double ejecutado_acumulado) {
		this.ejecutado_acumulado = ejecutado_acumulado;
	}

	public Double getVigente() {
		return vigente;
	}

	public void setVigente(Double vigente) {
		this.vigente = vigente;
	}
	
}
