package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import db.utilities.CDatabase;
import pojo.CMunicipio;
import utilities.CLogger;

public class CMunicipioDAO {
	CMunicipio municipio;
	
	public static CMunicipio getTown(Integer id){
		CMunicipio ret=null;
		try{
			if(CDatabase.connect()){
				PreparedStatement pstm = CDatabase.getConnection().prepareStatement("select * from municipio where id = ?");
				pstm.setInt(1, id);
				ResultSet results = pstm.executeQuery();
				if(results.next()){
					ret = new CMunicipio(id,results.getString("nombre_mayuscula"), results.getString("nombre"), results.getString("latitud"), results.getString("longitud"),
							results.getInt("codigo_departamento"));
				}
				results.close();
				pstm.close();
				CDatabase.close();
			}
		}
		catch(Exception e){
			
		}
		return ret;	
	}
	
	public static ArrayList<CMunicipio> getMunicipios(){
		final ArrayList<CMunicipio> towns=new ArrayList<CMunicipio>();
		if(CDatabase.connect()){
			try{
				Statement statement =  CDatabase.getConnection().createStatement();
				ResultSet results = statement.executeQuery("select * from municipio order by nombre");
				while (results.next()){
					CMunicipio town = new CMunicipio(results.getInt("codigo"),results.getString("nombre_mayuscula"), results.getString("nombre"), results.getString("latitud"), results.getString("longitud"),
							results.getInt("codigo_departamento"));
					towns.add(town);
				}
				results.close();
			}
			catch(Exception e){
				CLogger.write("1", CMunicipioDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		if(towns.size()==0)
			return null;
		
		return towns;
	}
}
