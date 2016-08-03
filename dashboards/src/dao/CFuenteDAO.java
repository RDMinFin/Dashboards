package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabase;
import pojo.CFuente;
import utilities.CLogger;

public class CFuenteDAO {
	CFuente fuente;
	
	public static ArrayList<CFuente> getFuentes(int ejercicio){
		final ArrayList<CFuente> fuentes=new ArrayList<CFuente>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("select fuente, nombre from cg_fuentes where ejercicio=? order by fuente");
				pstm1.setInt(1, ejercicio);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CFuente fuente = new CFuente(ejercicio, results.getInt("fuente"), results.getString("nombre"));
					fuentes.add(fuente);
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("1", CFuenteDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return fuentes.size()>0 ? fuentes : null;
	}
}
