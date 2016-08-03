package servlets.transparencia;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.GsonBuilder;

import dao.transparencia.CActividadDAO;
import dao.transparencia.CDocumentoDAO;
import dao.transparencia.CEjecucionFFDAO;


/**
 * Servlet implementation class STransparenciaVentanas
 */
@WebServlet("/STransparenciaVentanas")
public class STransparenciaVentanas extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	class stresults{
    	int actividades;
    	int documentos;
    	double ejecucion_financiera;
    	double ejecucion_fisica;
    }
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public STransparenciaVentanas() {
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
		
		stresults results = new stresults();
		results.actividades = CActividadDAO.numActividades();
		results.documentos = CDocumentoDAO.numDocumentos();	
		results.ejecucion_financiera = CEjecucionFFDAO.ejecucionFinanciera(94,2);
		results.ejecucion_fisica = CEjecucionFFDAO.ejecucionFisica(94,2);
					
					
		String response_text=new GsonBuilder().serializeNulls().create().toJson(results);
		response_text = String.join("", "\"results\":",response_text);
			            
		response_text = String.join("", "{\"success\":true,", response_text,"}");
			            
		gz.write(response_text.getBytes("UTF-8"));
		gz.close();
		output.close();	
		
	}

}
