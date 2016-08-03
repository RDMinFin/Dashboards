package servlets;

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

import dao.CGastoGeograficoDAO;
import pojo.CGastoGeografico;

/**
 * Servlet implementation class SGastoGeografico
 */
@WebServlet("/SGastoGeografico")
public class SGastoGeografico extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stgeografico{
		int geografico;
		int ejercicio;
		int mes;
		int puntos;
		String latitud;
		String longitud;
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SGastoGeografico() {
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
		if(action.compareTo("gastogeografico")==0){
			int mes = map.get("mes")!=null ? Integer.parseInt(map.get("mes")) : 1;
			ArrayList<stgeografico> stgeograficos=new ArrayList<stgeografico>();
			ArrayList<CGastoGeografico> geograficos = CGastoGeograficoDAO.getGastoGeograficoPuntos(mes);
			if(geograficos!=null && geograficos.size()>0){
				for(CGastoGeografico geografico : geograficos){
					stgeografico sttemp = new stgeografico();
					sttemp.ejercicio = geografico.getEjercicio();
					sttemp.geografico = geografico.getGeografico();
					sttemp.mes = mes;
					sttemp.puntos = (geografico.getGeografico()==101) ? geografico.getPuntos() / 10 : geografico.getPuntos();
					sttemp.latitud = geografico.getLatitud();
					sttemp.longitud = geografico.getLongitud();
					stgeograficos.add(sttemp);
				}
				
				String response_text=new GsonBuilder().serializeNulls().create().toJson(stgeograficos);
	            response_text = String.join("", "\"geograficos\":",response_text);
		            
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
		            
		        gz.write(response_text.getBytes("UTF-8"));
	            gz.close();
	            output.close();
			}
		}
	}

}
