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

import dao.CMunicipioDAO;
import pojo.CMunicipio;

/**
 * Servlet implementation class STown
 */
@WebServlet("/STown")
public class STown extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	class stcenter{
		String latitude;
		String longitude;
	}
	
	class sttown{
    	int id;
    	String name;
    	String latitude;
    	String longitude;
    	stcenter center;
    }
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public STown() {
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
		//if(CShiro.hasPermission("towns")){
			String action = map.get("action");
			if(action.compareTo("listTownsMap")==0){
				ArrayList<sttown> sttowns=new ArrayList<sttown>();
				ArrayList<CMunicipio> towns = CMunicipioDAO.getMunicipios();
				if(towns!=null && towns.size()>0){
					for(CMunicipio town : towns){
						sttown sttemp = new sttown();
						sttemp.id = town.getId();
						sttemp.name = town.getNombre();
						sttemp.latitude = town.getLatitud();
						sttemp.longitude = town.getLongitud();
						sttemp.center = new stcenter();
						sttemp.center.latitude = town.getLatitud();
						sttemp.center.longitude = town.getLongitud();
						sttowns.add(sttemp);
					}
					
					String response_text=new GsonBuilder().serializeNulls().create().toJson(sttowns);
		            response_text = String.join("", "\"towns\":",response_text);
			            
			        response_text = String.join("", "{\"success\":true,", response_text,"}");
			            
			        gz.write(response_text.getBytes("UTF-8"));
		            gz.close();
		            output.close();
				}
			}
		//}
		//else{
			//response.sendRedirect("/main.jsp");
		//}
	}

}
