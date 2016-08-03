package servlets.metas;

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

import dao.metas.CMetaDAO;
import pojo.metas.CMeta;

/**
 * Servlet implementation class SMetas
 */
@WebServlet("/SMetas")
public class SMetas extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	class stmeta{
		int id;
		int nivel;
		String nombre;
		double asignado;
		double vigente;
		double gasto;
		double modificaciones;
		long meta;
		double vigente_entidad;
		long avance;
		int indice_alineamiento;
	}
	
	class stmeta_chartff{
		int mes;
		double fisica;
		double financiera;
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SMetas() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		
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
		
		int mes = map.get("mes")!=null ? Integer.parseInt(map.get("mes")) : 0;
		if(action.compareTo("getMetasEntidades")==0){
			ArrayList<CMeta> metas = CMetaDAO.getMetasEntidades(2016, mes);
			ArrayList<stmeta> stmetas = new ArrayList<stmeta>();
			double indice;
			for(CMeta meta : metas){
				stmeta temp= new stmeta();
				temp.asignado = meta.getAsignado();
				temp.avance = meta.getAvance();
				temp.gasto = meta.getGasto();
				temp.modificaciones = meta.getModificaciones();
				temp.id = meta.getId();
				temp.meta = meta.getMeta();
				temp.nivel = 1;
				temp.vigente = meta.getVigente();
				temp.vigente_entidad = meta.getVigente_entidad();
				indice=(temp.vigente / temp.vigente_entidad);
				if(indice<50)
		    		temp.indice_alineamiento = 4;
				else if(indice<75)
					temp.indice_alineamiento = 2;
				else if(indice<100)
					temp.indice_alineamiento = 3;
				else
					temp.indice_alineamiento = 1;
				temp.nombre = meta.getNombre();
				stmetas.add(temp);
			}
			
			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");
			response_text=new GsonBuilder().serializeNulls().create().toJson(stmetas);
            response_text = String.join("", "\"metas\":",response_text);
	            
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		if(action.compareTo("getMetasMetas")==0){
			ArrayList<CMeta> metas = CMetaDAO.getMetasMetas(2016, mes);
			ArrayList<stmeta> stmetas = new ArrayList<stmeta>();
			for(CMeta meta : metas){
				stmeta temp= new stmeta();
				temp.asignado = meta.getAsignado();
				temp.avance = meta.getAvance();
				temp.gasto = meta.getGasto();
				temp.modificaciones = meta.getModificaciones();
				temp.id = meta.getId();
				temp.meta = meta.getMeta();
				temp.nivel = 1;
				temp.vigente = meta.getVigente();
				temp.nombre = meta.getNombre();
				temp.indice_alineamiento = 0;
				stmetas.add(temp);
			}
			
			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");
			response_text=new GsonBuilder().serializeNulls().create().toJson(stmetas);
            response_text = String.join("", "\"metas\":",response_text);
	            
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		if(action.compareTo("chartMetasFF")==0){
			ArrayList<Double[]> metas = CMetaDAO.getEjecucionFinancieraFisicaMetasMeses(2016, mes);
			ArrayList<stmeta_chartff> stmetas=new ArrayList<stmeta_chartff>();
			for(int i=0; i<metas.size(); i++){
				stmeta_chartff temp = new stmeta_chartff();
				temp.financiera = (metas.get(i)[1]/metas.get(i)[2]);
				temp.fisica = (metas.get(i)[3]/metas.get(i)[4]);
				temp.mes = metas.get(i)[0].intValue();
				stmetas.add(temp);
			}
			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");
			response_text=new GsonBuilder().serializeNulls().create().toJson(stmetas);
            response_text = String.join("", "\"puntos\":",response_text);
	            
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
	        
	    OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
	    gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
		
	}

}
