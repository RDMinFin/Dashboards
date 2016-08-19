package dao.transparencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.joda.time.DateTime;

import com.mysql.jdbc.Statement;

import db.utilities.CDatabase;
import pojo.transparencia.CActividad;
import pojo.transparencia.CEstructura;
import utilities.CLogger;

public class CActividadDAO {
	
	public static boolean crearActividad(CActividad actividad,String usuario){
		boolean ret=false;
		if(CDatabase.connect()){
			try{
				long idResponsable=-1;
				PreparedStatement pstm;
				if (actividad.getResponsable().getId()>0)
					idResponsable=actividad.getResponsable().getId();
				else{					
					pstm =  CDatabase.getConnection().prepareStatement("INSERT INTO "
							+ "seg_responsable(nombre, correo, telefono, fecha_creacion, usuario_creacion) "
							+ "VALUES (?,?,?,?,?) " , Statement.RETURN_GENERATED_KEYS);
					pstm.setString(1, actividad.getResponsable().getNombre());
					pstm.setString(2, actividad.getResponsable().getCorreo());
					pstm.setString(3, actividad.getResponsable().getTelefono());
					pstm.setTimestamp(4, new Timestamp(DateTime.now().getMillis()));
					pstm.setString(5, usuario);
					pstm.executeUpdate();
					ResultSet rs = pstm.getGeneratedKeys();
					if (rs != null && rs.next()) {
					    idResponsable = rs.getLong(1);								
						pstm =  CDatabase.getConnection().prepareStatement("INSERT INTO "
								+"seg_actividad(nombre,descripcion,fecha_inicio,fecha_fin,porcentaje_ejecucion,coord_lat,coord_long,entidades,id_responsable,programa,subprgrama,usuario_creacion,fecha_creacion) "
								+"VALUES (?,?,?,?,?,?,?,?,?,?,?) ");
						pstm.setString(1, actividad.getNombre());
						pstm.setString(2, actividad.getDescripcion());
						pstm.setTimestamp(3, actividad.getFecha_inicio());
						pstm.setTimestamp(4, actividad.getFecha_fin());
						pstm.setDouble(5, actividad.getPorcentaje_ejecucion());
						pstm.setString(6, actividad.getCoord_lat());
						pstm.setString(7, actividad.getCoord_long());
						pstm.setString(8, actividad.getEntidades());
						pstm.setLong(9, idResponsable);
						pstm.setInt(10,94);
						pstm.setInt(11,2);
						pstm.setString(12, usuario);
						pstm.setTimestamp(13, new Timestamp(DateTime.now().getMillis()));
						if (pstm.executeUpdate()>0)
							ret=true;
					}
				}
			}
			catch(Exception e){
				CLogger.write("1", CActividadDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return ret;		
	}
	
	public static boolean actualizarActividad(CActividad actividad,String usuario){
		boolean ret=false;
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("UPDATE seg_actividad SET "
						+ "NOMBRE = ?, "
						+ "DESCRIPCION = ?, "
						+ "FECHA_INICIO = ?, "
						+ "FECHA_FIN = ?, "
						+ "COORD_LAT=?, "
						+ "COORD_LONG=?, "
						+ "PORCENTAJE_EJECUCION = ?, "
						+ "ENTIDADES = ?,"
						+ "usuario_actualizacion=?, fecha_actualizacion=? "
						+ "WHERE id = ?");
				pstm.setString(1, actividad.getNombre());
				pstm.setString(2, actividad.getDescripcion());
				pstm.setTimestamp(3, actividad.getFecha_inicio());
				pstm.setTimestamp(4, actividad.getFecha_fin());
				pstm.setString(5, actividad.getCoord_lat());
				pstm.setString(6, actividad.getCoord_long());
				pstm.setDouble(7, actividad.getPorcentaje_ejecucion());
				pstm.setString(8, actividad.getEntidades());
				pstm.setString(9, usuario);
				pstm.setTimestamp(10, new Timestamp(DateTime.now().getMillis()));
				pstm.setInt(11, actividad.getId());
				if (pstm.executeUpdate()>0)
					ret=true;
				pstm = CDatabase.getConnection().prepareStatement("UPDATE seg_responsable SET "
						+ "NOMBRE = ?, "
						+ "CORREO = ?, "
						+ "TELEFONO = ?, "
						+ "usuario_actualizacion=?, fecha_actualizacion=? "
						+ "WHERE id = ?");
				pstm.setString(1, actividad.getResponsable().getNombre());
				pstm.setString(2, actividad.getResponsable().getCorreo());
				pstm.setString(3, actividad.getResponsable().getTelefono());
				pstm.setString(4, usuario);
				pstm.setTimestamp(5, new Timestamp(DateTime.now().getMillis()));
				pstm.setLong(6, actividad.getResponsable().getId());
				if (pstm.executeUpdate()>0)
					ret=ret&true;
			}
			catch(Exception e){
				CLogger.write("2", CActividadDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return ret;		
	}
	
	public static boolean eliminarActividad(CActividad actividad){
		boolean ret=false;
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("delete from seg_responsable where "
						+ "id = ? ");
				pstm.setLong(1, actividad.getResponsable().getId());
				if (pstm.executeUpdate()>0)
					ret=true;
				pstm = CDatabase.getConnection().prepareStatement("delete from seg_actividad "
						+ "WHERE id = ?");
				pstm.setLong(1, actividad.getId());
				if (pstm.executeUpdate()>0)
					ret=ret&true;
			}
			catch(Exception e){
				CLogger.write("3", CActividadDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return ret;		
	}
	
	public static Integer numActividades(){
		Integer ret = 0;
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("SELECT COUNT(*) FROM seg_actividad where subprograma=2");
				ResultSet rs = pstm.executeQuery();
				if (rs.next())
					ret=rs.getInt(1);
			}
			catch(Exception e){
				CLogger.write("4", CActividadDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return ret;
	}
	
	public static ArrayList<CActividad> getActividades(){
		ArrayList<CActividad> ret = new ArrayList<CActividad>();
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("SELECT * FROM seg_actividad where programa=94 and subprograma=2 ORDER BY id ");
				ResultSet rs = pstm.executeQuery();
				while(rs.next()){
					CActividad actividad = new CActividad(rs.getInt("id"), rs.getString("nombre"), 
							rs.getString("descripcion"), rs.getTimestamp("fecha_inicio"), rs.getTimestamp("fecha_fin"), 
							rs.getDouble("porcentaje_ejecucion"), rs.getString("coord_lat"), 
							rs.getString("coord_long"), rs.getString("entidades"), 
							CResponsableDAO.getResponsable(rs.getInt("id_responsable")),
							new CEstructura(rs.getInt("entidad"),rs.getInt("unidad_ejecutora"),rs.getInt("programa"),rs.getInt("subprograma"),rs.getInt("proyecto"),rs.getInt("actividad"),rs.getInt("obra"))
							, CDocumentoDAO.getDocumentosActividad(rs.getInt("id")));
					ret.add(actividad);
				}
			}
			catch(Exception e){
				CLogger.write("5", CActividadDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return ret;
	}

	public static CActividad getActividad(int id) {
		CActividad ret = null;
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("SELECT * FROM seg_actividad WHERE id=? ");
				pstm.setInt(1, id);
				ResultSet rs = pstm.executeQuery();
				if(rs.next()){
					ret = new CActividad(rs.getInt("id"), rs.getString("nombre"), 
							rs.getString("descripcion"), rs.getTimestamp("fecha_inicio"), rs.getTimestamp("fecha_fin"), 
							rs.getDouble("porcentaje_ejecucion"), rs.getString("coord_lat"), 
							rs.getString("coord_long"), rs.getString("entidades"), 
							CResponsableDAO.getResponsable(rs.getInt("id_responsable")),
							new CEstructura(rs.getInt("entidad"),rs.getInt("unidad_ejecutora"),rs.getInt("programa"),rs.getInt("subprograma"),rs.getInt("proyecto"),rs.getInt("actividad"),rs.getInt("obra"))
							, CDocumentoDAO.getDocumentosActividad(rs.getInt("id")));
				}
			}
			catch(Exception e){
				CLogger.write("5", CActividadDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return ret;
	}

	

}