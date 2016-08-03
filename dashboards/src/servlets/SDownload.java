package servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.transparencia.CDocumentoDAO;
import pojo.transparencia.CDocumento;
import utilities.CLogger;

/**
 * Servlet implementation class SDownload
 */
@WebServlet("/SDownload")
public class SDownload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SDownload() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String place = request.getParameter("place");
		int id_documento= request.getParameter("iddoc")!=null ? Integer.parseInt(request.getParameter("iddoc")) : -1;
		if(place!=null && place.length()>0 && id_documento>-1){
			CDocumento documento = CDocumentoDAO.getDocumento(id_documento);
			if(documento!=null){
				File file=null;
				BufferedInputStream input = null;
				BufferedOutputStream output = null;
				file = (documento.getTipo()==1) ? new File("/files/images/"+place+"/"+(documento.getId_actividad()>-1 ? documento.getId_actividad()+"/" : "")+documento.getNombre()) : 
					new File("/files/docs/"+place+"/"+documento.getNombre());				
				response.setHeader("Content-Type", getServletContext().getMimeType(file.getName()));
				response.setHeader("Content-Disposition", "inline; filename=\"" + documento.getNombre() + "\""); 
				try {
				response.setHeader("Content-Length", String.valueOf(file.length()));
				input = new BufferedInputStream(new FileInputStream(file));
				output = new BufferedOutputStream(response.getOutputStream());
				
				    byte[] buffer = new byte[8192];
				    for (int length = 0; (length = input.read(buffer)) > 0; ) {
				        output.write(buffer, 0, length);
				    }
				} 
				catch(Exception e){
					CLogger.write("1", SDownload.class, e);
				}
				finally {
				    if (output != null) try { output.close(); } catch (IOException e) { CLogger.write("2", SDownload.class, e); }
				    if (input != null) try { input.close(); } catch (IOException e) { CLogger.write("3", SDownload.class, e); }
				}
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
