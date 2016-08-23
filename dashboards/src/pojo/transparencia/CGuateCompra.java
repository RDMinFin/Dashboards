package pojo.transparencia;

import java.sql.Timestamp;

public class CGuateCompra {
	private Integer id;
	private Integer nog;
	private String npg;
	private Integer programa;
	private Integer subprograma;
	private String usuario;
	private Timestamp fecha;

	public CGuateCompra() {

	}

	public CGuateCompra(Integer id, Integer nog, String npg, Integer programa, Integer subprograma, String usuario,
			Timestamp fecha) {
		this.id = id;
		this.nog = nog;
		this.npg = npg;
		this.programa = programa;
		this.subprograma = subprograma;
		this.usuario = usuario;
		this.fecha = fecha;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNog() {
		return nog;
	}

	public void setNog(Integer nog) {
		this.nog = nog;
	}

	public String getNpg() {
		return npg;
	}

	public void setNpg(String npg) {
		this.npg = npg;
	}

	public Integer getPrograma() {
		return programa;
	}

	public void setPrograma(Integer programa) {
		this.programa = programa;
	}

	public Integer getSubprograma() {
		return subprograma;
	}

	public void setSubprograma(Integer subprograma) {
		this.subprograma = subprograma;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

}
