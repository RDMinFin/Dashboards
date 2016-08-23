package dao.transparencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import db.utilities.CDatabase;
import pojo.transparencia.CCompra;
import utilities.CLogger;

public class CCompraDAO {
	public static Integer numCompras(){
		Integer ret=0;
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("select count(*) from seg_compra");
				ResultSet rs = pstm.executeQuery();
				if (rs.next())
					ret=rs.getInt(1);
			}
			catch(Exception e){
				CLogger.write("1", CCompraDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return ret;		
	}
	
	public static ArrayList<CCompra> getCompras(){
		ArrayList<CCompra> ret=new ArrayList<CCompra>();
		if(CDatabase.connect() && CDatabase.connectOracle()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("SELECT * FROM seg_compra WHERE programa=? and subprograma=?");
				pstm.setInt(1, 94);
				pstm.setInt(2, 2);
				ResultSet rs=pstm.executeQuery();
				PreparedStatement pstm2=null;
				ResultSet rs2;
				CCompra compra;
				while (rs.next()){
					if (rs.getString("NPG")!=null){
						pstm2 = CDatabase.getConnection_Oracle().prepareStatement("select * from GUATECOMPRAS.VW_GC_NPG_JUTIAPA2016 where NPG_CONCURSO=?");
						pstm2.setString(1,rs.getString("NPG"));
						rs2 = pstm2.executeQuery();
						if (rs2.next()){
							compra = new CCompra(rs2.getString("ENTIDAD_GC"), rs2.getString("UNIDAD_GC"),"NPG", rs2.getString("NPG_CONCURSO"), rs2.getTimestamp("FECHA_PUBLICACION_GC"), rs2.getString("DESCRIPCION"), rs2.getString("NOMBRE_MODALIDAD")+" - "+rs2.getString("NOMBRE_MODALIDAD_EJECUCION"),rs2.getString("NOMBRE_ESTATUS"), rs2.getString("NIT"), rs2.getString("NOMBRE"), rs2.getDouble("MONTO"));
							ret.add(compra);
						}					
					}else if (rs.getInt("NOG")>0){
						pstm2 = CDatabase.getConnection_Oracle().prepareStatement("select * from GUATECOMPRAS.VW_GC_CONCURSOS_JUTIAPA2016 where NOG_CONCURSO=?");
						pstm2.setString(1,rs.getString("NOG"));
						rs2 = pstm2.executeQuery();
						if (rs2.next()){
							compra = new CCompra(rs2.getString("ENTIDAD_GC"), rs2.getString("UNIDAD_GC"),"NOG", rs2.getString("NOG_CONCURSO"), rs2.getTimestamp("FECHA_PUBLICACION"), rs2.getString("DESCRIPCION"), rs2.getString("MODALIDAD_COMPRA"),rs2.getString("ESTATUS_CONCURSO"), rs2.getString("NIT"), rs2.getString("NOMBRE_PROVEEDOR"), rs2.getDouble("MONTO"));
							ret.add(compra);
						}		
					}					
				}
			}
			catch(Exception e){
				CLogger.write("2", CCompraDAO.class, e);
			}
			finally{
				CDatabase.close();
				CDatabase.close_oracle();
			}
		}
		return ret;		
	}

}
