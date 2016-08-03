package servlets.paptn;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import dao.CPaptnDAO;

/**
 * Servlet implementation class SEstructurasFinanciamiento
 */
@WebServlet("/SEstructurasFinanciamiento")
public class SEstructurasFinanciamiento extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SEstructurasFinanciamiento() {
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
		Double[] estructuras_financiamiento = CPaptnDAO.getEstructurasFinanciamiento(DateTime.now().getYear());
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		
		String response_text = String.join("", "{\"success\":true, \"ejercicio\": \"", estructuras_financiamiento[0].toString(),"\", ",
				" \"tributarias\": \"",estructuras_financiamiento[1].toString(),"\", ",
				" \"prestamos_externos\": \"",estructuras_financiamiento[2].toString(),"\", ",
				" \"donaciones\": \"",estructuras_financiamiento[3].toString(),"\", ",
				" \"otras\": \"",estructuras_financiamiento[4].toString(),"\" }");
	        
	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
            gz.close();
            output.close();
		
	}

}
