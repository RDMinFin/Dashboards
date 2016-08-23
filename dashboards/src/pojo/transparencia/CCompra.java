package pojo.transparencia;
import java.sql.Timestamp;


public class CCompra {
	String entidad;
	String unidad;
	String tipo;
	String id;
	Timestamp fecha;
	String descripcion;
	String modalidad;
	String estado;
	String nit;
	String nombre;
	double monto;
	
	public CCompra(String entidad, String unidad, String tipo, String id, Timestamp fecha, String descripcion, String modalidad,
			String estado, String nit, String nombre, double monto) {
		super();
		this.entidad = entidad;
		this.unidad = unidad;
		this.tipo = tipo;
		this.id = id;
		this.fecha = fecha;
		this.descripcion = descripcion;
		this.modalidad = modalidad;
		this.estado = estado;
		this.nit = nit;
		this.nombre = nombre;
		this.monto = monto;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEntidad() {
		return entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getModalidad() {
		return modalidad;
	}

	public void setModalidad(String modalidad) {
		this.modalidad = modalidad;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}
	
	
	
}
