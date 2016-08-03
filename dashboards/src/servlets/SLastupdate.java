package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dao.CLastupdateDAO;
import pojo.CLastupdate;

/**
 * Servlet implementation class SLastupdate
 */
@WebServlet("/SLastupdate")
public class SLastupdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SLastupdate() {
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
		
		String dashboard = map.get("dashboard");
		if(dashboard!=null && dashboard.length()>0){
			CLastupdate update=CLastupdateDAO.getLastupdate(dashboard);
			if(update!=null){
				DateTime lastupdate = new DateTime(update.getLast_update());
				DateTimeFormatter fmt = DateTimeFormat.forPattern("h:mm a d/M/yyyy");
				String response_text = String.join("", "{\"success\":true, \"dashboard_name\":\"", update.getDashboard_name() ,"\", \"lastupdate\":\"",fmt.print(lastupdate),"\"}");
		        gz.write(response_text.getBytes("UTF-8"));
	            gz.close();
	            output.close();
			}
			
		}
	}

}
