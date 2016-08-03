package servlets.paptn;

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

import dao.CPaptnDAO;
import pojo.CEjecucion;

/**
 * Servlet implementation class SEntidadesTabla
 */
@WebServlet("/SEntidadesTabla")
public class SEntidadesTabla extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stentidad{
		int entidad;
    	String nombre;
    	double gp_ejecucion;
    	double gp_vigente;
    	double gp_porcentaje;
    	double paptn_ejecucion;
    	double paptn_vigente;
    	double paptn_porcentaje;
    }
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SEntidadesTabla() {
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
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		StringBuilder sb = new StringBuilder();
	    BufferedReader br = request.getReader();
	    String str;
	    while( (str = br.readLine()) != null ){
	        sb.append(str);
	    };
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		int mes = map.get("mes")!=null ? Integer.parseInt(map.get("mes")) : 0;
		ArrayList<CEjecucion> entidades = CPaptnDAO.getEntidadesEjecucion(mes);
		ArrayList<stentidad> stentidades=new ArrayList<stentidad>();
		if(entidades!=null && entidades.size()>0){
			for(CEjecucion centidad : entidades){
				stentidad temp= new stentidad();
				temp.entidad = centidad.getEntidad();
				temp.nombre = centidad.getNombre();
				temp.gp_ejecucion = centidad.getAno1();
				temp.gp_vigente = centidad.getAno2();
				temp.gp_porcentaje = centidad.getAno3();
				temp.paptn_ejecucion = centidad.getAno4();
				temp.paptn_vigente = centidad.getAno5();
				temp.paptn_porcentaje = centidad.getCierre_estimado()*100;
				stentidades.add(temp);
			}
			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");
			String response_text=new GsonBuilder().serializeNulls().create().toJson(stentidades);
            response_text = String.join("", "\"entidades\":",response_text);
	            
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
	        
	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
            gz.close();
            output.close();
		}	
	}

}
