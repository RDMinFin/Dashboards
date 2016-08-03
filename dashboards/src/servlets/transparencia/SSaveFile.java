package servlets.transparencia;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.joda.time.DateTime;

import dao.transparencia.CDocumentoDAO;
import pojo.transparencia.CDocumento;
import shiro.utilities.CShiro;

/**
 * Servlet implementation class SSaveFile
 */

@WebServlet("/SSaveFile")
public class SSaveFile extends HttpServlet {
	private static final long serialVersionUID = 1L;


    private static final int MAX_MEMORY_SIZE = 1024 * 1024 * 1;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SSaveFile() {
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
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        String fileName="";
        String filePath="";
        File uploadedFile=null;
        //String titulo="";
        //String nombre="";
        String place="";
        FileItem fItem=null;
        Integer id_actividad=-1;        
        
        if (!isMultipart) {
            return;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();

        factory.setSizeThreshold(MAX_MEMORY_SIZE);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);
        
        try {
            List<?> items = upload.parseRequest(request);
            Iterator<?> iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if (!item.isFormField()) {                	
                    fItem = item;
                }else{
                	switch(item.getFieldName()){
                	//case "titulo" : titulo = item.getString(); break;
                	//case "nombre" : nombre = item.getString(); break;
                	case "id_actividad" : id_actividad = Integer.parseInt(item.getString());
                	case "place" : place = item.getString(); break;
                	}
                }
            }
            int filetype = fItem.getContentType().contains("image")?1:2;

            String uploadFolder =(filetype==1) ? "/files/images/"+place+"/"+(id_actividad>-1 ? id_actividad+"/" : "") : 
            									"/files/docs/"+place+"/"+(id_actividad>-1 ? id_actividad+"/" : "");
            File path = new File(uploadFolder);
            if (!path.exists()){
            	path.mkdirs();
            }
            fileName = new File(fItem.getName()).getName();
            filePath = path.getPath() +"/"+ fileName;
            uploadedFile = new File(filePath);
            fItem.write(uploadedFile);
            CDocumento doc = new CDocumento(-1, id_actividad, fileName, fileName, filePath, filetype, new Timestamp(DateTime.now().getMillis()), CShiro.getAttribute("username").toString());
            CDocumentoDAO.crearDocumento(doc);
            
    		request.setCharacterEncoding("UTF-8");
    		response.setHeader("Content-Encoding", "gzip");
    		response.setCharacterEncoding("UTF-8");
    		OutputStream output = response.getOutputStream();
    		GZIPOutputStream gz = new GZIPOutputStream(output);
    		String response_text = "";
	        response_text = String.join("", "{\"success\":true}");       
			gz.write(response_text.getBytes("UTF-8"));
	        gz.close();
	        output.close();
    		
    		
        } catch (FileUploadException ex) {
            throw new ServletException(ex);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
      
	}

}
