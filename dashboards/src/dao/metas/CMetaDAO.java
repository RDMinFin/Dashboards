package dao.metas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.utilities.CDatabase;
import pojo.metas.CMeta;
import utilities.CLogger;

public class CMetaDAO {
	
	public static ArrayList<CMeta> getMetasEntidades(int ano, int mes){
		ArrayList<CMeta> ret = new ArrayList<CMeta>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("select mp.entidad, mp.entidad_nombre, "
						+ " sum( case when mp.codigo_meta=1 then  mp.vigente_"+mes+" else 0 end) vigente, "
					    + " sum(case when mp.codigo_meta=1 then mp.asignado else 0 end) asignado, "
					    + " sum( case when mp.codigo_meta=1 then (mp.vigente_"+mes+" - mp.asignado) else 0 end ) modificaciones, sum( case "+
																									" when ((mp.mes <= "+mes+") and (mp.codigo_meta=1))  then mp.gasto_total "+
																								" else 0 end ) gasto, "+ 
							 " sum( case when (mp.mes <= "+mes+") then mp.meta_cantidad_unidades_avance else 0 end ) fisica, "+ 
							 " (avg(mp.meta_cantidad)+avg(mp.meta_adicion)+avg(mp.meta_disminucion)) meta, v.vigente_entidad "+
					 " from mv_meta_presidencial mp, ( "+
					 	" select ejercicio, entidad, sum(vigente_"+mes+") vigente_entidad "+
						" from minfin.vw_vigente_entidad "+ 
						" group by ejercicio, entidad "+
					 ") v " +
					 "where mp.ejercicio = ? "+
					 "and mp.mes = ? "+
					 "and v.ejercicio = mp.ejercicio "+
					 "and v.entidad = mp.entidad "+
					 "group by mp.entidad, mp.entidad_nombre ");
				pstm1.setInt(1, ano);
				pstm1.setInt(2, mes);
				ResultSet rs = pstm1.executeQuery();	
				while (rs.next()){
					CMeta temp = new CMeta(rs.getInt("entidad"), rs.getString("entidad_nombre"), rs.getDouble("asignado"), rs.getDouble("vigente"), rs.getDouble("gasto"), rs.getDouble("modificaciones"), rs.getLong("fisica"), rs.getLong("meta"), rs.getDouble("vigente_entidad"),1);
					ret.add(temp);
				}
				rs.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("1", CMetaDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return ret;
	}
	
	public static ArrayList<CMeta> getMetasMetas(int ano, int mes){
		ArrayList<CMeta> ret = new ArrayList<CMeta>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("select id, nombre, sum( case when codigo_meta=1 then vigente_"+mes+" else 0 end) vigente, "
						+ "sum(case when codigo_meta=1 then asignado else 0 end ) asignado, "
						+ "sum(case when codigo_meta=1 then (vigente_"+mes+" - asignado) else 0 end) modificaciones, sum( case "+ 
																									" when ((mes <= "+mes+") and (codigo_meta=1))  then gasto_total "+
																								"else 0 end ) gasto, "+
							" sum( case when (mes <= "+mes+") then meta_cantidad_unidades_avance else 0 end ) fisica, "+
							" (avg(meta_cantidad)+avg(meta_adicion)+avg(meta_disminucion)) meta "+	 
					" from mv_meta_presidencial "+
					" where ejercicio = ? "+
					" and mes = ? "+
					" group by id, nombre ");
				pstm1.setInt(1, ano);
				pstm1.setInt(2, mes);
				ResultSet rs = pstm1.executeQuery();	
				while (rs.next()){
					CMeta temp = new CMeta(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("asignado"), rs.getDouble("vigente"), rs.getDouble("gasto"), rs.getDouble("modificaciones"), rs.getLong("fisica"), rs.getLong("meta"), 0.0d, 1);
					ret.add(temp);
				}
				rs.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("1", CMetaDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return ret;
	}

	public static ArrayList<Double[]> getEjecucionFinancieraFisicaMetasMeses(int ano, int mes) {
		ArrayList<Double[]> ret = new ArrayList<Double[]>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("select  mes, sum( case when codigo_meta=1 then gasto_total else 0 end ) gasto, sum( case when codigo_meta=1 then ( " + 
						"	case mes " + 
						"		when 1 then vigente_1 " + 
						"		when 2 then vigente_2 " + 
						"		when 3 then vigente_3 " + 
						"		when 4 then vigente_4 " + 
						"		when 5 then vigente_5 " + 
						"		when 6 then vigente_6 " + 
						"		when 7 then vigente_7 " + 
						"		when 8 then vigente_8 " + 
						"		when 9 then vigente_9 " + 
						"		when 10 then vigente_10 " + 
						"		when 11 then vigente_11 " + 
						"		when 12 then vigente_12 end " + 
						") end) vigente, sum(meta_cantidad_unidades_avance) avance, sum(meta_cantidad+meta_adicion+meta_disminucion) meta " + 
						"from mv_meta_presidencial mp " + 
						"where mp.ejercicio = ? " + 
						"and mes<=? " + 
						"group by mes");
				pstm1.setInt(1, ano);
				pstm1.setInt(2, mes);
				ResultSet rs = pstm1.executeQuery();	
				while (rs.next()){
					Double[] list = new Double[5];
					list[0] = rs.getInt("mes")+0.0d;
					list[1] = rs.getDouble("gasto")/1000000;
					list[2] = rs.getDouble("vigente")/1000000;
					list[3] = rs.getDouble("avance")/1000000;
					list[4] = rs.getDouble("meta")/1000000;
					ret.add(list);
				}
				rs.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("2", CMetaDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return ret;
	}
	
}
