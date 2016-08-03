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

import dao.CGrupoGastoDAO;
import pojo.CGrupoGasto;



/**
 * Servlet implementation class SGrupoGasto
 */
@WebServlet("/SGrupoGasto")
public class SGrupoGasto extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stgrupogasto{
		int grupo;
		String nombre;
		boolean checked;
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SGrupoGasto() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
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
			ArrayList<stgrupogasto> stentidades=new ArrayList<stgrupogasto>();
			
			ArrayList<CGrupoGasto> grupos=CGrupoGastoDAO.getGruposGasto(ejercicio);
			if(grupos!=null && grupos.size()>0){
				for(CGrupoGasto cgrupo : grupos){
					stgrupogasto sttemp = new stgrupogasto();
					sttemp.grupo = cgrupo.getGrupoGasto();
					sttemp.nombre = cgrupo.getNombre();
					sttemp.checked = true;
					stentidades.add(sttemp);
				}
				
				String response_text=new GsonBuilder().serializeNulls().create().toJson(stentidades);
	            response_text = String.join("", "\"Grupos\":",response_text);
		            
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
		            
		        gz.write(response_text.getBytes("UTF-8"));
	            gz.close();
	            output.close();
			}
			
		}
	}

}
