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
import dao.transparencia.CGuateCompraDAO;
import pojo.transparencia.CActividad;
import pojo.transparencia.CGuateCompra;
import pojo.transparencia.CDocumento;
import pojo.transparencia.CResponsable;
import servlets.transparencia.SSaveActividad.stactividad;
import shiro.utilities.CShiro;
import utilities.CDate;

/**
 * Servlet implementation class SSaveCompra
 */
@WebServlet("/SSaveCompra")
public class SGuateCompra extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SGuateCompra() {
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

		CGuateCompra compra = null;

		Integer id = Integer.parseInt(map.get("id"));
		Integer nog = Integer.parseInt(map.get("nog"));
		String npg = map.get("npg");
		Integer programa = Integer.parseInt(map.get("programa"));
		Integer subprograma = Integer.parseInt(map.get("subprograma"));

		String usuario = CShiro.getAttribute("username").toString();
		Timestamp fecha = new Timestamp(DateTime.now().getMillis());

		compra = new CGuateCompra(id, nog, npg, programa, subprograma, usuario, fecha);

		switch (map.get("action")) {
		case "create":
			CGuateCompraDAO.crearCompra(compra);
			response_text = String.join("", "{\"success\":true, \"result\":\"creada\"}");
			break;

		case "update":
			if (id > 0) {
				CGuateCompraDAO.actualizarCompra(compra);
				response_text = String.join("", "{\"success\":true, \"result\":\"actualizada\"}");
			} else
				response_text = String.join("", "{\"success\":false, \"result\":\"NO actualizada\"}");

			break;

		case "delete":
			if (id > 0) {
				CGuateCompraDAO.deleteCompra(id);
				response_text = String.join("", "{\"success\":true, \"result\":\"eliminada\"}");
			} else
				response_text = String.join("", "{\"success\":false, \"result\":\"NO eliminada\"}");

			break;
		default:
			List<CGuateCompra> numsCompra = CGuateCompraDAO.getCompras();

			response_text = new GsonBuilder().serializeNulls().create().toJson(numsCompra);
			response_text = String.join("", "\"compras\":", response_text);

			response_text = String.join("", "{\"success\":true,", response_text, "}");
			break;
		}

		gz.write(response_text.getBytes("UTF-8"));
		gz.close();
		output.close();
	}

	private CGuateCompra setCompra(Map<String,String>) {

	}
}
