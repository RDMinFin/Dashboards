package servlets;

import java.awt.image.BufferedImage; 

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;

import utilities.CLogger;

/**
 * Servlet implementation class SPicture
 */
@WebServlet("/SPicture")
public class SPicture extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SPicture() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String picture = request.getParameter("pic");
		String subprograma= request.getParameter("subp");
		int idevento = request.getParameter("idevento")!=null ? Integer.parseInt(request.getParameter("idevento").toString()) :  -1;
		int ancho = request.getParameter("pic_w")!=null ? Integer.parseInt(request.getParameter("pic_w").toString()) :  -1;
		int alto = request.getParameter("pic_h")!=null ? Integer.parseInt(request.getParameter("pic_h").toString()) :  -1;
		try{
			if(picture!=null){
				File file=null;
				BufferedInputStream input = null;
				BufferedOutputStream output = null;
				file = (idevento>0) ? new File("/files/images/"+subprograma+"/"+idevento+"/"+picture) : new File("/files/images/"+subprograma+"/buttons/"+picture);
				
				response.setHeader("Content-Type", getServletContext().getMimeType(file.getName()));
				response.setHeader("Content-Disposition", "inline; filename=\"" + picture + "\"");
				
				if(ancho>-1 || alto>-1){
					BufferedImage image = ImageIO.read(file);
					BufferedImage iresized=resize(image, ancho>0 ? ancho : image.getWidth(), alto>0 ? alto : image.getHeight());
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(iresized, FilenameUtils.getExtension(file.getName()), baos); 
					input = new BufferedInputStream(new ByteArrayInputStream(baos.toByteArray()));
					response.setHeader("Content-Length", String.valueOf(baos.size()));
				}
				else{
					response.setHeader("Content-Length", String.valueOf(file.length()));
					input = new BufferedInputStream(new FileInputStream(file));
				}
				output = new BufferedOutputStream(response.getOutputStream());
				try {
				    byte[] buffer = new byte[8192];
				    for (int length = 0; (length = input.read(buffer)) > 0; ) {
				        output.write(buffer, 0, length);
				    }
				} 
				catch(Exception e){
					CLogger.write("1", SPicture.class, e);
				}
				finally {
				    if (output != null) try { output.close(); } catch (IOException e) { CLogger.write("2", SPicture.class, e); }
				    if (input != null) try { input.close(); } catch (IOException e) { CLogger.write("3", SPicture.class, e); }
				}
			
			}
		}
		catch(Exception e){
			CLogger.write("4", SPicture.class, e);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	private BufferedImage resize(BufferedImage image, int width, int height) {
		Mode translationMode = Mode.AUTOMATIC;
		if (image.getWidth() < width && image.getHeight() < height) {
			return image;
		} else if (image.getWidth() < width) {
			translationMode = Mode.FIT_TO_HEIGHT;
		} else if (image.getHeight() < height) {
			translationMode = Mode.FIT_TO_WIDTH;
		} else {
			float wRatio = ((float)width / (float)image.getWidth());
			float hRatio = ((float)height / (float)image.getHeight());
			translationMode = wRatio < hRatio ? Mode.FIT_TO_WIDTH : Mode.FIT_TO_HEIGHT;
		}
				
		BufferedImage bufferedImage = Scalr.resize(image, translationMode, width, height, Scalr.OP_ANTIALIAS);
		return bufferedImage;
	}

}
