package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabase;
import pojo.CEjecucion;
import utilities.CLogger;

public class CPaptnDAO {
	
	
	public static ArrayList<CEjecucion> getEntidadesEjecucion(int mes){
		final ArrayList<CEjecucion> entidades=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("select * from mv_paptn_tablaentidades where mes = ? order by entidad");
				pstm1.setInt(1, mes);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CEjecucion entidad = new CEjecucion(null, results.getInt("entidad"), results.getString("nombre"), results.getDouble("gp_ejecucion"), 
							results.getDouble("gp_vigente"), results.getDouble("gp_porcentaje"), results.getDouble("paptn_ejecucion"), results.getDouble("paptn_vigente"), 
							results.getDouble("paptn_porcentaje"), null, null, null, null,null);
					entidades.add(entidad);
				}
				results.close();
				pstm1.close();
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("1", CPaptnDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return entidades.size()>0 ? entidades : null;
	}
	
	public static ArrayList<CEjecucion> getEjesEjecucion(int mes){
		final ArrayList<CEjecucion> entidades=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("select * from mv_paptn_tablaejes where mes = ? order by iorder");
				pstm1.setInt(1, mes);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CEjecucion entidad = new CEjecucion(results.getInt("eje"), results.getInt("linea"), results.getString("eje_nombre"), 
							results.getString("linea_nombre"), results.getString("eje_nombre_corto"), results.getDouble("aprobado"), 
							results.getDouble("modificaciones"), results.getDouble("vigente"), results.getDouble("ejecucion"), results.getDouble("porcentaje"), 
							null, null, null, null, null,null);
					entidades.add(entidad);
				}
				results.close();
				pstm1.close();
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("2", CPaptnDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return entidades.size()>0 ? entidades : null;
	}
	
	public static Double[] getEstructurasFinanciamiento(int year){
		Double[] ret=null;
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("select * from mv_paptn_estructura_financiamiento where ejercicio = ?");
				pstm1.setInt(1, year);
				ResultSet results = pstm1.executeQuery();	
				if (results.next()){
					ret = new Double[5];
					ret[0] = (double) results.getInt("ejercicio");
					ret[1] = results.getDouble("tributarias");
					ret[2] = results.getDouble("prestamos_externos");
					ret[3] = results.getDouble("donaciones");
					ret[4] = results.getDouble("otras");
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("3", CPaptnDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return ret;
	}
}
