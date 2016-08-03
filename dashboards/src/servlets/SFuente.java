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

import dao.CFuenteDAO;
import pojo.CFuente;

/**
 * Servlet implementation class SFuentes
 */
@WebServlet("/SFuente")
public class SFuente extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stfuente{
		int fuente;
		String nombre;
		boolean checked;
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SFuente() {
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
		
		int ejercicio = map.get("ejercicio")!=null ? Integer.parseInt(map.get("ejercicio")) : 0;
		if(ejercicio>0){
			ArrayList<stfuente> stentidades=new ArrayList<stfuente>();
			
			ArrayList<CFuente> fuentes=CFuenteDAO.getFuentes(ejercicio);
			if(fuentes!=null && fuentes.size()>0){
				for(CFuente cfuente : fuentes){
					stfuente sttemp = new stfuente();
					sttemp.fuente = cfuente.getFuente();
					sttemp.nombre = cfuente.getNombre();
					sttemp.checked = true;
					stentidades.add(sttemp);
				}
				
				String response_text=new GsonBuilder().serializeNulls().create().toJson(stentidades);
	            response_text = String.join("", "\"fuentes\":",response_text);
		            
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
		            
		        gz.write(response_text.getBytes("UTF-8"));
	            gz.close();
	            output.close();
			}
			
		}
	}

}
