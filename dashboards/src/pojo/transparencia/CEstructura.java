package pojo.transparencia;

public class CEstructura {
	int entidad;
	int unidad_ejecutora;
	int programa;
	int subprograma;
	int proyecto;
	int actividad;
	int obra;
	
	public CEstructura(int entidad, int unidad_ejecutora, int programa, int subprograma, int proyecto, int actividad,
			int obra) {
		super();
		this.entidad = entidad;
		this.unidad_ejecutora = unidad_ejecutora;
		this.programa = programa;
		this.subprograma = subprograma;
		this.proyecto = proyecto;
		this.actividad = actividad;
		this.obra = obra;
	}

	public int getEntidad() {
		return entidad;
	}

	public void setEntidad(int entidad) {
		this.entidad = entidad;
	}

	public int getUnidad_ejecutora() {
		return unidad_ejecutora;
	}

	public void setUnidad_ejecutora(int unidad_ejecutora) {
		this.unidad_ejecutora = unidad_ejecutora;
	}

	public int getPrograma() {
		return programa;
	}

	public void setPrograma(int programa) {
		this.programa = programa;
	}

	public int getSubprograma() {
		return subprograma;
	}

	public void setSubprograma(int subprograma) {
		this.subprograma = subprograma;
	}

	public int getProyecto() {
		return proyecto;
	}

	public void setProyecto(int proyecto) {
		this.proyecto = proyecto;
	}

	public int getActividad() {
		return actividad;
	}

	public void setActividad(int actividad) {
		this.actividad = actividad;
	}

	public int getObra() {
		return obra;
	}

	public void setObra(int obra) {
		this.obra = obra;
	}
	
	
}
