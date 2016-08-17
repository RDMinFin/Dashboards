package dao.transparencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabase;
import pojo.transparencia.CDocumento;
import utilities.CLogger;

public class CDocumentoDAO {
	
	public static boolean crearDocumento(CDocumento documento){
		boolean ret=false;
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("INSERT INTO seg_documento"
						+ "(id_actividad,nombre, titulo,ruta,tipo,usuario_creacion,fecha_creacion) "
						+ "values (?,?,?,?,?,?,?)");
				if (documento.getId_actividad()>0)
					pstm.setInt(1, documento.getId_actividad());
				else					
					pstm.setNull(1,java.sql.Types.INTEGER);
				pstm.setString(2, documento.getNombre());
				pstm.setString(3, documento.getTitulo());
				pstm.setString(4, documento.getRuta());
				pstm.setInt(5, documento.getTipo());
				pstm.setString(6, documento.getUsuario_creacion());
				pstm.setTimestamp(7, documento.getFecha_creacion());
				if (pstm.executeUpdate()>0)
					ret=true;
			}
			catch(Exception e){
				CLogger.write("1", CDocumentoDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return ret;		
	}
	
	public static boolean deleteDocumento(int id){
		boolean ret=false;
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("DELETE FROM seg_documento "
						+ "WHERE id =  " + id);
				if (pstm.executeUpdate()>0)
					ret=true;
			}
			catch(Exception e){
				CLogger.write("2", CDocumentoDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return ret;		
	}
	
	
	public static Integer numDocumentos(){
		Integer ret=0;
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("select count(*) from seg_documento ");
				ResultSet rs = pstm.executeQuery();
				if (rs.next())
					ret=rs.getInt(1);
			}
			catch(Exception e){
				CLogger.write("3", CDocumentoDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return ret;		
	}
	
	public static ArrayList<CDocumento> getDocumentosActividad(Integer id_actividad){
		ArrayList<CDocumento> ret=new ArrayList<CDocumento>();
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("SELECT * FROM seg_documento WHERE id_actividad=? ");
				pstm.setInt(1, id_actividad);
				ResultSet rs=pstm.executeQuery();
				while (rs.next()){
					CDocumento documento = new CDocumento(rs.getInt("id"), id_actividad, rs.getString("nombre"), rs.getString("titulo"), 
							rs.getString("ruta"), rs.getInt("tipo"), rs.getTimestamp("fecha_creacion"), rs.getString("usuario_creacion"));
					ret.add(documento);
				}
			}
			catch(Exception e){
				CLogger.write("4", CDocumentoDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return ret;		
	}

	public static ArrayList<CDocumento> getDocumentos(int id) {
		ArrayList<CDocumento> ret =new ArrayList<CDocumento>();
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("select * from seg_documento "
						+ (id>0? "where id_actividad="+id : "" ) 
						+ " order by fecha_creacion ");
				ResultSet rs=pstm.executeQuery();
				while (rs.next()){
					CDocumento documento = new CDocumento(rs.getInt("id"), rs.getInt("id_actividad"), rs.getString("nombre"), rs.getString("titulo"), 
							rs.getString("ruta"), rs.getInt("tipo"), rs.getTimestamp("fecha_creacion"), rs.getString("usuario_creacion"));
					ret.add(documento);
				}
			}
			catch(Exception e){
				CLogger.write("5", CDocumentoDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return ret;
	}

	public static CDocumento getDocumento(int id_documento) {
		CDocumento ret = null;
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("SELECT * FROM seg_documento WHERE id = ?");
				pstm.setInt(1,id_documento);
				ResultSet rs=pstm.executeQuery();
				if (rs.next()){
					ret = new CDocumento(rs.getInt("id"), rs.getInt("id_actividad"), rs.getString("nombre"), rs.getString("titulo"), 
							rs.getString("ruta"), rs.getInt("tipo"), rs.getTimestamp("fecha_creacion"), rs.getString("usuario_creacion"));
				}
			}
			catch(Exception e){
				CLogger.write("6", CDocumentoDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return ret;
	}
}
