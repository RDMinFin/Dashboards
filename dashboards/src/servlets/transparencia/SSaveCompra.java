package servlets.transparencia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.transparencia.CActividadDAO;
import dao.transparencia.CComprasDAO;
import pojo.transparencia.CActividad;
import pojo.transparencia.CCompra;
import pojo.transparencia.CDocumento;
import pojo.transparencia.CResponsable;
import servlets.transparencia.SSaveActividad.stactividad;
import shiro.utilities.CShiro;
import utilities.CDate;

/**
 * Servlet implementation class SSaveCompra
 */
@WebServlet("/SSaveCompra")
public class SSaveCompra extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SSaveCompra() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
		String response_text = "";

		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();

		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		;

		Map<String, String> map = gson.fromJson(sb.toString(), type);

		CCompra compra = null;

		Integer id = Integer.parseInt(map.get("id"));
		Integer nog = Integer.parseInt(map.get("nog"));
		String npg = map.get("npg");
		Integer programa = Integer.parseInt(map.get("programa"));
		Integer subprograma = Integer.parseInt(map.get("subprograma"));

		String usuario = CShiro.getAttribute("username").toString();
		Timestamp fecha = new Timestamp(DateTime.now().getMillis());

		compra = new CCompra(id, nog, npg, programa, subprograma, usuario, fecha);

		switch (map.get("action")) {
		case "create":
			CComprasDAO.crearCompra(compra);
			response_text = String.join("", "{\"success\":true, \"result\":\"creada\"}");

			break;

		case "update":
			if (id > 0) {
				CComprasDAO.actualizarCompra(compra);
				response_text = String.join("", "{\"success\":true, \"result\":\"actualizada\"}");
			} else
				response_text = String.join("", "{\"success\":false, \"result\":\"NO actualizada\"}");

			break;

		case "delete":
			if (id > 0) {
				CComprasDAO.deleteCompra(id);
				response_text = String.join("", "{\"success\":true, \"result\":\"eliminada\"}");
			} else
				response_text = String.join("", "{\"success\":false, \"result\":\"NO eliminada\"}");

			break;
		default:
			List<CCompra> actividades = CComprasDAO.getActividades();
			for (CActividad actividad : actividades) {
				stactividad temp = new stactividad();
				temp.latitude = actividad.getCoord_lat();
				temp.longitude = actividad.getCoord_long();
				temp.descripcion = actividad.getDescripcion();
				temp.entidades = actividad.getEntidades();
				temp.fecha_fin = CDate.formatTimestamp(actividad.getFecha_fin());
				temp.fecha_inicio = CDate.formatTimestamp(actividad.getFecha_inicio());
				temp.id = actividad.getId();
				temp.nombre = actividad.getNombre();
				temp.responsable_id = (actividad.getResponsable() != null) ? actividad.getResponsable().getId() : -1;
				temp.responsable_correo = (actividad.getResponsable() != null) ? actividad.getResponsable().getCorreo()
						: null;
				temp.responsable_nombre = (actividad.getResponsable() != null) ? actividad.getResponsable().getNombre()
						: null;
				temp.responsable_telefono = (actividad.getResponsable() != null)
						? actividad.getResponsable().getTelefono() : null;
				temp.porcentaje_ejecucion = actividad.getPorcentaje_ejecucion();
				ArrayList<String> fotos = new ArrayList<String>();
				for (CDocumento doc : actividad.getDocumentos()) {
					if (doc.getTipo() == 1)
						fotos.add(doc.getNombre());
				}
				if (fotos.size() > 0) {
					temp.fotos = new String[fotos.size()];
					temp.fotos = fotos.toArray(temp.fotos);
				}
				temp.entidad = (actividad.getEstructura() != null) ? actividad.getEstructura().getEntidad() : null;
				temp.unidad_ejecutora = (actividad.getEstructura() != null)
						? actividad.getEstructura().getUnidad_ejecutora() : null;
				temp.programa = (actividad.getEstructura() != null) ? actividad.getEstructura().getPrograma() : null;
				temp.subprograma = (actividad.getEstructura() != null) ? actividad.getEstructura().getSubprograma()
						: null;
				temp.proyecto = (actividad.getEstructura() != null) ? actividad.getEstructura().getProyecto() : null;
				temp.actividad = (actividad.getEstructura() != null) ? actividad.getEstructura().getActividad() : null;
				temp.obra = (actividad.getEstructura() != null) ? actividad.getEstructura().getObra() : null;

			}
			response_text = new GsonBuilder().serializeNulls().create().toJson(stactividades);
			response_text = String.join("", "\"actividades\":", response_text);

			response_text = String.join("", "{\"success\":true,", response_text, "}");
			break;
		}

		gz.write(response_text.getBytes("UTF-8"));
		gz.close();
		output.close();
	}

	private void saveCompra() {

	}

	private void updateCompra() {

	}

	private void deleteCompra() {

	}

	private void getCompras() {

	}
}
