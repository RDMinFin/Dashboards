package dao.transparencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.joda.time.DateTime;

import db.utilities.CDatabase;
import pojo.transparencia.CEjecucionFF;
import utilities.CLogger;

public class CEjecucionFFDAO {
	
	public static ArrayList<CEjecucionFF> getEntidadesEjecucion(int nivel, Integer entidad, Integer unidad_ejecutora, Integer programa,Integer subprograma, Integer proyecto, Integer actividad){
		final ArrayList<CEjecucionFF> datos=new ArrayList<CEjecucionFF>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				DateTime now = new DateTime();
				PreparedStatement  pstm1;
				pstm1 = conn.prepareStatement("select fn.entidad, fn.entidad_nombre "+
							( nivel > 1 ? ", fn.unidad_ejecutora, fn.unidad_ejecutora_nombre " : "" )+
							( nivel > 2 ? ", fn.proyecto, fn.proyecto_nombre " : "" )+
							( nivel > 3 ? ", fn.actividad + fn.obra actividad, fn.actividad_nombre " : "" )+
							( nivel > 4 ? ", fn.RENGLON, fn.renglon_NOMBRE " : "" )+
							", sum(fn.vigente) vigente, sum(fn.ejecutado) ejecutado, sum(fn.meta_avanzado) meta_avanzado, sum(fn.meta) meta "+
							"from calamidad_ejecucion fn where fn.ejercicio ="+ now.getYear() + " " +
							( nivel > 1 ? "and entidad = "+entidad+" " : "" )+
							( nivel > 2 ? "and unidad_ejecutora = "+unidad_ejecutora+" " : "")+
							( nivel > 3 ? "and proyecto = "+proyecto+" " : "")+
							( nivel > 4 ? "and ( (actividad = "+actividad+" and obra = 0) or (actividad=0 and obra="+actividad+")) " : "")+
							"and fn.programa="+programa+" and fn.subprograma="+subprograma+" "+
							"group by fn.entidad, fn.entidad_nombre "+
							( nivel > 1 ? ", fn.unidad_ejecutora, fn.unidad_ejecutora_nombre ": "" )+
							( nivel > 2 ? ", fn.proyecto, fn.proyecto_nombre ":"")+
							( nivel > 3 ? ", fn.actividad, fn.obra, fn.actividad_nombre ":"")+
							( nivel > 4 ? ", fn.RENGLON, fn.renglon_NOMBRE ":""));
				ResultSet results = pstm1.executeQuery();	
				Integer codigo = null;
				String  nombre = "";
				while (results.next()){
				switch(nivel){
					case 1: nombre = results.getString("entidad_nombre"); codigo=results.getInt("entidad"); break;
					case 2: nombre = results.getString("unidad_ejecutora_nombre"); codigo=results.getInt("unidad_ejecutora"); break;
					case 3: nombre = results.getString("proyecto_nombre"); codigo=results.getInt("proyecto"); break;
					case 4: nombre = results.getString("actividad_nombre"); codigo=results.getInt("actividad"); break;
					case 5: nombre = results.getString("renglon_nombre"); codigo=results.getInt("renglon"); break;
					}
				CEjecucionFF dato = new CEjecucionFF(codigo,nombre
						, results.getDouble("ejecutado"), results.getDouble("vigente")
						, results.getDouble("vigente")>0?results.getDouble("ejecutado")/results.getDouble("vigente"):0.0
						, results.getDouble("meta"), results.getDouble("meta_avanzado")							
						, results.getDouble("meta")>0?results.getDouble("meta_avanzado")/results.getDouble("meta"):0.0);
					datos.add(dato);
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("1", CEjecucionFFDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return datos.size()>0 ? datos : null;
	}
				
	public static ArrayList<CEjecucionFF> getProgramasEjecucion(int nivel, Integer entidad, Integer unidad_ejecutora, Integer programa,Integer subprograma, Integer proyecto, Integer actividad){
		final ArrayList<CEjecucionFF> datos=new ArrayList<CEjecucionFF>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				DateTime now = new DateTime();
				PreparedStatement  pstm1;
				pstm1 = conn.prepareStatement("select fn.programa, fn.programa_nombre "+
					( nivel > 1 ? ", fn.subprograma, fn.subprograma_nombre " : "" )+
					( nivel > 2 ? ", fn.proyecto, fn.proyecto_nombre " : "" )+
					( nivel > 3 ? ", fn.actividad + fn.obra actividad, fn.actividad_nombre " : "" )+
					( nivel > 4 ? ", fn.RENGLON, fn.renglon_NOMBRE " : "" )+
					", sum(fn.vigente) vigente, sum(fn.ejecutado) ejecutado, sum(meta_avanzado) meta_avanzado, sum(meta) meta "+
					"from calamidad_ejecucion fn where fn.ejercicio ="+ now.getYear() + " " +
					"and programa = "+programa+" and subprograma = "+subprograma+" " +
					( nivel > 3 ? "and proyecto = "+proyecto+" " : "")+
					( nivel > 4 ? "and ( (actividad = "+actividad+" and obra = 0) or (actividad=0 and obra="+actividad+")) " : "")+
					"group by fn.programa, fn.programa_nombre "+
					( nivel > 1 ? ", fn.subprograma, fn.subprograma_nombre ": "" )+
					( nivel > 2 ? ", fn.proyecto, fn.proyecto_nombre ":"")+
					( nivel > 3 ? ", fn.actividad, fn.obra, fn.actividad_nombre ":"")+
					( nivel > 4 ? ", fn.RENGLON, fn.renglon_NOMBRE ":""));
				ResultSet results = pstm1.executeQuery();	
				Integer codigo = null;
				String  nombre = "";
				while (results.next()){
				switch(nivel){
					case 1: nombre = results.getString("programa_nombre"); codigo=results.getInt("programa"); break;
					case 2: nombre = results.getString("subprograma_nombre"); codigo=results.getInt("subprograma"); break;
					case 3: nombre = results.getString("proyecto_nombre"); codigo=results.getInt("proyecto"); break;
					case 4: nombre = results.getString("actividad_nombre"); codigo=results.getInt("actividad"); break;
					case 5: nombre = results.getString("renglon_nombre"); codigo=results.getInt("renglon"); break;
					}
				CEjecucionFF dato = new CEjecucionFF(codigo,nombre
						, results.getDouble("ejecutado"), results.getDouble("vigente")
						, results.getDouble("vigente")>0?results.getDouble("ejecutado")/results.getDouble("vigente"):0.0
						, results.getDouble("meta"), results.getDouble("meta_avanzado")							
						, results.getDouble("meta")>0?results.getDouble("meta_avanzado")/results.getDouble("meta"):0.0);
					datos.add(dato);
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("2", CEjecucionFFDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return datos.size()>0 ? datos : null;
	}
	
	
	public static double ejecucionFinanciera(int programa, int subprograma){
		double ret = 0.0;
		double vigente = 0.0;
		double ejecutado = 0.0;
		try{
			if(CDatabase.connect()){
				Connection conn = CDatabase.getConnection();
				DateTime now = new DateTime();
				PreparedStatement  pstm1;
				pstm1 = conn.prepareStatement("select sum(vigente) vigente, sum(ejecutado) ejecutado, sum(meta) meta, "
						+ "sum(meta_avanzado) meta_avanzado "
						+ "from calamidad_ejecucion "
						+ "where ejercicio="+now.getYear()+" and programa="+programa+" and subprograma="+subprograma);		
				ResultSet rs = pstm1.executeQuery();	
				if (rs.next()){
					vigente = rs.getDouble(1);
					ejecutado = rs.getDouble(2);
				}
				pstm1.close();
				rs.close();				
				CDatabase.close(conn);
			}			
		}
		catch(Exception e){
			CLogger.write("3", CEjecucionFFDAO.class, e);
		}
		ret = vigente > 0 ? (ejecutado/vigente)*100 : 0.0;
		return ret;
	}
	
	public static double ejecucionFisica(int programa, int subprograma){
		double ret = 0.0;
		try{
			if(CDatabase.connect()){
				Connection conn = CDatabase.getConnection();
				DateTime now = new DateTime();
				PreparedStatement  pstm1;
				pstm1 = conn.prepareStatement("select avg(porcentaje_ejecucion) porcentaje_ejecucion "
						+ "from seg_actividad "
						+ "where ejercicio="+now.getYear()+" and programa="+programa+" and subprograma="+subprograma);		
				ResultSet rs = pstm1.executeQuery();	
				if (rs.next()){
					ret = rs.getDouble(1);
				}
				pstm1.close();
				rs.close();				
				CDatabase.close(conn);
			}			
		}
		catch(Exception e){
			CLogger.write("4", CEjecucionFFDAO.class, e);
		}
		return ret;
	}
}
