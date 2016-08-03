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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dao.CGridStateDAO;
import pojo.CGridState;
import shiro.utilities.CShiro;

/**
 * Servlet implementation class SSaveGridState
 */
@WebServlet("/SSaveGridState")
public class SSaveGridState extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SSaveGridState() {
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
		if(action.compareTo("savestate")==0){
			CGridState gridstate = new CGridState(CShiro.getAttribute("username").toString(), map.get("grid"), map.get("state").toString());
			CGridStateDAO.saveGridState(gridstate);
		}
		else if(action.compareTo("getstate")==0){
			String state = CGridStateDAO.getGridState(CShiro.getAttribute("username").toString(), map.get("grid"));
			response_text = String.join("", "{\"success\":true, \"state\":", state.length()>0 ?  state : "\"\""," }");
        }
		gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}

}
