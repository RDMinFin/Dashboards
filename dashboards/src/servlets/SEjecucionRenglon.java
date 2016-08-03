package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.codec.Base64;
import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.CEjecucionDAO;
import pojo.CEjecucion;
import utilities.CExcel;
/**
 * Servlet implementation class SEjecucionRenglon
 */
@WebServlet("/SEjecucionRenglon")
public class SEjecucionRenglon extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	class stentidad{
		Integer parent;
    	int entidad;
    	String nombre;
    	double ano1;
    	double ano2;
    	double ano3;
    	double ano4;
    	double ano5;
    	double cierre_estimado;
    	double solicitado;
    	double solicitado_acumulado;
    	double aprobado;
    	double aprobado_sin_anticipo;
    	double anticipo;
    	double aprobado_acumulado;
    	double ejecutado;
    	double ejecutado_acumulado;
    	double vigente;
    	double ejecucion_anual;
    	double porcentaje_aprobacion;
    	int icono_ejecucion_anual;
    }
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SEjecucionRenglon() {
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
		if(action.compareTo("entidadesData")==0){
			int mes = map.get("mes")!=null ? Integer.parseInt(map.get("mes")) : 0;
			int ano = map.get("ano")!=null ? Integer.parseInt(map.get("ano")) : 0;
			int level = map.get("level")!=null ? Integer.parseInt(map.get("level")) : 0;
			int entidad = map.get("entidad")!=null ? Integer.parseInt(map.get("entidad")) : 0;
			String fuentes = map.get("fuentes");
			String grupos = map.get("grupos");
			String nmes = map.get("nmes");
			ArrayList<stentidad> stentidades=new ArrayList<stentidad>();
			boolean todosgrupos = map.get("todosgrupos")!=null && map.get("todosgrupos").compareTo("1")==0;
			ArrayList<CEjecucion> entidades=null; 
			
			int acumulado = map.get("acumulado")!=null ? Integer.parseInt(map.get("acumulado")) : 0;
			if (acumulado==0)
				switch(level){
					case 1: entidades = CEjecucionDAO.getEntidadesEjecucion(mes,fuentes,"1,2,3,4",grupos, todosgrupos); break;
					case 2: entidades = CEjecucionDAO.getEntidadesEjecucionRenglon(entidad,mes,ano,fuentes,"1,2,3,4",grupos); break;
				}
			else
				switch(level){
				case 1: entidades = CEjecucionDAO.getEntidadesEjecucionAcumulada(mes,fuentes,"1,2,3,4",grupos, todosgrupos); break;
				case 2: entidades = CEjecucionDAO.getEntidadesEjecucionRenglonAcumulada(entidad,mes,ano,fuentes,"1,2,3,4",grupos); break;
			}
			
			if(entidades!=null && entidades.size()>0){
				for(CEjecucion centidad : entidades){
					stentidad sttemp = new stentidad();
					sttemp.parent = centidad.getParent();
					sttemp.entidad = centidad.getEntidad();
					sttemp.nombre = centidad.getNombre();
					sttemp.ano1 = centidad.getAno1();
					sttemp.ano2 = centidad.getAno2();
					sttemp.ano3 = centidad.getAno3();
					sttemp.ano4 = centidad.getAno4();
					sttemp.ano5 = centidad.getAno5();
					sttemp.cierre_estimado = 0;
					sttemp.solicitado = centidad.getSolicitado() != null ? centidad.getSolicitado() : 0;
					sttemp.solicitado_acumulado = centidad.getSolicitado_acumulado() != null ? centidad.getSolicitado_acumulado() : 0;
					sttemp.aprobado = centidad.getAprobado() != null ? centidad.getAprobado() : 0 ;
					sttemp.aprobado_sin_anticipo = centidad.getAprobado_sin_anticipo() != null ? centidad.getAprobado_sin_anticipo() : 0 ;
					sttemp.anticipo = centidad.getAnticipo()!= null ? centidad.getAnticipo() : 0;
					sttemp.aprobado_acumulado = centidad.getAprobado_acumulado() != null ? centidad.getAprobado_acumulado() : 0;
					sttemp.porcentaje_aprobacion = centidad.getSolicitado_acumulado() != null && centidad.getAprobado_acumulado() != null && centidad.getSolicitado_acumulado() > 0 ? (centidad.getAprobado_acumulado()/centidad.getSolicitado_acumulado())*100 : 0;
					sttemp.ejecutado = centidad.getEjecutado() != null ? centidad.getEjecutado() : 0;
					sttemp.ejecutado_acumulado = centidad.getEjecutado_acumulado() != null ? centidad.getEjecutado_acumulado() : 0;
					sttemp.vigente = centidad.getVigente();
					sttemp.ejecucion_anual = (sttemp.vigente>0) ? (sttemp.ejecutado_acumulado/sttemp.vigente)*100.00 : 0.00;
					double semaforo = (sttemp.ejecucion_anual*100.00)/(8.33*mes);
					if(semaforo<50)
						sttemp.icono_ejecucion_anual = 4;
					else if(semaforo<75)
						sttemp.icono_ejecucion_anual = 2;
					else if(semaforo<100)
						sttemp.icono_ejecucion_anual = 3;
					else
						sttemp.icono_ejecucion_anual = 1;
					stentidades.add(sttemp);
				}
				
				if(map.get("excel")==null){
					response.setHeader("Content-Encoding", "gzip");
					response.setCharacterEncoding("UTF-8");
					String response_text=new GsonBuilder().serializeNulls().create().toJson(stentidades);
		            response_text = String.join("", "\"entidades\":",response_text);
			            
			        response_text = String.join("", "{\"success\":true,", response_text,"}");
			        
			        OutputStream output = response.getOutputStream();
					GZIPOutputStream gz = new GZIPOutputStream(output);
			        gz.write(response_text.getBytes("UTF-8"));
		            gz.close();
		            output.close();
				}
				else {
					CExcel excel=null;
					String headers[][];
					String[][] extra_lines;
					Workbook wb=null;
					DateTime now = new DateTime();
					ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
					
					if (map.get("excel").compareTo("1")==0){						
						excel = new CExcel("Ejecucion_Presupuestaria_Renglon", level==2);
						headers = new String[][]{{"Código", "Nombre", "Ejecución 2011", "Ejecución 2012", "Ejecución 2013", "Ejecución 2014", "Ejecución 2015","Cuota Aprobada", "Cuota Apr. Ac.", "Ejecutado", "Eje. Acumulado", "Vigente", "% Anual"},
								{"entidad","nombre","ano1","ano2","ano3","ano4","ano5","aprobado","aprobado_acumulado","ejecutado","ejecutado_acumulado","vigente","ejecucion_anual"},
								{"int", "string", "currency", "currency", "currency", "currency", "currency", "currency", "currency", "currency", "currency", "currency", "percent"},
								{"","","sum","sum","sum","sum","sum","sum","sum","sum","sum","sum","div"},
								{"","","","","","","","","","","","","ejecutado_acumulado,vigente"}		
						};
						extra_lines=new String[][]{{"Año",ano+""},{"Mes",nmes},{"Fuentes", fuentes},{"Grupos de Gasto",grupos},{"",""}};
						wb=excel.generateExcel(stentidades, "Ejecucion Presupuestaria", headers , ano, extra_lines);
					}else if (map.get("excel").compareTo("2")==0){						
						excel = new CExcel("Ejecucion_Presupuestaria_Renglon_Acumulada", level==2);
						headers = new String[][]{{"Código", "Nombre", "Ejecución 2011", "Ejecución 2012", "Ejecución 2013", "Ejecución 2014", "Ejecución 2015","Cuota Solicitada", "Cuota Aprobada","Anticipos","Aproabada+Anticipos", "Ejecutado", "Vigente", "% Anual"},
								{"entidad","nombre","ano1","ano2","ano3","ano4","ano5","solicitado_acumulado","aprobado_sin_anticipo","anticipo","aprobado_acumulado","ejecutado_acumulado","vigente","ejecucion_anual"},
								{"int", "string", "currency", "currency", "currency","currency", "currency", "currency", "currency", "currency", "currency", "currency", "currency", "percent"},
								{"","","sum","sum","sum","sum","sum","sum","sum","sum","sum","sum","sum","div"},
								{"","","","","","","","","","","","","","ejecutado_acumulado,vigente"}		
						};
						extra_lines=new String[][]{{"Año",ano+""},{"Mes",nmes},{"Fuentes", fuentes},{"Grupos de Gasto",grupos},{"",""}};
						wb=excel.generateExcel(stentidades, "Ejecucion Presupuestaria Acumulada", headers , ano, extra_lines);
					}
					wb.write(outByteStream);
					byte [] outArray = Base64.encode(outByteStream.toByteArray());
					response.setContentType("application/ms-excel");
					response.setContentLength(outArray.length);
					response.setHeader("Expires:", "0"); // eliminates browser caching
					response.setHeader("Content-Disposition", "attachment; Ejecución_Presupuestaria_Renglon_"+now.getDayOfMonth()+""+(now.getMonthOfYear()+1)+""+now.getYear()+".xls");
					OutputStream outStream = response.getOutputStream();
					outStream.write(outArray);
					outStream.flush();
				}
			}
		}
	}

}
