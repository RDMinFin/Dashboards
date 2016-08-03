package servlets.transparencia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.transparencia.CActividadDAO;
import pojo.transparencia.CActividad;
import pojo.transparencia.CDocumento;
import pojo.transparencia.CResponsable;
import shiro.utilities.CShiro;
import utilities.CDate;

/**
 * Servlet implementation class SActividad
 */
@WebServlet("/SSaveActividad")
public class SSaveActividad extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	class stactividad{
    	int id;
		String latitude;
    	String longitude;
    	String descripcion;
    	String nombre;
    	String fecha_inicio;
    	String fecha_fin;
    	String entidades;
    	long responsable_id;
    	String responsable_nombre;
    	String responsable_telefono;
    	String responsable_correo;
    	double porcentaje_ejecucion;
    	String[] fotos;
    	int entidad;
    	int unidad_ejecutora;
    	int programa;
    	int subprograma;
    	int proyecto;
    	int actividad;
    	int obra;
    }
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SSaveActividad() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		
		OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
		String response_text = "";
		
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		StringBuilder sb = new StringBuilder();
	    BufferedReader br = request.getReader();
	    String str;
	    while( (str = br.readLine()) != null ){
	        sb.append(str);
	    };
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String action = map.get("action");
		if(action.compareTo("create")==0){
			CResponsable responsable = new CResponsable(-1,map.get("responsable_nombre"),map.get("responsable_correo"),map.get("responsable_telefono"));
			CActividad actividad = new CActividad(-1, map.get("nombre"), map.get("descripcion"),new Timestamp(Long.parseLong(map.get("fecha_inicio"))),new Timestamp(Long.parseLong(map.get("fecha_fin"))),
					Double.parseDouble(map.get("porcentaje_ejecucion")),map.get("coord_lat"),map.get("coord_long"),map.get("entidades"),responsable,null,null);
			CActividadDAO.crearActividad(actividad,CShiro.getAttribute("username").toString());
			response_text = String.join("", "{\"success\":true, \"result\":\"creada\"}");
		}
		else if(action.compareTo("update")==0){
			int id = Integer.parseInt(map.get("id"));
			if (id>0){
				CResponsable responsable = new CResponsable(Integer.parseInt(map.get("responsable_id")),map.get("responsable_nombre"),map.get("responsable_correo"),map.get("responsable_telefono"));
				CActividad actividad = new CActividad(id,map.get("nombre"),map.get("descripcion"),new Timestamp(Long.parseLong(map.get("fecha_inicio"))),new Timestamp(Long.parseLong(map.get("fecha_fin"))),
						Double.parseDouble(map.get("porcentaje_ejecucion")),map.get("coord_lat"),map.get("coord_long"),map.get("entidades"),responsable,null,null);
				CActividadDAO.actualizarActividad(actividad,CShiro.getAttribute("username").toString());
				response_text = String.join("", "{\"success\":true, \"result\":\"actualizada\"}");
			}else
				response_text = String.join("", "{\"success\":false, \"result\":\"NO actualizada\"}");
        }
		else if(action.compareTo("delete")==0){
			int id = Integer.parseInt(map.get("id"));
			if (id>0){
				CResponsable responsable = new CResponsable(Integer.parseInt(map.get("responsable_id")),null,null,null);
				CActividad actividad = new CActividad(id,null,null,null,null,0,null,null,null,responsable,null,null);
				CActividadDAO.eliminarActividad(actividad);
				response_text = String.join("", "{\"success\":true, \"result\":\"eliminada\"}");
			}else
				response_text = String.join("", "{\"success\":false, \"result\":\"NO eliminada\"}");
        }
		else if(action.compareTo("getlist")==0){
			ArrayList<CActividad> actividades = CActividadDAO.getActividades();
			ArrayList<stactividad> stactividades= new ArrayList<stactividad>();
			for(CActividad actividad : actividades){
				stactividad temp = new stactividad();
				temp.latitude = actividad.getCoord_lat();
				temp.longitude = actividad.getCoord_long();
				temp.descripcion = actividad.getDescripcion();
				temp.entidades = actividad.getEntidades();
				temp.fecha_fin = CDate.formatTimestamp(actividad.getFecha_fin());
				temp.fecha_inicio = CDate.formatTimestamp(actividad.getFecha_inicio());
				temp.id = actividad.getId();
				temp.nombre = actividad.getNombre();
				temp.responsable_id = (actividad.getResponsable()!=null) ? actividad.getResponsable().getId() : -1;
				temp.responsable_correo = (actividad.getResponsable()!=null) ? actividad.getResponsable().getCorreo() : null;
				temp.responsable_nombre = (actividad.getResponsable()!=null) ? actividad.getResponsable().getNombre() : null;
				temp.responsable_telefono = (actividad.getResponsable()!=null) ? actividad.getResponsable().getTelefono() : null;
				temp.porcentaje_ejecucion = actividad.getPorcentaje_ejecucion();
				ArrayList<String> fotos = new ArrayList<String>();
				for(CDocumento doc : actividad.getDocumentos()){
					if(doc.getTipo()==1)
						fotos.add(doc.getNombre());
				}
				if(fotos.size()>0){
					temp.fotos = new String[fotos.size()];
					temp.fotos = fotos.toArray(temp.fotos);
				}
				temp.entidad = (actividad.getEstructura()!=null) ? actividad.getEstructura().getEntidad() : null;
				temp.unidad_ejecutora = (actividad.getEstructura()!=null) ? actividad.getEstructura().getUnidad_ejecutora() : null;
				temp.programa = (actividad.getEstructura()!=null) ? actividad.getEstructura().getPrograma() : null;
				temp.subprograma = (actividad.getEstructura()!=null) ? actividad.getEstructura().getSubprograma() : null;
				temp.proyecto = (actividad.getEstructura()!=null) ? actividad.getEstructura().getProyecto() : null;
				temp.actividad = (actividad.getEstructura()!=null) ? actividad.getEstructura().getActividad() : null;
				temp.obra = (actividad.getEstructura()!=null) ? actividad.getEstructura().getObra() : null;

				stactividades.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(stactividades);
            response_text = String.join("", "\"actividades\":",response_text);
	            
	        response_text = String.join("", "{\"success\":true,", response_text,"}");       
	    }
		gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}

}
