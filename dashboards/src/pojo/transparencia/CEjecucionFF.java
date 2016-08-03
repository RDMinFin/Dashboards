package pojo.transparencia;

public class CEjecucionFF {
	Integer codigo;
	String nombre;
 	double ejecutado;
	double vigente;
	double ejecucion_financiera;
	double meta;
	double meta_avanzado;
	double ejecucion_fisica;
	public CEjecucionFF(Integer codigo, String nombre, double ejecutado, double vigente, double ejecucion_financiera,
			double meta, double meta_avanzado, double ejecucion_fisica) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.ejecutado = ejecutado;
		this.vigente = vigente;
		this.ejecucion_financiera = ejecucion_financiera;
		this.meta = meta;
		this.meta_avanzado = meta_avanzado;
		this.ejecucion_fisica = ejecucion_fisica;
	}
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public double getEjecutado() {
		return ejecutado;
	}
	public void setEjecutado(double ejecutado) {
		this.ejecutado = ejecutado;
	}
	public double getVigente() {
		return vigente;
	}
	public void setVigente(double vigente) {
		this.vigente = vigente;
	}
	public double getEjecucion_financiera() {
		return ejecucion_financiera;
	}
	public void setEjecucion_financiera(double ejecucion_financiera) {
		this.ejecucion_financiera = ejecucion_financiera;
	}
	public double getMeta() {
		return meta;
	}
	public void setMeta(double meta) {
		this.meta = meta;
	}
	public double getMeta_Avanzado() {
		return meta_avanzado;
	}
	public void setMeta_Avanzado(double meta_avanzado) {
		this.meta_avanzado = meta_avanzado;
	}
	public double getEjecucion_fisica() {
		return ejecucion_fisica;
	}
	public void setEjecucion_fisica(double ejecucion_fisica) {
		this.ejecucion_fisica = ejecucion_fisica;
	}
}
