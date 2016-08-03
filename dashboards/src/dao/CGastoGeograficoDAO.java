package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabase;
import pojo.CGastoGeografico;
import utilities.CLogger;

public class CGastoGeograficoDAO {
	
	public static ArrayList<CGastoGeografico> getGastoGeograficoPuntos(int mes){
		final ArrayList<CGastoGeografico> geograficos=new ArrayList<CGastoGeografico>();
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("select gg.*, m.latitud, m.longitud "+
						"from vw_gasto_geografico_puntos gg, municipio m "+
						"where gg.geografico = m.codigo and gg.mes=? and gg.ejercicio=2016");
				pstm.setInt(1, mes);
				ResultSet results = pstm.executeQuery();
				while (results.next()){
					Float puntos = results.getFloat("puntos");
					CGastoGeografico gg = new CGastoGeografico(2016, results.getInt("geografico"), mes, puntos.intValue(), 
							results.getString("latitud"), results.getString("longitud"));
					geograficos.add(gg);
				}
				results.close();
				pstm.close();
			}
			catch(Exception e){
				CLogger.write("1", CGastoGeograficoDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return geograficos.size()>0 ? geograficos : null;
	}
}
