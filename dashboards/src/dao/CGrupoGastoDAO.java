package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabase;
import pojo.CGrupoGasto;
import utilities.CLogger;

public class CGrupoGastoDAO {
CGrupoGasto grupoGasto;
	
	public static ArrayList<CGrupoGasto> getGruposGasto(int ejercicio){
		final ArrayList<CGrupoGasto> gruposGasto=new ArrayList<CGrupoGasto>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("select grupo_gasto, nombre from cp_grupos_gasto where ejercicio=? order by grupo_gasto");
				pstm1.setInt(1, ejercicio);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CGrupoGasto grupogasto = new CGrupoGasto(ejercicio, results.getInt("grupo_gasto"), results.getString("nombre"));
					gruposGasto.add(grupogasto);
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("1", CGrupoGastoDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return gruposGasto.size()>0 ? gruposGasto : null;
	}
}
