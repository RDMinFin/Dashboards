package dao.transparencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.joda.time.DateTime;

import com.mysql.jdbc.Statement;

import db.utilities.CDatabase;
import pojo.transparencia.CResponsable;
import utilities.CLogger;

public class CResponsableDAO {
	public static CResponsable crearResponsable(String nombre, String correo, String telefono,String usuario){
		CResponsable responsable=null;
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("INSERT INTO "
						+ "seg_responsable(nombre, correo, telefono, fecha_creacion, usuario_creacion) "
						+ "VALUES (?,?,?,?,?) " , Statement.RETURN_GENERATED_KEYS);
				pstm.setString(1, nombre);
				pstm.setString(2, correo);
				pstm.setString(3, telefono);
				pstm.setTimestamp(4, new Timestamp(DateTime.now().getMillis()));
				pstm.setString(5, usuario);
				pstm.executeUpdate();
				ResultSet rs = pstm.getGeneratedKeys();
				if (rs != null && rs.next()) {
				    responsable = new CResponsable(rs.getLong(1), nombre, correo, telefono);
				}
			}
			catch(Exception e){
				CLogger.write("1", CResponsableDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return responsable
				;		
	}
	
	public static CResponsable getResponsable(Integer id){
		CResponsable responsable=null;
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("SELECT * FROM "
						+ "seg_responsable WHERE id=?");
				pstm.setInt(1, id);
				ResultSet rs = pstm.executeQuery();
				if (rs != null && rs.next()) {
				    responsable = new CResponsable(id, rs.getString("nombre"), rs.getString("correo"), rs.getString("telefono"));
				}
			}
			catch(Exception e){
				CLogger.write("2", CResponsableDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return responsable;
	}
}
