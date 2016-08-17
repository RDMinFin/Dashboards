package servlets.transparencia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
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
import dao.transparencia.CDocumentoDAO;
import pojo.transparencia.CActividad;
import pojo.transparencia.CDocumento;
import utilities.CDate;


/**
 * Servlet implementation class STransparenciaVentanas
 */
@WebServlet("/STransparenciaDocumentos")
public class STransparenciaDocumentos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	class stdocumento{
    	int id;
    	String actividad;
    	String titulo;
    	String nombre;
    	int tipo;
    	String fecha_creacion;
    	String usuario_creacion;
    }
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public STransparenciaDocumentos() {
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
		int id_actividad = map.get("id")!=null ? Integer.parseInt(map.get("id")):-1;
		if(action.compareTo("getlist")==0){
		
			ArrayList<CDocumento> documentos = CDocumentoDAO.getDocumentos(id_actividad);
			ArrayList<stdocumento> stdocumentos= new ArrayList<stdocumento>();
			for(CDocumento documento : documentos){
				stdocumento temp = new stdocumento();
				CActividad act = CActividadDAO.getActividad(documento.getId_actividad());
				temp.actividad = act!=null ? act.getNombre() : "";
				temp.fecha_creacion = CDate.formatTimestamp(documento.getFecha_creacion());
				temp.id = documento.getId();
				temp.nombre = documento.getNombre();
				temp.tipo = documento.getTipo();
				temp.titulo = documento.getTitulo();
				temp.usuario_creacion = documento.getUsuario_creacion();
				
				stdocumentos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(stdocumentos);
	        response_text = String.join("", "\"documentos\":",response_text);	            
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}else if (action.compareTo("delete")==0){
			int iddoc = map.get("iddoc")!=null ? Integer.parseInt(map.get("iddoc")):-1;
			if (iddoc > 0 && CDocumentoDAO.deleteDocumento(iddoc))
				response_text = String.join("", "{\"success\":true}");
			else
				response_text = String.join("", "{\"success\":false}");
		}
		
		gz.write(response_text.getBytes("UTF-8"));
		gz.close();
		output.close();	
		
	}

}
