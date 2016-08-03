package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.joda.time.DateTime;

import db.utilities.CDatabase;
import pojo.CEjecucion;
import utilities.CLogger;

public class CEjecucionDAO { 
	
	public static ArrayList<CEjecucion> getEntidadesEjecucion(int mes, String fuentes, String clase_registro, String gruposGasto, boolean todosgrupos){
		final ArrayList<CEjecucion> entidades=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				DateTime now = new DateTime();
				PreparedStatement pstm1 =  conn.prepareStatement("select e.entidad, e.nombre, "+
					"SUM(CASE WHEN (g.ejercicio=?-5 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_1, "+
					"SUM(CASE WHEN (g.ejercicio=?-4 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_2, "+
					"SUM(CASE WHEN (g.ejercicio=?-3 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_3, "+
					"SUM(CASE WHEN (g.ejercicio=?-2 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_4, "+
					"SUM(CASE WHEN (g.ejercicio=?-1 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_5, "+
					"SUM(CASE WHEN (g.ejercicio=? AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_actual, "+
					"SUM(CASE WHEN (g.ejercicio=? AND g.mes<=?) THEN g.total ELSE 0 END) acumulado_actual "+
					"from cg_entidades e LEFT OUTER JOIN vw_gasto_renglon g "
						+ "ON (e.entidad = g.entidad and g.fuente IN ("+fuentes+")  and g.clase_registro IN ("+clase_registro+") "
						+ " and ((g.renglon - mod( g.renglon,10)) - (mod(((g.renglon - mod( g.renglon,10))) ,100)) in ("+gruposGasto+")) )  "+
					"where e.ejercicio = ? "+
					"and ((e.entidad BETWEEN 11130000 AND 11130020) OR (e.entidad = 11140021))  "+
					"group by e.entidad, e.nombre "+
					"order by e.entidad, e.nombre");				
				pstm1.setInt(1, now.getYear());
				pstm1.setInt(2, mes);
				pstm1.setInt(3, now.getYear());
				pstm1.setInt(4, mes);
				pstm1.setInt(5, now.getYear());
				pstm1.setInt(6, mes);
				pstm1.setInt(7, now.getYear());
				pstm1.setInt(8, mes);
				pstm1.setInt(9, now.getYear());
				pstm1.setInt(10, mes);
				pstm1.setInt(11, now.getYear());
				pstm1.setInt(12, mes);
				pstm1.setInt(13, now.getYear());
				pstm1.setInt(14, mes);
				pstm1.setInt(15, now.getYear());
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CEjecucion entidad = new CEjecucion(null, results.getInt("entidad"), results.getString("nombre"), results.getDouble("ejecutado_1"), 
							results.getDouble("ejecutado_2"), results.getDouble("ejecutado_3"), results.getDouble("ejecutado_4"), results.getDouble("ejecutado_5"), 
							null, 0.0, 0.0, results.getDouble("ejecutado_actual"), results.getDouble("acumulado_actual"), 
							0.0);
					entidades.add(entidad);
				}
				results.close();
				pstm1.close();
				pstm1 = conn.prepareStatement("select e.ejercicio, e.entidad, sum( case when mes=? then v.apr else 0 end)  apr, "+
					"	sum(v.sol) sol, "+
					"	sum(case when mes=? then v.reprogramacion else 0 end) reprogramacion, "+
					"	sum( case when mes<=? then v.apr else 0 end)  a_apr, "+
					"	sum(case when mes<=? then v.reprogramacion else 0 end) a_reprogramacion "+
					"from cg_entidades e left outer join vw_aprobado_repo v on (e.entidad = v.entidad and e.ejercicio = v.ejercicio and v.unidad_ejecutora = 0 ) "+
					"where v.fuente IN ("+fuentes+") "+
					" and v.grupo_gasto IN ("+gruposGasto+") "+
					"and ((e.entidad BETWEEN 11130000 AND 11130020) OR (e.entidad = 11140021)) "+ 
					"and v.mes<=? "+
					"and e.ejercicio = ? "+
					"group by e.ejercicio, e.entidad "+
					"order by e.entidad");
				pstm1.setInt(1, mes);
				pstm1.setInt(2, mes);
				pstm1.setInt(3, mes);
				pstm1.setInt(4, mes);
				pstm1.setInt(5, mes);
				pstm1.setInt(6, now.getYear());
				results = pstm1.executeQuery();
				int pos = 0;
				while(results.next()){
					entidades.get(pos).setAprobado(results.getDouble("apr"));
					entidades.get(pos).setAprobado_acumulado(results.getDouble("a_apr"));
					pos++;
				}
				results.close();
				pstm1.close();
				if(todosgrupos){
					int cuatrimestre = ((mes-1)/4)+1;
					pstm1 = conn.prepareStatement("select e.ejercicio, e.entidad, "+
						"	ifnull(sum(case when cuatrimestre < ? then mes1_anticipo else 0 end),0)  + "+
						"	ifnull(sum(case when cuatrimestre < ? then mes2_anticipo else 0 end),0)  + "+
						"	ifnull(sum(case when cuatrimestre < ? then mes3_anticipo else 0 end),0)  + "+
						"	ifnull(sum(case when cuatrimestre < ? then mes4_anticipo else 0 end),0)  a_anticipo, "+
						"	ifnull(sum(case when mes_aprobacion<=? and cuatrimestre = ? then mes1_anticipo else 0 end),0)  + "+ 
						"	ifnull(sum(case when mes_aprobacion<=? and cuatrimestre = ? and (mod(?-1,4)+1)>1 then mes2_anticipo else 0 end),0) + "+ 
						"	ifnull(sum(case when mes_aprobacion<=? and cuatrimestre = ? and (mod(?-1,4)+1)>2 then mes3_anticipo else 0 end),0)  + "+
						"	ifnull(sum(case when mes_aprobacion<=? and cuatrimestre = ? and (mod(?-1,4)+1)>3 then mes4_anticipo else 0 end),0)  anticipo "+
						" from cg_entidades e left outer join anticipo a on (e.entidad=a.entidad and e.ejercicio=a.ejercicio and a.fuente IN ("+fuentes+") ) " +
						" where e.ejercicio = ? "+
						" and ((e.entidad BETWEEN 11130000 AND 11130020) OR (e.entidad = 11140021)) "+
						" group by e.ejercicio, e.entidad "+
						" order by e.ejercicio, e.entidad ");
					pstm1.setInt(1, cuatrimestre);
					pstm1.setInt(2, cuatrimestre);
					pstm1.setInt(3, cuatrimestre);
					pstm1.setInt(4, cuatrimestre);
					pstm1.setInt(5, mes);
					pstm1.setInt(6, cuatrimestre);
					pstm1.setInt(7, mes);
					pstm1.setInt(8, cuatrimestre);
					pstm1.setInt(9, mes);
					pstm1.setInt(10, mes);
					pstm1.setInt(11, cuatrimestre);
					pstm1.setInt(12, mes);
					pstm1.setInt(13, mes);
					pstm1.setInt(14, cuatrimestre);
					pstm1.setInt(15, mes);
					pstm1.setInt(16, now.getYear());
					results = pstm1.executeQuery();
					pos=0;
					while(results.next()){
						entidades.get(pos).setAprobado(entidades.get(pos).getAprobado()+results.getDouble("anticipo"));
						entidades.get(pos).setAprobado_acumulado(entidades.get(pos).getAprobado_acumulado()+(cuatrimestre>1 ? results.getDouble("a_anticipo") : results.getDouble("anticipo")));
						pos++;
					}
					results.close();
					pstm1.close();
				}
				pstm1 = conn.prepareStatement("select e.entidad, sum(v.asignado) asignado, "+
					"sum(v.vigente_1) vigente_1, "+
					"sum(v.vigente_2) vigente_2, "+
					"sum(v.vigente_3) vigente_3, "+
					"sum(v.vigente_4) vigente_4, "+
					"sum(v.vigente_5) vigente_5, "+
					"sum(v.vigente_6) vigente_6, "+
					"sum(v.vigente_7) vigente_7, "+
					"sum(v.vigente_8) vigente_8, "+
					"sum(v.vigente_9) vigente_9, "+
					"sum(v.vigente_10) vigente_10, "+
					"sum(v.vigente_11) vigente_11, "+
					"sum(v.vigente_12) vigente_12, "+
					"sum(v.vigente_actual) vigente_actual "+
					"from cg_entidades e left outer join vw_vigente_renglon v "
					+ "on (e.entidad=v.entidad and e.ejercicio=v.ejercicio and v.fuente IN ("+fuentes+")  and (v.renglon - mod( v.renglon,10)) - (mod(((v.renglon - mod( v.renglon,10))) ,100)) in ("+gruposGasto+") ) "+
					"where e.ejercicio=? "+
					"and ((e.entidad BETWEEN 11130000 AND 11130020) OR (e.entidad = 11140021)) "+ 
					"group by e.entidad "+
					"order by e.entidad");
				pstm1.setInt(1, now.getYear());
				results = pstm1.executeQuery();
				pos = 0;
				while(results.next()){
					entidades.get(pos).setVigente(results.getDouble("vigente_"+mes));
					pos++;
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("1", CEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return entidades.size()>0 ? entidades : null;
	}
	
	public static ArrayList<CEjecucion> getUnidadesEjecutorasEjecucion(int entidad, int mes, String fuentes, String clase_registro, String gruposGasto, boolean todosgrupos){
		final ArrayList<CEjecucion> entidades=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				DateTime now = new DateTime();
				PreparedStatement pstm1 =  conn.prepareStatement("select ue.entidad, ue.unidad_ejecutora, ue.nombre, "+
					"SUM(CASE WHEN (g.ejercicio=?-5 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_1, "+
					"SUM(CASE WHEN (g.ejercicio=?-4 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_2, "+
					"SUM(CASE WHEN (g.ejercicio=?-3 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_3, "+
					"SUM(CASE WHEN (g.ejercicio=?-2 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_4, "+
					"SUM(CASE WHEN (g.ejercicio=?-1 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_5, "+
					"SUM(CASE WHEN (g.ejercicio=? AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_actual, "+
					"SUM(CASE WHEN (g.ejercicio=? AND g.mes<=?) THEN g.total ELSE 0 END) acumulado_actual "+
					"from unidades_ejecutoras ue left outer join vw_gasto_renglon g "+
					"on (g.entidad = ue.entidad and g.ue = ue.unidad_ejecutora and g.fuente IN ("+fuentes+") and g.clase_registro IN ("+clase_registro+")"
							+ "and ((g.renglon - mod( g.renglon,10)) - (mod(((g.renglon - mod( g.renglon,10))) ,100)) in ("+gruposGasto+") )) "+
					"where ue.ejercicio = ? "+
					"and ue.entidad = ? "+
					"group by ue.entidad, ue.unidad_ejecutora, ue.nombre "+
					"order by ue.entidad, ue.unidad_ejecutora, ue.nombre ");				
				pstm1.setInt(1, now.getYear());
				pstm1.setInt(2, mes);
				pstm1.setInt(3, now.getYear());
				pstm1.setInt(4, mes);
				pstm1.setInt(5, now.getYear());
				pstm1.setInt(6, mes);
				pstm1.setInt(7, now.getYear());
				pstm1.setInt(8, mes);
				pstm1.setInt(9, now.getYear());
				pstm1.setInt(10, mes);
				pstm1.setInt(11, now.getYear());
				pstm1.setInt(12, mes);
				pstm1.setInt(13, now.getYear());
				pstm1.setInt(14, mes);
				pstm1.setInt(15, now.getYear());
				pstm1.setInt(16, entidad);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CEjecucion ue = new CEjecucion(results.getInt("entidad"),results.getInt("unidad_ejecutora"), results.getString("nombre"), results.getDouble("ejecutado_1"), 
							results.getDouble("ejecutado_2"), results.getDouble("ejecutado_3"), results.getDouble("ejecutado_4"), results.getDouble("ejecutado_5"), 
							null, 0.0, 0.0, results.getDouble("ejecutado_actual"), results.getDouble("acumulado_actual"), 
							0.0);
					entidades.add(ue);
				}
				results.close();
				pstm1.close();
				int cuatrimestre = ((mes-1)/4)+1;
				pstm1 = conn.prepareStatement("select ue.unidad_ejecutora, sum( case when mes=? then v.apr else 0 end) apr, " +
					"	sum(v.sol) sol, "+
					"	sum(case when mes=? then v.reprogramacion else 0 end) reprogramacion, "+
					"	sum( case when mes<=? then v.apr else 0 end)  a_apr, "+
					"	sum(case when mes<=? then v.reprogramacion else 0 end) a_reprogramacion "+
					" from unidades_ejecutoras ue left outer join vw_aprobado_repo v on "
					+ "(ue.entidad = v.entidad and ue.unidad_ejecutora=v.unidad_ejecutora and ue.ejercicio = v.ejercicio "
					+ " and v.fuente IN ("+fuentes+") and v.grupo_gasto IN ("+gruposGasto+") and v.mes<=? ) "+
					" where ue.ejercicio = ? "+
					" and ue.entidad = ? "+ 
					"group by ue.unidad_ejecutora "+
					"order by ue.unidad_ejecutora");
					pstm1.setInt(1, mes);
					pstm1.setInt(2, mes);
					pstm1.setInt(3, mes);
					pstm1.setInt(4, mes);
					pstm1.setInt(5, mes);
					pstm1.setInt(6, now.getYear());
					pstm1.setInt(7, entidad);
					
					results = pstm1.executeQuery();
				int pos = 0;
				while(results.next()){
					entidades.get(pos).setAprobado(results.getDouble("apr")+results.getDouble("reprogramacion"));
					entidades.get(pos).setAprobado_acumulado(results.getDouble("a_apr")+results.getDouble("a_reprogramacion"));
					pos++;
				}
				results.close();
				pstm1.close();
				if(todosgrupos){
					pstm1 = conn.prepareStatement("select ue.ejercicio, ue.entidad, ue.unidad_ejecutora, "+
						"	ifnull(sum(case when cuatrimestre < ? then mes1_anticipo else 0 end),0)  + "+
						"	ifnull(sum(case when cuatrimestre < ? then mes2_anticipo else 0 end),0)  + "+
						"	ifnull(sum(case when cuatrimestre < ? then mes3_anticipo else 0 end),0)  + "+
						"	ifnull(sum(case when cuatrimestre < ? then mes4_anticipo else 0 end),0)  a_anticipo, "+
						"	ifnull(sum(case when mes_aprobacion<=? and cuatrimestre = ? then mes1_anticipo else 0 end),0)  + "+ 
						"	ifnull(sum(case when mes_aprobacion<=? and cuatrimestre = ? and (mod(?-1,4)+1)>1 then mes2_anticipo else 0 end),0) + "+ 
						"	ifnull(sum(case when mes_aprobacion<=? and cuatrimestre = ? and (mod(?-1,4)+1)>2 then mes3_anticipo else 0 end),0)  + "+
						"	ifnull(sum(case when mes_aprobacion<=? and cuatrimestre = ? and (mod(?-1,4)+1)>3 then mes4_anticipo else 0 end),0)  anticipo "+
						" from unidades_ejecutoras ue left outer join anticipo a on (ue.entidad=a.entidad and ue.unidad_ejecutora=a.unidad_ejecutora and ue.ejercicio=a.ejercicio and a.fuente IN ("+fuentes+") ) "+
						" where ue.ejercicio = ? "+
						" and ue.entidad = ? "+ 
						" group by ue.ejercicio, ue.entidad, ue.unidad_ejecutora "+
						" order by ue.ejercicio, ue.entidad, ue.unidad_ejecutora ");
					pstm1.setInt(1, cuatrimestre);
					pstm1.setInt(2, cuatrimestre);
					pstm1.setInt(3, cuatrimestre);
					pstm1.setInt(4, cuatrimestre);
					pstm1.setInt(5, mes);
					pstm1.setInt(6, cuatrimestre);
					pstm1.setInt(7, mes);
					pstm1.setInt(8, cuatrimestre);
					pstm1.setInt(9, mes);
					pstm1.setInt(10, mes);
					pstm1.setInt(11, cuatrimestre);
					pstm1.setInt(12, mes);
					pstm1.setInt(13, mes);
					pstm1.setInt(14, cuatrimestre);
					pstm1.setInt(15, mes);
					pstm1.setInt(16, now.getYear());
					pstm1.setInt(17, entidad);
					results = pstm1.executeQuery();
					pos=0;
					while(results.next()){
						entidades.get(pos).setAprobado(entidades.get(pos).getAprobado()+results.getDouble("anticipo"));
						entidades.get(pos).setAprobado_acumulado(entidades.get(pos).getAprobado_acumulado()+results.getDouble("a_anticipo"));
						pos++;
					}
					results.close();
					pstm1.close();
				}
				pstm1 = conn.prepareStatement("select ue.entidad, ue.unidad_ejecutora, sum(v.asignado), "+
						"sum(v.vigente_1) vigente_1, "+
						"sum(v.vigente_2) vigente_2, "+
						"sum(v.vigente_3) vigente_3, "+
						"sum(v.vigente_4) vigente_4, "+
						"sum(v.vigente_5) vigente_5, "+
						"sum(v.vigente_6) vigente_6, "+
						"sum(v.vigente_7) vigente_7, "+
						"sum(v.vigente_8) vigente_8, "+
						"sum(v.vigente_9) vigente_9, "+
						"sum(v.vigente_10) vigente_10, "+
						"sum(v.vigente_11) vigente_11, "+
						"sum(v.vigente_12) vigente_12, "+
						"sum(v.vigente_actual) vigente_actual "+
					"from unidades_ejecutoras ue left outer join vw_vigente_renglon v "
					+ "on (ue.entidad=v.entidad and ue.unidad_ejecutora=v.ue and ue.ejercicio=v.ejercicio and v.fuente IN ("+fuentes+") and ((v.renglon - mod( v.renglon,10)) - (mod(((v.renglon - mod( v.renglon,10))) ,100)) in ("+gruposGasto+")) ) "+
					"where ue.ejercicio=? "+
					"and ue.entidad = ? "+ 
					"group by ue.entidad, ue.unidad_ejecutora "+
					"order by ue.entidad, ue.unidad_ejecutora ");
				pstm1.setInt(1, now.getYear());
				pstm1.setInt(2, entidad);
				results = pstm1.executeQuery();
				pos = 0;
				while(results.next()){
					entidades.get(pos).setVigente(results.getDouble("vigente_"+mes));
					pos++;
				}
				results.close();
				pstm1.close();
				if(entidades.size()>1 && entidades.get(0).getEntidad()==0)
					entidades.remove(0);
			}
			catch(Exception e){
				CLogger.write("2", CEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return entidades.size()>0 ? entidades : null;
	}
	
	public static ArrayList<CEjecucion> getRenglonesEjecucion(int entidad,int unidad_ejecutora, int mes, int ejercicio, String fuentes, String clase_registro, String gruposGasto){
		final ArrayList<CEjecucion> lista=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				DateTime now = new DateTime();
				PreparedStatement pstm1 =  conn.prepareStatement("SELECT gasto.entidad, SUM(CASE WHEN (gasto.ejercicio=?-5 AND gasto.mes=?) THEN gasto.total ELSE 0 END) ejecutado_1, "+
					"SUM(CASE WHEN (gasto.ejercicio=?-4 AND gasto.mes=?) THEN gasto.total ELSE 0 END) ejecutado_2, "+
					"SUM(CASE WHEN (gasto.ejercicio=?-3 AND gasto.mes=?) THEN gasto.total ELSE 0 END) ejecutado_3, "+
					"SUM(CASE WHEN (gasto.ejercicio=?-2 AND gasto.mes=?) THEN gasto.total ELSE 0 END) ejecutado_4, "+
					"SUM(CASE WHEN (gasto.ejercicio=?-1 AND gasto.mes=?) THEN gasto.total ELSE 0 END) ejecutado_5, "+
					"SUM(CASE WHEN (gasto.ejercicio=? AND gasto.mes=?) THEN gasto.total ELSE 0 END) ejecutado_actual, "+ 
					"SUM(CASE WHEN (gasto.ejercicio=? AND gasto.mes<=?) THEN gasto.total ELSE 0 END) acumulado_actual,  "+
					"r.renglon, r.nombre renglon_nombre, r.grupo, r.subgrupo "+
				  "FROM renglones r left outer join vw_gasto_renglon gasto "+
				  	"on (r.renglon = gasto.renglon and gasto.clase_registro IN (1,2,3,4) and gasto.fuente IN ("+fuentes+") and gasto.entidad = ? and gasto.ue = ?) "+
				  	"WHERE r.ejercicio = ? and r.grupo IN ("+gruposGasto+")"+
					  "group by gasto.entidad, r.renglon, r.nombre, r.grupo, r.subgrupo "+
					  "order by r.renglon");	
				pstm1.setInt(1, ejercicio);
				pstm1.setInt(2, mes);
				pstm1.setInt(3, ejercicio);
				pstm1.setInt(4, mes);
				pstm1.setInt(5, ejercicio);
				pstm1.setInt(6, mes);
				pstm1.setInt(7, ejercicio);
				pstm1.setInt(8, mes);
				pstm1.setInt(9, ejercicio);
				pstm1.setInt(10, mes);
				pstm1.setInt(11, ejercicio);
				pstm1.setInt(12, mes);
				pstm1.setInt(13, ejercicio);
				pstm1.setInt(14, mes);
				pstm1.setInt(15, entidad);
				pstm1.setInt(16, unidad_ejecutora);
				pstm1.setInt(17, ejercicio);
				ResultSet results = pstm1.executeQuery();
				int grupo_actual=-1;
				int subgrupo_actual =-1;
				ArrayList<CEjecucion> subgrupos=new ArrayList<CEjecucion>();
				ArrayList<CEjecucion> renglones=new ArrayList<CEjecucion>();
				
				Double g_ano1=0.0, g_ano2=0.0,g_ano3=0.0,g_ano4=0.0,g_ano5=0.0, g_ejecutado=0.0, g_acumulado=0.0;
				Double sg_ano1=0.0, sg_ano2=0.0, sg_ano3=0.0, sg_ano4=0.0, sg_ano5=0.0, sg_ejecutado=0.0, sg_acumulado=0.0;
				CEjecucion egrupo=null, esubgrupo=null;
				boolean cambio_grupo=false;
				while (results.next()){
					if(grupo_actual!=results.getInt("grupo")){
						if(grupo_actual>-1){
							lista.add(egrupo);
							g_ano1+=sg_ano1; g_ano2+=sg_ano2; g_ano3+=sg_ano3; g_ano4+=sg_ano4; g_ano5+=sg_ano5;
							g_ejecutado += sg_ejecutado; g_acumulado += sg_acumulado;
							sg_ano1=0.0; sg_ano2=0.0; sg_ano3=0.0; sg_ano4=0.0; sg_ano5=0.0; sg_ejecutado=0.0; sg_acumulado=0.0;
							esubgrupo.setAno1(sg_ano1); esubgrupo.setAno2(sg_ano2); esubgrupo.setAno3(sg_ano3); esubgrupo.setAno4(sg_ano4); esubgrupo.setAno5(sg_ano5); 
							esubgrupo.setEjecutado(sg_ejecutado); esubgrupo.setEjecutado_acumulado(sg_acumulado);
							subgrupos.add(esubgrupo);
							subgrupos.addAll(renglones);
							lista.addAll(subgrupos);
							egrupo.setAno1(g_ano1); egrupo.setAno2(g_ano2); egrupo.setAno3(g_ano3); egrupo.setAno4(g_ano4); egrupo.setAno5(g_ano5); 
							egrupo.setEjecutado(g_ejecutado); egrupo.setEjecutado_acumulado(g_acumulado);
							subgrupos.clear();
							renglones.clear();
							cambio_grupo = true;
						}
						grupo_actual = results.getInt("grupo");
						egrupo = new CEjecucion(0,grupo_actual, results.getString("renglon_nombre"), 0.0, 
								0.0, 0.0, 0.0, 0.0,null, 0.0, 0.0, 0.0, 0.0,0.0);
						g_ano1=0.0; g_ano2=0.0; g_ano3=0.0; g_ano4=0.0; g_ano5=0.0; g_ejecutado=0.0; g_acumulado=0.0;
					}
					if(subgrupo_actual!=results.getInt("subgrupo")){
						if(subgrupo_actual>0 && !cambio_grupo){
							esubgrupo.setAno1(sg_ano1); esubgrupo.setAno2(sg_ano2); esubgrupo.setAno3(sg_ano3); esubgrupo.setAno4(sg_ano4); esubgrupo.setAno5(sg_ano5); 
							esubgrupo.setEjecutado(sg_ejecutado); esubgrupo.setEjecutado_acumulado(sg_acumulado);
							subgrupos.add(esubgrupo);
							subgrupos.addAll(renglones);
							renglones.clear();
						}
						else
							cambio_grupo=false;
						subgrupo_actual = results.getInt("subgrupo");
						esubgrupo = new CEjecucion(1,subgrupo_actual, results.getString("renglon_nombre"), 0.0, 
								0.0, 0.0, 0.0, 0.0,null, 0.0, 0.0, 0.0, 0.0,0.0);
						g_ano1+=sg_ano1; g_ano2+=sg_ano2; g_ano3+=sg_ano3; g_ano4+=sg_ano4; g_ano5+=sg_ano5;
						g_ejecutado += sg_ejecutado; g_acumulado += sg_acumulado;
						sg_ano1=0.0; sg_ano2=0.0; sg_ano3=0.0; sg_ano4=0.0; sg_ano5=0.0; sg_ejecutado=0.0; sg_acumulado=0.0;
						
					}
					CEjecucion renglon = new CEjecucion(null,results.getInt("renglon"), results.getString("renglon_nombre"), results.getDouble("ejecutado_1"), 
							results.getDouble("ejecutado_2"), results.getDouble("ejecutado_3"), results.getDouble("ejecutado_4"), results.getDouble("ejecutado_5"), 
							null, 0.0, 0.0, results.getDouble("ejecutado_actual"), results.getDouble("acumulado_actual"), 
							0.0);
					sg_ano1 += results.getDouble("ejecutado_1");
					sg_ano2 += results.getDouble("ejecutado_2");
					sg_ano3 += results.getDouble("ejecutado_3");
					sg_ano4 += results.getDouble("ejecutado_4");
					sg_ano5 += results.getDouble("ejecutado_5");
					sg_ejecutado += results.getDouble("ejecutado_actual");
					sg_acumulado += results.getDouble("acumulado_actual");
					renglones.add(renglon);
				}
				lista.add(egrupo);
				g_ano1+=sg_ano1; g_ano2+=sg_ano2; g_ano3+=sg_ano3; g_ano4+=sg_ano4; g_ano5+=sg_ano5;
				g_ejecutado += sg_ejecutado; g_acumulado += sg_acumulado;
				sg_ano1=0.0; sg_ano2=0.0; sg_ano3=0.0; sg_ano4=0.0; sg_ano5=0.0; sg_ejecutado=0.0; sg_acumulado=0.0;
				esubgrupo.setAno1(sg_ano1); esubgrupo.setAno2(sg_ano2); esubgrupo.setAno3(sg_ano3); esubgrupo.setAno4(sg_ano4); esubgrupo.setAno5(sg_ano5); 
				esubgrupo.setEjecutado(sg_ejecutado); esubgrupo.setEjecutado_acumulado(sg_acumulado);
				subgrupos.add(esubgrupo);
				subgrupos.addAll(renglones);
				lista.addAll(subgrupos);
				egrupo.setAno1(g_ano1); egrupo.setAno2(g_ano2); egrupo.setAno3(g_ano3); egrupo.setAno4(g_ano4); egrupo.setAno5(g_ano5); 
				egrupo.setEjecutado(g_ejecutado); egrupo.setEjecutado_acumulado(g_acumulado);
				results.close();
				pstm1.close();
				pstm1 = conn.prepareStatement("select r.renglon, SUM(v.asignado), "+
						"sum(v.vigente_1) vigente_1, "+
						"sum(v.vigente_2) vigente_2, "+
						"sum(v.vigente_3) vigente_3, "+
						"sum(v.vigente_4) vigente_4, "+
						"sum(v.vigente_5) vigente_5, "+
						"sum(v.vigente_6) vigente_6, "+
						"sum(v.vigente_7) vigente_7, "+
						"sum(v.vigente_8) vigente_8, "+
						"sum(v.vigente_9) vigente_9, "+
						"sum(v.vigente_10) vigente_10, "+
						"sum(v.vigente_11) vigente_11, "+
						"sum(v.vigente_12) vigente_12, "+
						"sum(v.vigente_actual) vigente_actual "+
					"from renglones r left outer join vw_vigente_renglon v on ("
					+ "	v.entidad=? and v.ue=? and r.renglon=v.renglon and r.ejercicio=v.ejercicio and v.fuente IN ("+fuentes+")) "+
					"where r.ejercicio=? and r.grupo in ("+gruposGasto+") "+
					"and mod(r.renglon,10)>0 "+
					"group by r.renglon "+
					"order by r.entidad, r.unidad_ejecutora, r.renglon ");
				pstm1.setInt(1, entidad);
				pstm1.setInt(2, unidad_ejecutora);
				pstm1.setInt(3, now.getYear());
				results = pstm1.executeQuery();
				int pos = 0;
				while(results.next()){
					for(int i=0; i<lista.size(); i++){
						if(lista.get(i).getEntidad()==results.getInt("renglon")){
							pos=i;
							i=lista.size();
						}
					}
					if(pos>-1)
						lista.get(pos).setVigente(results.getDouble("vigente_"+mes));
					pos=-1;
				}
				grupo_actual = -1;
				subgrupo_actual = -1;
				Double vigente=0.0, vigente_grupo=0.0;
				ArrayList<CEjecucion> todelete=new ArrayList<CEjecucion>();
				for(int i=lista.size()-1; i>-1; i--){
					if(subgrupo_actual!=lista.get(i).getEntidad() && lista.get(i).getParent()!=null && lista.get(i).getParent()==1){
						lista.get(i).setVigente(vigente);
						subgrupo_actual= lista.get(i).getEntidad();
						vigente_grupo += vigente;
						vigente=0.0;
					}
					else if(grupo_actual!=lista.get(i).getEntidad() && lista.get(i).getParent()!=null && lista.get(i).getParent()==0){
						lista.get(i).setVigente(vigente_grupo);
						grupo_actual = lista.get(i).getEntidad();
						vigente=0.0;
						vigente_grupo=0.0;
					}
					else
						vigente+=lista.get(i).getVigente();
					if(!(lista.get(i).getAno1()>0 || lista.get(i).getAno2()>0 || lista.get(i).getAno3()>0 || lista.get(i).getAno4()>0
							|| lista.get(i).getAno5()>0 || lista.get(i).getEjecutado_acumulado()>0 || lista.get(i).getVigente()>0))
						todelete.add(lista.get(i));
				}
				lista.removeAll(todelete);
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("3", CEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return lista.size()>0 ? lista : null;
	}

	public static ArrayList<CEjecucion> getEntidadesEjecucionAcumulada(int mes, String fuentes, String clase_registro, String gruposGasto, boolean todosgrupos){
		final ArrayList<CEjecucion> entidades=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				DateTime now = new DateTime();
				PreparedStatement pstm1 =  conn.prepareStatement("select e.entidad, e.nombre, "+
					"SUM(CASE WHEN (g.ejercicio=?-5 AND g.mes<=?) THEN g.total ELSE 0 END) ejecutado_1, "+
					"SUM(CASE WHEN (g.ejercicio=?-4 AND g.mes<=?) THEN g.total ELSE 0 END) ejecutado_2, "+
					"SUM(CASE WHEN (g.ejercicio=?-3 AND g.mes<=?) THEN g.total ELSE 0 END) ejecutado_3, "+
					"SUM(CASE WHEN (g.ejercicio=?-2 AND g.mes<=?) THEN g.total ELSE 0 END) ejecutado_4, "+
					"SUM(CASE WHEN (g.ejercicio=?-1 AND g.mes<=?) THEN g.total ELSE 0 END) ejecutado_5, "+
					"SUM(CASE WHEN (g.ejercicio=? AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_actual, "+
					"SUM(CASE WHEN (g.ejercicio=? AND g.mes<=?) THEN g.total ELSE 0 END) acumulado_actual "+
					"from cg_entidades e LEFT OUTER JOIN vw_gasto_renglon g "
						+ "ON (e.entidad = g.entidad and g.fuente IN ("+fuentes+")  and g.clase_registro IN ("+clase_registro+") "
						+ " and ((g.renglon - mod( g.renglon,10)) - (mod(((g.renglon - mod( g.renglon,10))) ,100)) in ("+gruposGasto+")) )  "+
					"where e.ejercicio = ? "+
					"and ((e.entidad BETWEEN 11130000 AND 11130020) OR (e.entidad = 11140021))  "+
					"group by e.entidad, e.nombre "+
					"order by e.entidad, e.nombre");				
				pstm1.setInt(1, now.getYear());
				pstm1.setInt(2, mes);
				pstm1.setInt(3, now.getYear());
				pstm1.setInt(4, mes);
				pstm1.setInt(5, now.getYear());
				pstm1.setInt(6, mes);
				pstm1.setInt(7, now.getYear());
				pstm1.setInt(8, mes);
				pstm1.setInt(9, now.getYear());
				pstm1.setInt(10, mes);
				pstm1.setInt(11, now.getYear());
				pstm1.setInt(12, mes);
				pstm1.setInt(13, now.getYear());
				pstm1.setInt(14, mes);
				pstm1.setInt(15, now.getYear());
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CEjecucion entidad = new CEjecucion(null, results.getInt("entidad"), results.getString("nombre"), results.getDouble("ejecutado_1"), 
							results.getDouble("ejecutado_2"), results.getDouble("ejecutado_3"), results.getDouble("ejecutado_4"), results.getDouble("ejecutado_5"), 0.0,
							0.0, 0.0,0.0,0.0, results.getDouble("acumulado_actual"), 0.0);
					entidades.add(entidad);
				}
				results.close();
				pstm1.close();
				pstm1 = conn.prepareStatement("select e.ejercicio, e.entidad, sum( case when mes=? then v.apr else 0 end)  apr, "+
					"	sum(case when mes<=? then v.sol else 0 end) a_sol, "+
					"	sum(case when mes=? then v.reprogramacion else 0 end) reprogramacion, "+
					"	sum( case when mes<=? then v.apr else 0 end)  a_apr, "+
					"	sum(case when mes<=? then v.reprogramacion else 0 end) a_reprogramacion "+
					"from cg_entidades e left outer join vw_aprobado_repo v on (e.entidad = v.entidad and e.ejercicio = v.ejercicio and v.unidad_ejecutora = 0 ) "+
					"where v.fuente IN ("+fuentes+") "+
					" and v.grupo_gasto IN ("+gruposGasto+") "+
					"and ((e.entidad BETWEEN 11130000 AND 11130020) OR (e.entidad = 11140021)) "+ 
					"and v.mes<=? "+
					"and e.ejercicio = ? "+
					"group by e.ejercicio, e.entidad "+
					"order by e.entidad");
				pstm1.setInt(1, mes);
				pstm1.setInt(2, mes);
				pstm1.setInt(3, mes);
				pstm1.setInt(4, mes);
				pstm1.setInt(5, mes);
				pstm1.setInt(6, mes);
				pstm1.setInt(7, now.getYear());
				results = pstm1.executeQuery();
				int pos = 0;
				while(results.next()){
					entidades.get(pos).setSolicitado_acumulado(results.getDouble("a_sol"));
					entidades.get(pos).setAprobado_sin_anticipo(results.getDouble("a_apr"));//por que sin reprogramaciones
					pos++;
				}
				results.close();
				pstm1.close();
				if(todosgrupos){
					int cuatrimestre = ((mes-1)/4)+1;
					pstm1 = conn.prepareStatement("select e.ejercicio, e.entidad, "+
						"	ifnull(sum(case when cuatrimestre < ? then mes1_anticipo else 0 end),0)  + "+
						"	ifnull(sum(case when cuatrimestre < ? then mes2_anticipo else 0 end),0)  + "+
						"	ifnull(sum(case when cuatrimestre < ? then mes3_anticipo else 0 end),0)  + "+
						"	ifnull(sum(case when cuatrimestre < ? then mes4_anticipo else 0 end),0)  a_anticipo, "+
						"	ifnull(sum(case when mes_aprobacion<=? and cuatrimestre = ? then mes1_anticipo else 0 end),0)  + "+ 
						"	ifnull(sum(case when mes_aprobacion<=? and cuatrimestre = ? and (mod(?-1,4)+1)>1 then mes2_anticipo else 0 end),0) + "+ 
						"	ifnull(sum(case when mes_aprobacion<=? and cuatrimestre = ? and (mod(?-1,4)+1)>2 then mes3_anticipo else 0 end),0)  + "+
						"	ifnull(sum(case when mes_aprobacion<=? and cuatrimestre = ? and (mod(?-1,4)+1)>3 then mes4_anticipo else 0 end),0)  anticipo "+
						" from cg_entidades e left outer join anticipo a on (e.entidad=a.entidad and e.ejercicio=a.ejercicio and a.fuente IN ("+fuentes+") ) " +
						" where e.ejercicio = ? "+
						" and ((e.entidad BETWEEN 11130000 AND 11130020) OR (e.entidad = 11140021)) "+
						" group by e.ejercicio, e.entidad "+
						" order by e.ejercicio, e.entidad ");
					pstm1.setInt(1, cuatrimestre);
					pstm1.setInt(2, cuatrimestre);
					pstm1.setInt(3, cuatrimestre);
					pstm1.setInt(4, cuatrimestre);
					pstm1.setInt(5, mes);
					pstm1.setInt(6, cuatrimestre);
					pstm1.setInt(7, mes);
					pstm1.setInt(8, cuatrimestre);
					pstm1.setInt(9, mes);
					pstm1.setInt(10, mes);
					pstm1.setInt(11, cuatrimestre);
					pstm1.setInt(12, mes);
					pstm1.setInt(13, mes);
					pstm1.setInt(14, cuatrimestre);
					pstm1.setInt(15, mes);
					pstm1.setInt(16, now.getYear());
					results = pstm1.executeQuery();
					pos=0;
					while(results.next()){
						if (cuatrimestre>1){
							entidades.get(pos).setAnticipo(results.getDouble("a_anticipo"));
							entidades.get(pos).setAprobado_acumulado(entidades.get(pos).getAprobado_sin_anticipo()+results.getDouble("a_anticipo"));
						}else{
							entidades.get(pos).setAnticipo(results.getDouble("anticipo"));
							entidades.get(pos).setAprobado_acumulado(entidades.get(pos).getAprobado_sin_anticipo()+results.getDouble("anticipo"));
						}							
						pos++;
					}
					results.close();
					pstm1.close();
				}
				pstm1 = conn.prepareStatement("select e.entidad, sum(v.asignado) asignado, "+
					"sum(v.vigente_1) vigente_1, "+
					"sum(v.vigente_2) vigente_2, "+
					"sum(v.vigente_3) vigente_3, "+
					"sum(v.vigente_4) vigente_4, "+
					"sum(v.vigente_5) vigente_5, "+
					"sum(v.vigente_6) vigente_6, "+
					"sum(v.vigente_7) vigente_7, "+
					"sum(v.vigente_8) vigente_8, "+
					"sum(v.vigente_9) vigente_9, "+
					"sum(v.vigente_10) vigente_10, "+
					"sum(v.vigente_11) vigente_11, "+
					"sum(v.vigente_12) vigente_12, "+
					"sum(v.vigente_actual) vigente_actual "+
					"from cg_entidades e left outer join vw_vigente_renglon v "
					+ "on (e.entidad=v.entidad and e.ejercicio=v.ejercicio and v.fuente IN ("+fuentes+")  and (v.renglon - mod( v.renglon,10)) - (mod(((v.renglon - mod( v.renglon,10))) ,100)) in ("+gruposGasto+") ) "+
					"where e.ejercicio=? "+
					"and ((e.entidad BETWEEN 11130000 AND 11130020) OR (e.entidad = 11140021)) "+ 
					"group by e.entidad "+
					"order by e.entidad");
				pstm1.setInt(1, now.getYear());
				results = pstm1.executeQuery();
				pos = 0;
				while(results.next()){
					entidades.get(pos).setVigente(results.getDouble("vigente_"+mes));
					pos++;
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("4", CEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return entidades.size()>0 ? entidades : null;
	}
	
	public static ArrayList<CEjecucion> getUnidadesEjecutorasEjecucionAcumulada(int entidad, int mes, String fuentes, String clase_registro, String gruposGasto, boolean todosgrupos){
		final ArrayList<CEjecucion> entidades=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				DateTime now = new DateTime();
				PreparedStatement pstm1 =  conn.prepareStatement("select ue.entidad, ue.unidad_ejecutora, ue.nombre, "+
					"SUM(CASE WHEN (g.ejercicio=?-5 AND g.mes<=?) THEN g.total ELSE 0 END) ejecutado_1, "+
					"SUM(CASE WHEN (g.ejercicio=?-4 AND g.mes<=?) THEN g.total ELSE 0 END) ejecutado_2, "+
					"SUM(CASE WHEN (g.ejercicio=?-3 AND g.mes<=?) THEN g.total ELSE 0 END) ejecutado_3, "+
					"SUM(CASE WHEN (g.ejercicio=?-2 AND g.mes<=?) THEN g.total ELSE 0 END) ejecutado_4, "+
					"SUM(CASE WHEN (g.ejercicio=?-1 AND g.mes<=?) THEN g.total ELSE 0 END) ejecutado_5, "+
					"SUM(CASE WHEN (g.ejercicio=? AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_actual, "+
					"SUM(CASE WHEN (g.ejercicio=? AND g.mes<=?) THEN g.total ELSE 0 END) acumulado_actual "+
					"from unidades_ejecutoras ue left outer join vw_gasto_renglon g "+
					"on (g.entidad = ue.entidad and g.ue = ue.unidad_ejecutora and g.fuente IN ("+fuentes+") and g.clase_registro IN ("+clase_registro+")"
							+ "and ((g.renglon - mod( g.renglon,10)) - (mod(((g.renglon - mod( g.renglon,10))) ,100)) in ("+gruposGasto+") )) "+
					"where ue.ejercicio = ? "+
					"and ue.entidad = ? "+
					"group by ue.entidad, ue.unidad_ejecutora, ue.nombre "+
					"order by ue.entidad, ue.unidad_ejecutora, ue.nombre ");				
				pstm1.setInt(1, now.getYear());
				pstm1.setInt(2, mes);
				pstm1.setInt(3, now.getYear());
				pstm1.setInt(4, mes);
				pstm1.setInt(5, now.getYear());
				pstm1.setInt(6, mes);
				pstm1.setInt(7, now.getYear());
				pstm1.setInt(8, mes);
				pstm1.setInt(9, now.getYear());
				pstm1.setInt(10, mes);
				pstm1.setInt(11, now.getYear());
				pstm1.setInt(12, mes);
				pstm1.setInt(13, now.getYear());
				pstm1.setInt(14, mes);
				pstm1.setInt(15, now.getYear());
				pstm1.setInt(16, entidad);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CEjecucion ue = new CEjecucion(results.getInt("entidad"),results.getInt("unidad_ejecutora"), results.getString("nombre"), results.getDouble("ejecutado_1"), 
							results.getDouble("ejecutado_2"), results.getDouble("ejecutado_3"), results.getDouble("ejecutado_4"), results.getDouble("ejecutado_5"), null,
							0.0,0.0,0.0, 0.0, results.getDouble("acumulado_actual"),0.0);
					entidades.add(ue);
				}
				results.close();
				pstm1.close();
				int cuatrimestre = ((mes-1)/4)+1;
				pstm1 = conn.prepareStatement("select ue.unidad_ejecutora, sum( case when mes=? then v.apr else 0 end) apr, " +
					"	sum(case when mes<=? then v.sol else 0 end) a_sol, "+
					"	sum(case when mes=? then v.reprogramacion else 0 end) reprogramacion, "+
					"	sum( case when mes<=? then v.apr else 0 end)  a_apr, "+
					"	sum(case when mes<=? then v.reprogramacion else 0 end) a_reprogramacion "+
					" from unidades_ejecutoras ue left outer join vw_aprobado_repo v on "
					+ "(ue.entidad = v.entidad and ue.unidad_ejecutora=v.unidad_ejecutora and ue.ejercicio = v.ejercicio "
					+ " and v.fuente IN ("+fuentes+") and v.grupo_gasto IN ("+gruposGasto+") and v.mes<=? ) "+
					" where ue.ejercicio = ? "+
					" and ue.entidad = ? "+ 
					"group by ue.unidad_ejecutora "+
					"order by ue.unidad_ejecutora");
					pstm1.setInt(1, mes);
					pstm1.setInt(2, mes);
					pstm1.setInt(3, mes);
					pstm1.setInt(4, mes);
					pstm1.setInt(5, mes);
					pstm1.setInt(6, mes);
					pstm1.setInt(7, now.getYear());
					pstm1.setInt(8, entidad);
					
					results = pstm1.executeQuery();
				int pos = 0;
				while(results.next()){
					entidades.get(pos).setSolicitado_acumulado(results.getDouble("a_sol"));
					entidades.get(pos).setAprobado_sin_anticipo(results.getDouble("a_apr")+results.getDouble("a_reprogramacion")); //por que con reprogramaciones
					pos++;
				}
				results.close();
				pstm1.close();
				if(todosgrupos){
					pstm1 = conn.prepareStatement("select ue.ejercicio, ue.entidad, ue.unidad_ejecutora, "+
						"	ifnull(sum(case when cuatrimestre < ? then mes1_anticipo else 0 end),0)  + "+
						"	ifnull(sum(case when cuatrimestre < ? then mes2_anticipo else 0 end),0)  + "+
						"	ifnull(sum(case when cuatrimestre < ? then mes3_anticipo else 0 end),0)  + "+
						"	ifnull(sum(case when cuatrimestre < ? then mes4_anticipo else 0 end),0)  a_anticipo, "+
						"	ifnull(sum(case when mes_aprobacion<=? and cuatrimestre = ? then mes1_anticipo else 0 end),0)  + "+ 
						"	ifnull(sum(case when mes_aprobacion<=? and cuatrimestre = ? and (mod(?-1,4)+1)>1 then mes2_anticipo else 0 end),0) + "+ 
						"	ifnull(sum(case when mes_aprobacion<=? and cuatrimestre = ? and (mod(?-1,4)+1)>2 then mes3_anticipo else 0 end),0)  + "+
						"	ifnull(sum(case when mes_aprobacion<=? and cuatrimestre = ? and (mod(?-1,4)+1)>3 then mes4_anticipo else 0 end),0)  anticipo "+
						" from unidades_ejecutoras ue left outer join anticipo a on (ue.entidad=a.entidad and ue.unidad_ejecutora=a.unidad_ejecutora and ue.ejercicio=a.ejercicio and a.fuente IN ("+fuentes+") ) "+
						" where ue.ejercicio = ? "+
						" and ue.entidad = ? "+ 
						" group by ue.ejercicio, ue.entidad, ue.unidad_ejecutora "+
						" order by ue.ejercicio, ue.entidad, ue.unidad_ejecutora ");
					pstm1.setInt(1, cuatrimestre);
					pstm1.setInt(2, cuatrimestre);
					pstm1.setInt(3, cuatrimestre);
					pstm1.setInt(4, cuatrimestre);
					pstm1.setInt(5, mes);
					pstm1.setInt(6, cuatrimestre);
					pstm1.setInt(7, mes);
					pstm1.setInt(8, cuatrimestre);
					pstm1.setInt(9, mes);
					pstm1.setInt(10, mes);
					pstm1.setInt(11, cuatrimestre);
					pstm1.setInt(12, mes);
					pstm1.setInt(13, mes);
					pstm1.setInt(14, cuatrimestre);
					pstm1.setInt(15, mes);
					pstm1.setInt(16, now.getYear());
					pstm1.setInt(17, entidad);
					results = pstm1.executeQuery();
					pos=0;
					while(results.next()){
						if (cuatrimestre>1){
							entidades.get(pos).setAnticipo(results.getDouble("a_anticipo"));
							entidades.get(pos).setAprobado_acumulado(entidades.get(pos).getAprobado_sin_anticipo()+results.getDouble("a_anticipo"));
						}else{
							entidades.get(pos).setAnticipo(results.getDouble("anticipo"));
							entidades.get(pos).setAprobado_acumulado(entidades.get(pos).getAprobado_sin_anticipo()+results.getDouble("anticipo"));
						}	
						pos++;
					}
					results.close();
					pstm1.close();
				}
				pstm1 = conn.prepareStatement("select ue.entidad, ue.unidad_ejecutora, sum(v.asignado) asignado, "+
						"sum(v.vigente_1) vigente_1, "+
						"sum(v.vigente_2) vigente_2, "+
						"sum(v.vigente_3) vigente_3, "+
						"sum(v.vigente_4) vigente_4, "+
						"sum(v.vigente_5) vigente_5, "+
						"sum(v.vigente_6) vigente_6, "+
						"sum(v.vigente_7) vigente_7, "+
						"sum(v.vigente_8) vigente_8, "+
						"sum(v.vigente_9) vigente_9, "+
						"sum(v.vigente_10) vigente_10, "+
						"sum(v.vigente_11) vigente_11, "+
						"sum(v.vigente_12) vigente_12, "+
						"sum(v.vigente_actual) vigente_actual "+
					"from unidades_ejecutoras ue left outer join vw_vigente_renglon v "
					+ "on (ue.entidad=v.entidad and ue.unidad_ejecutora=v.ue and ue.ejercicio=v.ejercicio and v.fuente IN ("+fuentes+") and ((v.renglon - mod( v.renglon,10)) - (mod(((v.renglon - mod( v.renglon,10))) ,100)) in ("+gruposGasto+")) ) "+
					"where ue.ejercicio=? "+
					"and ue.entidad = ? "+ 
					"group by ue.entidad, ue.unidad_ejecutora "+
					"order by ue.entidad, ue.unidad_ejecutora ");
				pstm1.setInt(1, now.getYear());
				pstm1.setInt(2, entidad);
				results = pstm1.executeQuery();
				pos = 0;
				while(results.next()){
					entidades.get(pos).setVigente(results.getDouble("vigente_"+mes));
					pos++;
				}
				results.close();
				pstm1.close();
				if(entidades.size()>1 && entidades.get(0).getEntidad()==0)
					entidades.remove(0);
			}
			catch(Exception e){
				CLogger.write("5", CEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return entidades.size()>0 ? entidades : null;
	}
	
	public static ArrayList<CEjecucion> getRenglonesEjecucionAcumulada(int entidad,int unidad_ejecutora, int mes, int ejercicio, String fuentes, String clase_registro, String gruposGasto){
		final ArrayList<CEjecucion> lista=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				DateTime now = new DateTime();
				PreparedStatement pstm1 =  conn.prepareStatement("SELECT gasto.entidad, SUM(CASE WHEN (gasto.ejercicio=?-5 AND gasto.mes<=?) THEN gasto.total ELSE 0 END) ejecutado_1, "+
					"SUM(CASE WHEN (gasto.ejercicio=?-4 AND gasto.mes<=?) THEN gasto.total ELSE 0 END) ejecutado_2, "+
					"SUM(CASE WHEN (gasto.ejercicio=?-3 AND gasto.mes<=?) THEN gasto.total ELSE 0 END) ejecutado_3, "+
					"SUM(CASE WHEN (gasto.ejercicio=?-2 AND gasto.mes<=?) THEN gasto.total ELSE 0 END) ejecutado_4, "+
					"SUM(CASE WHEN (gasto.ejercicio=?-1 AND gasto.mes<=?) THEN gasto.total ELSE 0 END) ejecutado_5, "+
					"SUM(CASE WHEN (gasto.ejercicio=? AND gasto.mes<=?) THEN gasto.total ELSE 0 END) ejecutado_actual, "+ 
					"SUM(CASE WHEN (gasto.ejercicio=? AND gasto.mes<=?) THEN gasto.total ELSE 0 END) acumulado_actual,  "+
					"r.renglon, r.nombre renglon_nombre, r.grupo, r.subgrupo "+
				  "FROM renglones r left outer join vw_gasto_renglon gasto "+
				  	"on (r.renglon = gasto.renglon and gasto.clase_registro IN (1,2,3,4) and gasto.fuente IN ("+fuentes+") and gasto.entidad = ? and gasto.ue = ?) "+
				  	"WHERE r.ejercicio = ? and r.grupo IN ("+gruposGasto+")"+
					  "group by gasto.entidad, r.renglon, r.nombre, r.grupo, r.subgrupo "+
					  "order by r.renglon");				
				pstm1.setInt(1, ejercicio);
				pstm1.setInt(2, mes);
				pstm1.setInt(3, ejercicio);
				pstm1.setInt(4, mes);
				pstm1.setInt(5, ejercicio);
				pstm1.setInt(6, mes);
				pstm1.setInt(7, ejercicio);
				pstm1.setInt(8, mes);
				pstm1.setInt(9, ejercicio);
				pstm1.setInt(10, mes);
				pstm1.setInt(11, ejercicio);
				pstm1.setInt(12, mes);
				pstm1.setInt(13, ejercicio);
				pstm1.setInt(14, mes);
				pstm1.setInt(15, entidad);
				pstm1.setInt(16, unidad_ejecutora);
				pstm1.setInt(17, ejercicio);
				ResultSet results = pstm1.executeQuery();
				int grupo_actual=-1;
				int subgrupo_actual =-1;
				ArrayList<CEjecucion> subgrupos=new ArrayList<CEjecucion>();
				ArrayList<CEjecucion> renglones=new ArrayList<CEjecucion>();
				
				Double g_ano1=0.0, g_ano2=0.0,g_ano3=0.0,g_ano4=0.0,g_ano5=0.0, g_ejecutado=0.0, g_acumulado=0.0;
				Double sg_ano1=0.0, sg_ano2=0.0, sg_ano3=0.0, sg_ano4=0.0, sg_ano5=0.0, sg_ejecutado=0.0, sg_acumulado=0.0;
				CEjecucion egrupo=null, esubgrupo=null;
				boolean cambio_grupo=false;
				while (results.next()){
					if(grupo_actual!=results.getInt("grupo")){
						if(grupo_actual>-1){
							lista.add(egrupo);
							g_ano1+=sg_ano1; g_ano2+=sg_ano2; g_ano3+=sg_ano3; g_ano4+=sg_ano4; g_ano5+=sg_ano5;
							g_ejecutado += sg_ejecutado; g_acumulado += sg_acumulado;
							sg_ano1=0.0; sg_ano2=0.0; sg_ano3=0.0; sg_ano4=0.0; sg_ano5=0.0; sg_ejecutado=0.0; sg_acumulado=0.0;
							esubgrupo.setAno1(sg_ano1); esubgrupo.setAno2(sg_ano2); esubgrupo.setAno3(sg_ano3); esubgrupo.setAno4(sg_ano4); esubgrupo.setAno5(sg_ano5); 
							esubgrupo.setEjecutado(sg_ejecutado); esubgrupo.setEjecutado_acumulado(sg_acumulado);
							subgrupos.add(esubgrupo);
							subgrupos.addAll(renglones);
							lista.addAll(subgrupos);
							egrupo.setAno1(g_ano1); egrupo.setAno2(g_ano2); egrupo.setAno3(g_ano3); egrupo.setAno4(g_ano4); egrupo.setAno5(g_ano5); 
							egrupo.setEjecutado(g_ejecutado); egrupo.setEjecutado_acumulado(g_acumulado);
							subgrupos.clear();
							renglones.clear();
							cambio_grupo = true;
						}
						grupo_actual = results.getInt("grupo");
						egrupo = new CEjecucion(0,grupo_actual, results.getString("renglon_nombre"), 0.0, 
								0.0, 0.0, 0.0, 0.0,null, 0.0, 0.0, 0.0, 0.0,0.0);
						g_ano1=0.0; g_ano2=0.0; g_ano3=0.0; g_ano4=0.0; g_ano5=0.0; g_ejecutado=0.0; g_acumulado=0.0;
					}
					if(subgrupo_actual!=results.getInt("subgrupo")){
						if(subgrupo_actual>0 && !cambio_grupo){
							esubgrupo.setAno1(sg_ano1); esubgrupo.setAno2(sg_ano2); esubgrupo.setAno3(sg_ano3); esubgrupo.setAno4(sg_ano4); esubgrupo.setAno5(sg_ano5); 
							esubgrupo.setEjecutado(sg_ejecutado); esubgrupo.setEjecutado_acumulado(sg_acumulado);
							subgrupos.add(esubgrupo);
							subgrupos.addAll(renglones);
							renglones.clear();
						}
						else
							cambio_grupo=false;
						subgrupo_actual = results.getInt("subgrupo");
						esubgrupo = new CEjecucion(1,subgrupo_actual, results.getString("renglon_nombre"), 0.0, 
								0.0, 0.0, 0.0, 0.0,null, 0.0, 0.0, 0.0, 0.0,0.0);
						g_ano1+=sg_ano1; g_ano2+=sg_ano2; g_ano3+=sg_ano3; g_ano4+=sg_ano4; g_ano5+=sg_ano5;
						g_ejecutado += sg_ejecutado; g_acumulado += sg_acumulado;
						sg_ano1=0.0; sg_ano2=0.0; sg_ano3=0.0; sg_ano4=0.0; sg_ano5=0.0; sg_ejecutado=0.0; sg_acumulado=0.0;
						
					}
					CEjecucion renglon = new CEjecucion(null,results.getInt("renglon"), results.getString("renglon_nombre"), results.getDouble("ejecutado_1"), 
							results.getDouble("ejecutado_2"), results.getDouble("ejecutado_3"), results.getDouble("ejecutado_4"), results.getDouble("ejecutado_5"), 
							null, 0.0, 0.0, results.getDouble("ejecutado_actual"), results.getDouble("acumulado_actual"), 
							0.0);
					sg_ano1 += results.getDouble("ejecutado_1");
					sg_ano2 += results.getDouble("ejecutado_2");
					sg_ano3 += results.getDouble("ejecutado_3");
					sg_ano4 += results.getDouble("ejecutado_4");
					sg_ano5 += results.getDouble("ejecutado_5");
					sg_ejecutado += results.getDouble("ejecutado_actual");
					sg_acumulado += results.getDouble("acumulado_actual");
					renglones.add(renglon);
				}
				lista.add(egrupo);
				g_ano1+=sg_ano1; g_ano2+=sg_ano2; g_ano3+=sg_ano3; g_ano4+=sg_ano4; g_ano5+=sg_ano5;
				g_ejecutado += sg_ejecutado; g_acumulado += sg_acumulado;
				sg_ano1=0.0; sg_ano2=0.0; sg_ano3=0.0; sg_ano4=0.0; sg_ano5=0.0; sg_ejecutado=0.0; sg_acumulado=0.0;
				esubgrupo.setAno1(sg_ano1); esubgrupo.setAno2(sg_ano2); esubgrupo.setAno3(sg_ano3); esubgrupo.setAno4(sg_ano4); esubgrupo.setAno5(sg_ano5); 
				esubgrupo.setEjecutado(sg_ejecutado); esubgrupo.setEjecutado_acumulado(sg_acumulado);
				subgrupos.add(esubgrupo);
				subgrupos.addAll(renglones);
				lista.addAll(subgrupos);
				egrupo.setAno1(g_ano1); egrupo.setAno2(g_ano2); egrupo.setAno3(g_ano3); egrupo.setAno4(g_ano4); egrupo.setAno5(g_ano5); 
				egrupo.setEjecutado(g_ejecutado); egrupo.setEjecutado_acumulado(g_acumulado);
				results.close();
				pstm1.close();
				pstm1 = conn.prepareStatement("select r.renglon, SUM(v.asignado), "+
						"sum(v.vigente_1) vigente_1, "+
						"sum(v.vigente_2) vigente_2, "+
						"sum(v.vigente_3) vigente_3, "+
						"sum(v.vigente_4) vigente_4, "+
						"sum(v.vigente_5) vigente_5, "+
						"sum(v.vigente_6) vigente_6, "+
						"sum(v.vigente_7) vigente_7, "+
						"sum(v.vigente_8) vigente_8, "+
						"sum(v.vigente_9) vigente_9, "+
						"sum(v.vigente_10) vigente_10, "+
						"sum(v.vigente_11) vigente_11, "+
						"sum(v.vigente_12) vigente_12, "+
						"sum(v.vigente_actual) vigente_actual "+
					"from renglones r left outer join vw_vigente_renglon v on ("
					+ "	v.entidad=? and v.ue=? and r.renglon=v.renglon and r.ejercicio=v.ejercicio and v.fuente IN ("+fuentes+")) "+
					"where r.ejercicio=? and r.grupo in ("+gruposGasto+") "+
					"and mod(r.renglon,10)>0 "+
					"group by r.renglon "+
					"order by r.entidad, r.unidad_ejecutora, r.renglon ");
				pstm1.setInt(1, entidad);
				pstm1.setInt(2, unidad_ejecutora);
				pstm1.setInt(3, now.getYear());
				results = pstm1.executeQuery();
				int pos = 0;
				while(results.next()){
					for(int i=0; i<lista.size(); i++){
						if(lista.get(i).getEntidad()==results.getInt("renglon")){
							pos=i;
							i=lista.size();
						}
					}
					if(pos>-1)
						lista.get(pos).setVigente(results.getDouble("vigente_"+mes));
					pos=-1;
				}
				grupo_actual = -1;
				subgrupo_actual = -1;
				Double vigente=0.0, vigente_grupo=0.0;
				ArrayList<CEjecucion> todelete=new ArrayList<CEjecucion>();
				for(int i=lista.size()-1; i>-1; i--){
					if(subgrupo_actual!=lista.get(i).getEntidad() && lista.get(i).getParent()!=null && lista.get(i).getParent()==1){
						lista.get(i).setVigente(vigente);
						subgrupo_actual= lista.get(i).getEntidad();
						vigente_grupo += vigente;
						vigente=0.0;
					}
					else if(grupo_actual!=lista.get(i).getEntidad() && lista.get(i).getParent()!=null && lista.get(i).getParent()==0){
						lista.get(i).setVigente(vigente_grupo);
						grupo_actual = lista.get(i).getEntidad();
						vigente=0.0;
						vigente_grupo=0.0;
					}
					else
						vigente+=lista.get(i).getVigente();
					if(!(lista.get(i).getAno1()>0 || lista.get(i).getAno2()>0 || lista.get(i).getAno3()>0 || lista.get(i).getAno4()>0
							|| lista.get(i).getAno5()>0 || lista.get(i).getEjecutado_acumulado()>0 || lista.get(i).getVigente()>0))
						todelete.add(lista.get(i));
				}
				lista.removeAll(todelete);
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("6", CEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return lista.size()>0 ? lista : null;
	}
	
	public static ArrayList<CEjecucion> getEntidadesEjecucionRenglon(int entidad, int mes, int ejercicio, String fuentes, String clase_registro, String gruposGasto){
		final ArrayList<CEjecucion> lista=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("SELECT gasto.entidad, SUM(CASE WHEN (gasto.ejercicio=?-5 AND gasto.mes=?) THEN gasto.total ELSE 0 END) ejecutado_1, "+
					"SUM(CASE WHEN (gasto.ejercicio=?-4 AND gasto.mes=?) THEN gasto.total ELSE 0 END) ejecutado_2, "+
					"SUM(CASE WHEN (gasto.ejercicio=?-3 AND gasto.mes=?) THEN gasto.total ELSE 0 END) ejecutado_3, "+
					"SUM(CASE WHEN (gasto.ejercicio=?-2 AND gasto.mes=?) THEN gasto.total ELSE 0 END) ejecutado_4, "+
					"SUM(CASE WHEN (gasto.ejercicio=?-1 AND gasto.mes=?) THEN gasto.total ELSE 0 END) ejecutado_5, "+
					"SUM(CASE WHEN (gasto.ejercicio=? AND gasto.mes=?) THEN gasto.total ELSE 0 END) ejecutado_actual, "+ 
					"SUM(CASE WHEN (gasto.ejercicio=? AND gasto.mes<=?) THEN gasto.total ELSE 0 END) acumulado_actual,  "+
					"r.renglon, r.nombre renglon_nombre, r.grupo, r.subgrupo "+
				  "FROM renglones r left outer join vw_gasto_renglon gasto "+
				  	"on (r.renglon = gasto.renglon and gasto.clase_registro IN (1,2,3,4) and gasto.fuente IN ("+fuentes+") and gasto.entidad = ? )"+
				  "WHERE r.ejercicio = ? and r.grupo IN ("+gruposGasto+")"+
				  "group by gasto.entidad, r.renglon, r.nombre, r.grupo, r.subgrupo "+
				  "order by r.renglon");				
				pstm1.setInt(1, ejercicio);
				pstm1.setInt(2, mes);
				pstm1.setInt(3, ejercicio);
				pstm1.setInt(4, mes);
				pstm1.setInt(5, ejercicio);
				pstm1.setInt(6, mes);
				pstm1.setInt(7, ejercicio);
				pstm1.setInt(8, mes);
				pstm1.setInt(9, ejercicio);
				pstm1.setInt(10, mes);
				pstm1.setInt(11, ejercicio);
				pstm1.setInt(12, mes);
				pstm1.setInt(13, ejercicio);
				pstm1.setInt(14, mes);
				pstm1.setInt(15, entidad);
				pstm1.setInt(16, ejercicio);
				ResultSet results = pstm1.executeQuery();
				int grupo_actual=-1;
				int subgrupo_actual =-1;
				ArrayList<CEjecucion> subgrupos=new ArrayList<CEjecucion>();
				ArrayList<CEjecucion> renglones=new ArrayList<CEjecucion>();
				
				Double g_ano1=0.0, g_ano2=0.0,g_ano3=0.0,g_ano4=0.0,g_ano5=0.0, g_ejecutado=0.0, g_acumulado=0.0;
				Double sg_ano1=0.0, sg_ano2=0.0, sg_ano3=0.0, sg_ano4=0.0, sg_ano5=0.0, sg_ejecutado=0.0, sg_acumulado=0.0;
				CEjecucion egrupo=null, esubgrupo=null;
				boolean cambio_grupo=false;
				while (results.next()){
					if(grupo_actual!=results.getInt("grupo")){
						if(grupo_actual>-1){
							g_ano1+=sg_ano1; g_ano2+=sg_ano2; g_ano3+=sg_ano3; g_ano4+=sg_ano4; g_ano5+=sg_ano5;
							g_ejecutado += sg_ejecutado; g_acumulado += sg_acumulado;
							esubgrupo.setAno1(sg_ano1); esubgrupo.setAno2(sg_ano2); esubgrupo.setAno3(sg_ano3); esubgrupo.setAno4(sg_ano4); esubgrupo.setAno5(sg_ano5); 
							esubgrupo.setEjecutado(sg_ejecutado); esubgrupo.setEjecutado_acumulado(sg_acumulado);
							egrupo.setAno1(g_ano1); egrupo.setAno2(g_ano2); egrupo.setAno3(g_ano3); egrupo.setAno4(g_ano4); egrupo.setAno5(g_ano5); 
							egrupo.setEjecutado(g_ejecutado); egrupo.setEjecutado_acumulado(g_acumulado);
							lista.add(egrupo);
							subgrupos.add(esubgrupo);
							subgrupos.addAll(renglones);
							lista.addAll(subgrupos);
							subgrupos.clear();
							renglones.clear();
							cambio_grupo = true;
							sg_ano1=0.0; sg_ano2=0.0; sg_ano3=0.0; sg_ano4=0.0; sg_ano5=0.0; sg_ejecutado=0.0; sg_acumulado=0.0;
						}
						grupo_actual = results.getInt("grupo");
						egrupo = new CEjecucion(0,grupo_actual, results.getString("renglon_nombre"), 0.0, 
								0.0, 0.0, 0.0, 0.0,null, 0.0, 0.0, 0.0, 0.0,0.0);
						g_ano1=0.0; g_ano2=0.0; g_ano3=0.0; g_ano4=0.0; g_ano5=0.0; g_ejecutado=0.0; g_acumulado=0.0;
					}
					else if(subgrupo_actual!=results.getInt("subgrupo")){
						if(subgrupo_actual>0 && !cambio_grupo){
							esubgrupo.setAno1(sg_ano1); esubgrupo.setAno2(sg_ano2); esubgrupo.setAno3(sg_ano3); esubgrupo.setAno4(sg_ano4); esubgrupo.setAno5(sg_ano5); 
							esubgrupo.setEjecutado(sg_ejecutado); esubgrupo.setEjecutado_acumulado(sg_acumulado);
							subgrupos.add(esubgrupo);
							subgrupos.addAll(renglones);
							renglones.clear();
						}
						else
							cambio_grupo=false;
						subgrupo_actual = results.getInt("subgrupo");
						esubgrupo = new CEjecucion(1,subgrupo_actual, results.getString("renglon_nombre"), 0.0, 
								0.0, 0.0, 0.0, 0.0,null, 0.0, 0.0, 0.0, 0.0,0.0);
						g_ano1+=sg_ano1; g_ano2+=sg_ano2; g_ano3+=sg_ano3; g_ano4+=sg_ano4; g_ano5+=sg_ano5;
						g_ejecutado += sg_ejecutado; g_acumulado += sg_acumulado;
						sg_ano1=0.0; sg_ano2=0.0; sg_ano3=0.0; sg_ano4=0.0; sg_ano5=0.0; sg_ejecutado=0.0; sg_acumulado=0.0;
						
					}
					else{
						CEjecucion renglon = new CEjecucion(null,results.getInt("renglon"), results.getString("renglon_nombre"), results.getDouble("ejecutado_1"), 
								results.getDouble("ejecutado_2"), results.getDouble("ejecutado_3"), results.getDouble("ejecutado_4"), results.getDouble("ejecutado_5"), 
								null, 0.0, 0.0, results.getDouble("ejecutado_actual"), results.getDouble("acumulado_actual"), 
								0.0);
						sg_ano1 += results.getDouble("ejecutado_1");
						sg_ano2 += results.getDouble("ejecutado_2");
						sg_ano3 += results.getDouble("ejecutado_3");
						sg_ano4 += results.getDouble("ejecutado_4");
						sg_ano5 += results.getDouble("ejecutado_5");
						sg_ejecutado += results.getDouble("ejecutado_actual");
						sg_acumulado += results.getDouble("acumulado_actual");
						renglones.add(renglon);
					}
				}
				g_ano1+=sg_ano1; g_ano2+=sg_ano2; g_ano3+=sg_ano3; g_ano4+=sg_ano4; g_ano5+=sg_ano5;
				g_ejecutado += sg_ejecutado; g_acumulado += sg_acumulado;
				esubgrupo.setAno1(sg_ano1); esubgrupo.setAno2(sg_ano2); esubgrupo.setAno3(sg_ano3); esubgrupo.setAno4(sg_ano4); esubgrupo.setAno5(sg_ano5); 
				esubgrupo.setEjecutado(sg_ejecutado); esubgrupo.setEjecutado_acumulado(sg_acumulado);
				egrupo.setAno1(g_ano1); egrupo.setAno2(g_ano2); egrupo.setAno3(g_ano3); egrupo.setAno4(g_ano4); egrupo.setAno5(g_ano5); 
				egrupo.setEjecutado(g_ejecutado); egrupo.setEjecutado_acumulado(g_acumulado);
				lista.add(egrupo);
				subgrupos.add(esubgrupo);
				subgrupos.addAll(renglones);
				lista.addAll(subgrupos);
				results.close();
				pstm1.close();
				pstm1 = conn.prepareStatement("select r.renglon, SUM(v.asignado), "+
						"sum(v.vigente_1) vigente_1, "+
						"sum(v.vigente_2) vigente_2, "+
						"sum(v.vigente_3) vigente_3, "+
						"sum(v.vigente_4) vigente_4, "+
						"sum(v.vigente_5) vigente_5, "+
						"sum(v.vigente_6) vigente_6, "+
						"sum(v.vigente_7) vigente_7, "+
						"sum(v.vigente_8) vigente_8, "+
						"sum(v.vigente_9) vigente_9, "+
						"sum(v.vigente_10) vigente_10, "+
						"sum(v.vigente_11) vigente_11, "+
						"sum(v.vigente_12) vigente_12, "+
						"sum(v.vigente_actual) vigente_actual "+
					"from renglones r left outer join vw_vigente_renglon v on ("
					+ "	v.entidad=? and r.renglon=v.renglon and r.ejercicio=v.ejercicio and v.fuente IN ("+fuentes+")) "+
					"where r.ejercicio=? and r.grupo in ("+gruposGasto+") "+
					"and mod(r.renglon,10)>0 "+
					"group by r.renglon "+
					"order by r.entidad, r.unidad_ejecutora, r.renglon ");
				pstm1.setInt(1, entidad);
				pstm1.setInt(2, ejercicio);
				results = pstm1.executeQuery();
				int pos = 0;
				while(results.next()){
					for(int i=0; i<lista.size(); i++){
						if(lista.get(i).getEntidad()==results.getInt("renglon")){
							pos=i;
							i=lista.size();
						}
					}
					if(pos>-1)
						lista.get(pos).setVigente(results.getDouble("vigente_"+mes));
					pos=-1;
				}
				grupo_actual = -1;
				subgrupo_actual = -1;
				Double vigente=0.0, vigente_grupo=0.0;
				ArrayList<CEjecucion> todelete=new ArrayList<CEjecucion>();
				for(int i=lista.size()-1; i>-1; i--){
					if(subgrupo_actual!=lista.get(i).getEntidad() && lista.get(i).getParent()!=null && lista.get(i).getParent()==1){
						lista.get(i).setVigente(vigente);
						subgrupo_actual= lista.get(i).getEntidad();
						vigente_grupo += vigente;
						vigente=0.0;
					}
					else if(grupo_actual!=lista.get(i).getEntidad() && lista.get(i).getParent()!=null && lista.get(i).getParent()==0){
						lista.get(i).setVigente(vigente_grupo);
						grupo_actual = lista.get(i).getEntidad();
						vigente=0.0;
						vigente_grupo=0.0;
					}
					else{
						vigente+=lista.get(i).getVigente();
					}
					if(!(lista.get(i).getAno1()>0 || lista.get(i).getAno2()>0 || lista.get(i).getAno3()>0 || lista.get(i).getAno4()>0
							|| lista.get(i).getAno5()>0 || lista.get(i).getEjecutado_acumulado()>0 || lista.get(i).getVigente()>0))
						todelete.add(lista.get(i));
				}
				lista.removeAll(todelete);
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("7", CEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return lista.size()>0 ? lista : null;
	}
	
	public static ArrayList<CEjecucion> getEntidadesEjecucionRenglonAcumulada(int entidad,int mes, int ejercicio, String fuentes, String clase_registro, String gruposGasto){
		final ArrayList<CEjecucion> lista=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("SELECT gasto.entidad, SUM(CASE WHEN (gasto.ejercicio=?-5 AND gasto.mes<=?) THEN gasto.total ELSE 0 END) ejecutado_1, "+
						"SUM(CASE WHEN (gasto.ejercicio=?-4 AND gasto.mes<=?) THEN gasto.total ELSE 0 END) ejecutado_2, "+
						"SUM(CASE WHEN (gasto.ejercicio=?-3 AND gasto.mes<=?) THEN gasto.total ELSE 0 END) ejecutado_3, "+
						"SUM(CASE WHEN (gasto.ejercicio=?-2 AND gasto.mes<=?) THEN gasto.total ELSE 0 END) ejecutado_4, "+
						"SUM(CASE WHEN (gasto.ejercicio=?-1 AND gasto.mes<=?) THEN gasto.total ELSE 0 END) ejecutado_5, "+
						"SUM(CASE WHEN (gasto.ejercicio=? AND gasto.mes=?) THEN gasto.total ELSE 0 END) ejecutado_actual, "+ 
						"SUM(CASE WHEN (gasto.ejercicio=? AND gasto.mes<=?) THEN gasto.total ELSE 0 END) acumulado_actual,  "+
						"r.renglon, r.nombre renglon_nombre, r.grupo, r.subgrupo "+
						"FROM renglones r left outer join vw_gasto_renglon gasto "+
							"on (r.renglon = gasto.renglon and gasto.clase_registro IN (1,2,3,4) and gasto.fuente IN ("+fuentes+") and gasto.entidad = ? )"+
						"WHERE r.ejercicio = ? and r.grupo IN ("+gruposGasto+")"+
						"group by gasto.entidad, r.renglon, r.nombre, r.grupo, r.subgrupo "+
						"order by r.renglon");				
				pstm1.setInt(1, ejercicio);
				pstm1.setInt(2, mes);
				pstm1.setInt(3, ejercicio);
				pstm1.setInt(4, mes);
				pstm1.setInt(5, ejercicio);
				pstm1.setInt(6, mes);
				pstm1.setInt(7, ejercicio);
				pstm1.setInt(8, mes);
				pstm1.setInt(9, ejercicio);
				pstm1.setInt(10, mes);
				pstm1.setInt(11, ejercicio);
				pstm1.setInt(12, mes);
				pstm1.setInt(13, ejercicio);
				pstm1.setInt(14, mes);
				pstm1.setInt(15, entidad);
				pstm1.setInt(16, ejercicio);
				
				ResultSet results = pstm1.executeQuery();
				int grupo_actual=-1;
				int subgrupo_actual =-1;
				ArrayList<CEjecucion> subgrupos=new ArrayList<CEjecucion>();
				ArrayList<CEjecucion> renglones=new ArrayList<CEjecucion>();
				
				Double g_ano1=0.0, g_ano2=0.0,g_ano3=0.0,g_ano4=0.0,g_ano5=0.0, g_ejecutado=0.0, g_acumulado=0.0;
				Double sg_ano1=0.0, sg_ano2=0.0, sg_ano3=0.0, sg_ano4=0.0, sg_ano5=0.0, sg_ejecutado=0.0, sg_acumulado=0.0;
				CEjecucion egrupo=null, esubgrupo=null;
				boolean cambio_grupo=false;
				while (results.next()){
					if(grupo_actual!=results.getInt("grupo")){
						if(grupo_actual>-1){
							g_ano1+=sg_ano1; g_ano2+=sg_ano2; g_ano3+=sg_ano3; g_ano4+=sg_ano4; g_ano5+=sg_ano5;
							g_ejecutado += sg_ejecutado; g_acumulado += sg_acumulado;
							esubgrupo.setAno1(sg_ano1); esubgrupo.setAno2(sg_ano2); esubgrupo.setAno3(sg_ano3); esubgrupo.setAno4(sg_ano4); esubgrupo.setAno5(sg_ano5); 
							esubgrupo.setEjecutado(sg_ejecutado); esubgrupo.setEjecutado_acumulado(sg_acumulado);
							egrupo.setAno1(g_ano1); egrupo.setAno2(g_ano2); egrupo.setAno3(g_ano3); egrupo.setAno4(g_ano4); egrupo.setAno5(g_ano5); 
							egrupo.setEjecutado(g_ejecutado); egrupo.setEjecutado_acumulado(g_acumulado);
							lista.add(egrupo);
							subgrupos.add(esubgrupo);
							subgrupos.addAll(renglones);
							lista.addAll(subgrupos);
							subgrupos.clear();
							renglones.clear();
							cambio_grupo = true;
							sg_ano1=0.0; sg_ano2=0.0; sg_ano3=0.0; sg_ano4=0.0; sg_ano5=0.0; sg_ejecutado=0.0; sg_acumulado=0.0;
						}
						grupo_actual = results.getInt("grupo");
						egrupo = new CEjecucion(0,grupo_actual, results.getString("renglon_nombre"), 0.0, 
								0.0, 0.0, 0.0, 0.0,null, 0.0, 0.0, 0.0, 0.0,0.0);
						g_ano1=0.0; g_ano2=0.0; g_ano3=0.0; g_ano4=0.0; g_ano5=0.0; g_ejecutado=0.0; g_acumulado=0.0;
					}
					if(subgrupo_actual!=results.getInt("subgrupo")){
						if(subgrupo_actual>0 && !cambio_grupo){
							esubgrupo.setAno1(sg_ano1); esubgrupo.setAno2(sg_ano2); esubgrupo.setAno3(sg_ano3); esubgrupo.setAno4(sg_ano4); esubgrupo.setAno5(sg_ano5); 
							esubgrupo.setEjecutado(sg_ejecutado); esubgrupo.setEjecutado_acumulado(sg_acumulado);
							subgrupos.add(esubgrupo);
							subgrupos.addAll(renglones);
							renglones.clear();
						}
						else
							cambio_grupo=false;
						subgrupo_actual = results.getInt("subgrupo");
						esubgrupo = new CEjecucion(1,subgrupo_actual, results.getString("renglon_nombre"), 0.0, 
								0.0, 0.0, 0.0, 0.0,null, 0.0, 0.0, 0.0, 0.0,0.0);
						g_ano1+=sg_ano1; g_ano2+=sg_ano2; g_ano3+=sg_ano3; g_ano4+=sg_ano4; g_ano5+=sg_ano5;
						g_ejecutado += sg_ejecutado; g_acumulado += sg_acumulado;
						sg_ano1=0.0; sg_ano2=0.0; sg_ano3=0.0; sg_ano4=0.0; sg_ano5=0.0; sg_ejecutado=0.0; sg_acumulado=0.0;
						
					}
					CEjecucion renglon = new CEjecucion(null,results.getInt("renglon"), results.getString("renglon_nombre"), results.getDouble("ejecutado_1"), 
							results.getDouble("ejecutado_2"), results.getDouble("ejecutado_3"), results.getDouble("ejecutado_4"), results.getDouble("ejecutado_5"), 
							null, 0.0, 0.0, results.getDouble("ejecutado_actual"), results.getDouble("acumulado_actual"), 
							0.0);
					sg_ano1 += results.getDouble("ejecutado_1");
					sg_ano2 += results.getDouble("ejecutado_2");
					sg_ano3 += results.getDouble("ejecutado_3");
					sg_ano4 += results.getDouble("ejecutado_4");
					sg_ano5 += results.getDouble("ejecutado_5");
					sg_ejecutado += results.getDouble("ejecutado_actual");
					sg_acumulado += results.getDouble("acumulado_actual");
					renglones.add(renglon);
				}
				g_ano1+=sg_ano1; g_ano2+=sg_ano2; g_ano3+=sg_ano3; g_ano4+=sg_ano4; g_ano5+=sg_ano5;
				g_ejecutado += sg_ejecutado; g_acumulado += sg_acumulado;
				esubgrupo.setAno1(sg_ano1); esubgrupo.setAno2(sg_ano2); esubgrupo.setAno3(sg_ano3); esubgrupo.setAno4(sg_ano4); esubgrupo.setAno5(sg_ano5); 
				esubgrupo.setEjecutado(sg_ejecutado); esubgrupo.setEjecutado_acumulado(sg_acumulado);
				egrupo.setAno1(g_ano1); egrupo.setAno2(g_ano2); egrupo.setAno3(g_ano3); egrupo.setAno4(g_ano4); egrupo.setAno5(g_ano5); 
				egrupo.setEjecutado(g_ejecutado); egrupo.setEjecutado_acumulado(g_acumulado);
				lista.add(egrupo);
				subgrupos.add(esubgrupo);
				subgrupos.addAll(renglones);
				lista.addAll(subgrupos);
				results.close();
				pstm1.close();
				pstm1 = conn.prepareStatement("select r.renglon, SUM(v.asignado), "+
						"sum(v.vigente_1) vigente_1, "+
						"sum(v.vigente_2) vigente_2, "+
						"sum(v.vigente_3) vigente_3, "+
						"sum(v.vigente_4) vigente_4, "+
						"sum(v.vigente_5) vigente_5, "+
						"sum(v.vigente_6) vigente_6, "+
						"sum(v.vigente_7) vigente_7, "+
						"sum(v.vigente_8) vigente_8, "+
						"sum(v.vigente_9) vigente_9, "+
						"sum(v.vigente_10) vigente_10, "+
						"sum(v.vigente_11) vigente_11, "+
						"sum(v.vigente_12) vigente_12, "+
						"sum(v.vigente_actual) vigente_actual "+
					"from renglones r left outer join vw_vigente_renglon v on ("
					+ "	v.entidad=? and r.renglon=v.renglon and r.ejercicio=v.ejercicio and v.fuente IN ("+fuentes+")) "+
					"where r.ejercicio=? and r.grupo in ("+gruposGasto+") "+
					"and mod(r.renglon,10)>0 "+
					"group by r.renglon "+
					"order by r.entidad, r.unidad_ejecutora, r.renglon ");
				pstm1.setInt(1, entidad);
				pstm1.setInt(2, ejercicio);
				results = pstm1.executeQuery();
				int pos = 0;
				while(results.next()){
					for(int i=0; i<lista.size(); i++){
						if(lista.get(i).getEntidad()==results.getInt("renglon")){
							pos=i;
							i=lista.size();
						}
					}
					if(pos>-1)
						lista.get(pos).setVigente(results.getDouble("vigente_"+mes));
					pos=-1;
				}
				grupo_actual = -1;
				subgrupo_actual = -1;
				Double vigente=0.0, vigente_grupo=0.0;
				ArrayList<CEjecucion> todelete=new ArrayList<CEjecucion>();
				for(int i=lista.size()-1; i>-1; i--){
					if(subgrupo_actual!=lista.get(i).getEntidad() && lista.get(i).getParent()!=null && lista.get(i).getParent()==1){
						lista.get(i).setVigente(vigente);
						subgrupo_actual= lista.get(i).getEntidad();
						vigente_grupo += vigente;
						vigente=0.0;
					}
					else if(grupo_actual!=lista.get(i).getEntidad() && lista.get(i).getParent()!=null && lista.get(i).getParent()==0){
						lista.get(i).setVigente(vigente_grupo);
						grupo_actual = lista.get(i).getEntidad();
						vigente=0.0;
						vigente_grupo=0.0;
					}
					else
						vigente+=lista.get(i).getVigente();
					if(!(lista.get(i).getAno1()>0 || lista.get(i).getAno2()>0 || lista.get(i).getAno3()>0 || lista.get(i).getAno4()>0
							|| lista.get(i).getAno5()>0 || lista.get(i).getEjecutado_acumulado()>0 || lista.get(i).getVigente()>0))
						todelete.add(lista.get(i));
				}
				lista.removeAll(todelete);
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("8", CEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return lista.size()>0 ? lista : null;
	}

	public static ArrayList<CEjecucion> getEntidadesProgramasEjecucion(int entidad, int unidad_ejecutora, int mes, String fuentes, String clase_registro, String gruposGasto, boolean todosgrupos){
		final ArrayList<CEjecucion> entidades=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				DateTime now = new DateTime();
				PreparedStatement pstm1 =  conn.prepareStatement("select e.entidad, e.programa, e.nom_estructura, "+
					"SUM(CASE WHEN (g.ejercicio=?-5 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_1, "+
					"SUM(CASE WHEN (g.ejercicio=?-4 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_2, "+
					"SUM(CASE WHEN (g.ejercicio=?-3 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_3, "+
					"SUM(CASE WHEN (g.ejercicio=?-2 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_4, "+
					"SUM(CASE WHEN (g.ejercicio=?-1 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_5, "+
					"SUM(CASE WHEN (g.ejercicio=? AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_actual, "+
					"SUM(CASE WHEN (g.ejercicio=? AND g.mes<=?) THEN g.total ELSE 0 END) acumulado_actual "+
					"from minfin.cp_estructuras e left outer join minfin.vw_gasto_renglon_programa g  "+
					"on (g.entidad = e.entidad and g.programa = e.programa and e.unidad_ejecutora = g.ue "
					+ "and e.subprograma=0 and e.proyecto=0 and e.actividad=0 and e.obra=0 and g.fuente IN ("+fuentes+") and g.clase_registro IN ("+clase_registro+")"
							+ "and ((g.renglon - mod( g.renglon,10)) - (mod(((g.renglon - mod( g.renglon,10))) ,100)) in ("+gruposGasto+") )) "+
					"where e.ejercicio = ? "+
					"and e.entidad = ? " +
					(unidad_ejecutora >= 0 ? "and e.unidad_ejecutora="+unidad_ejecutora+" ": "") +
					"and e.nivel_estructura = 2 "+
					"group by e.entidad, e.programa, e.nom_estructura "+
					"order by e.entidad, e.programa, e.nom_estructura ");				
				pstm1.setInt(1, now.getYear());
				pstm1.setInt(2, mes);
				pstm1.setInt(3, now.getYear());
				pstm1.setInt(4, mes);
				pstm1.setInt(5, now.getYear());
				pstm1.setInt(6, mes);
				pstm1.setInt(7, now.getYear());
				pstm1.setInt(8, mes);
				pstm1.setInt(9, now.getYear());
				pstm1.setInt(10, mes);
				pstm1.setInt(11, now.getYear());
				pstm1.setInt(12, mes);
				pstm1.setInt(13, now.getYear());
				pstm1.setInt(14, mes);
				pstm1.setInt(15, now.getYear());
				pstm1.setInt(16, entidad);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CEjecucion ue = new CEjecucion(results.getInt("entidad"),results.getInt("programa"), results.getString("nom_estructura"), results.getDouble("ejecutado_1"), 
							results.getDouble("ejecutado_2"), results.getDouble("ejecutado_3"), results.getDouble("ejecutado_4"), results.getDouble("ejecutado_5"), 
							null, 0.0, 0.0, results.getDouble("ejecutado_actual"), results.getDouble("acumulado_actual"), 
							0.0);
					entidades.add(ue);
				}
				results.close();
				pstm1.close();
				pstm1 = conn.prepareStatement("select e.entidad, e.programa, sum(v.asignado) asignado, "+
						"sum(v.vigente_1) vigente_1, "+
						"sum(v.vigente_2) vigente_2, "+
						"sum(v.vigente_3) vigente_3, "+
						"sum(v.vigente_4) vigente_4, "+
						"sum(v.vigente_5) vigente_5, "+
						"sum(v.vigente_6) vigente_6, "+
						"sum(v.vigente_7) vigente_7, "+
						"sum(v.vigente_8) vigente_8, "+
						"sum(v.vigente_9) vigente_9, "+
						"sum(v.vigente_10) vigente_10, "+
						"sum(v.vigente_11) vigente_11, "+
						"sum(v.vigente_12) vigente_12, "+
						"sum(v.vigente_actual) vigente_actual "+
					"from cp_estructuras e left outer join vw_vigente_renglon_programa v "
					+ "on (e.entidad=v.entidad and e.unidad_ejecutora=v.ue and e.programa = v.programa "
					+ "and e.subprograma=0 and e.proyecto=0 and e.actividad=0 and e.obra=0 "
					+ "and e.ejercicio=v.ejercicio and v.fuente IN ("+fuentes+") and ((v.renglon - mod( v.renglon,10)) - (mod(((v.renglon - mod( v.renglon,10))) ,100)) in ("+gruposGasto+")) ) "+
					"where e.ejercicio=? "+
					"and e.entidad = ? "+ 
					(unidad_ejecutora >= 0 ? "and e.unidad_ejecutora="+unidad_ejecutora+" ": "") +
					"and e.nivel_estructura = 2 "+
					"group by e.entidad, e.programa "+
					"order by e.entidad, e.programa ");
				pstm1.setInt(1, now.getYear());
				pstm1.setInt(2, entidad);
				results = pstm1.executeQuery();
				int pos = 0;
				while(results.next()){
					entidades.get(pos).setVigente(results.getDouble("vigente_"+mes));
					pos++;
				}
				results.close();
				pstm1.close();
				/*if(entidades.size()>1 && entidades.get(0).getEntidad()==0)
					entidades.remove(0);*/
			}
			catch(Exception e){
				CLogger.write("9", CEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return entidades.size()>0 ? entidades : null;
	}

	public static ArrayList<CEjecucion> getProgramasSubprogramasEjecucion(int entidad, int unidad_ejecutora, int programa, int mes, String fuentes, String clase_registro, String gruposGasto, boolean todosgrupos){
		final ArrayList<CEjecucion> entidades=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				DateTime now = new DateTime();
				PreparedStatement pstm1 =  conn.prepareStatement("select e.entidad, e.subprograma, e.nom_estructura, "+
					"SUM(CASE WHEN (g.ejercicio=?-5 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_1, "+
					"SUM(CASE WHEN (g.ejercicio=?-4 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_2, "+
					"SUM(CASE WHEN (g.ejercicio=?-3 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_3, "+
					"SUM(CASE WHEN (g.ejercicio=?-2 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_4, "+
					"SUM(CASE WHEN (g.ejercicio=?-1 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_5, "+
					"SUM(CASE WHEN (g.ejercicio=? AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_actual, "+
					"SUM(CASE WHEN (g.ejercicio=? AND g.mes<=?) THEN g.total ELSE 0 END) acumulado_actual "+
					"from minfin.cp_estructuras e left outer join minfin.vw_gasto_renglon_programa g  "+
					"on (g.entidad = e.entidad and g.ue = e.unidad_ejecutora and g.programa = e.programa and e.subprograma = g.subprograma "
					+ "and e.proyecto=0 and e.actividad=0 and e.obra=0 "
					+ "and g.fuente IN ("+fuentes+") and g.clase_registro IN ("+clase_registro+")"
							+ "and ((g.renglon - mod( g.renglon,10)) - (mod(((g.renglon - mod( g.renglon,10))) ,100)) in ("+gruposGasto+") )) "+
					"where e.ejercicio = ? "+
					"and e.entidad = ? " +
					(unidad_ejecutora >= 0 ? "and e.unidad_ejecutora="+unidad_ejecutora+" ": "") +
					"and e.programa = ? "+
					"and e.nivel_estructura = 3 "+
					"group by e.entidad, e.subprograma, e.nom_estructura "+
					"order by e.entidad, e.subprograma, e.nom_estructura ");				
				pstm1.setInt(1, now.getYear());
				pstm1.setInt(2, mes);
				pstm1.setInt(3, now.getYear());
				pstm1.setInt(4, mes);
				pstm1.setInt(5, now.getYear());
				pstm1.setInt(6, mes);
				pstm1.setInt(7, now.getYear());
				pstm1.setInt(8, mes);
				pstm1.setInt(9, now.getYear());
				pstm1.setInt(10, mes);
				pstm1.setInt(11, now.getYear());
				pstm1.setInt(12, mes);
				pstm1.setInt(13, now.getYear());
				pstm1.setInt(14, mes);
				pstm1.setInt(15, now.getYear());
				pstm1.setInt(16, entidad);
				pstm1.setInt(17, programa);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CEjecucion ue = new CEjecucion(results.getInt("entidad"),results.getInt("subprograma"), results.getString("nom_estructura"), results.getDouble("ejecutado_1"), 
							results.getDouble("ejecutado_2"), results.getDouble("ejecutado_3"), results.getDouble("ejecutado_4"), results.getDouble("ejecutado_5"), 
							null, 0.0, 0.0, results.getDouble("ejecutado_actual"), results.getDouble("acumulado_actual"), 
							0.0);
					entidades.add(ue);
				}
				results.close();
				pstm1.close();
				pstm1 = conn.prepareStatement("select e.entidad, e.subprograma, sum(v.asignado) asignado, "+
						"sum(v.vigente_1) vigente_1, "+
						"sum(v.vigente_2) vigente_2, "+
						"sum(v.vigente_3) vigente_3, "+
						"sum(v.vigente_4) vigente_4, "+
						"sum(v.vigente_5) vigente_5, "+
						"sum(v.vigente_6) vigente_6, "+
						"sum(v.vigente_7) vigente_7, "+
						"sum(v.vigente_8) vigente_8, "+
						"sum(v.vigente_9) vigente_9, "+
						"sum(v.vigente_10) vigente_10, "+
						"sum(v.vigente_11) vigente_11, "+
						"sum(v.vigente_12) vigente_12, "+
						"sum(v.vigente_actual) vigente_actual "+
					"from cp_estructuras e left outer join vw_vigente_renglon_programa v "
					+ "on (e.entidad=v.entidad and e.unidad_ejecutora = v.ue and e.subprograma=v.subprograma and e.programa = v.programa "
					+ "and e.proyecto=0 and e.actividad=0 and e.obra=0  "
					+ "and e.ejercicio=v.ejercicio and v.fuente IN ("+fuentes+") and ((v.renglon - mod( v.renglon,10)) - (mod(((v.renglon - mod( v.renglon,10))) ,100)) in ("+gruposGasto+")) ) "+
					"where e.ejercicio=? "+
					"and e.entidad = ? "+ 
					(unidad_ejecutora >= 0 ? "and e.unidad_ejecutora="+unidad_ejecutora+" ": "") +
					"and e.programa = ? "+
					"and e.nivel_estructura = 3 "+
					"group by e.entidad, e.subprograma "+
					"order by e.entidad, e.subprograma ");
				pstm1.setInt(1, now.getYear());
				pstm1.setInt(2, entidad);
				pstm1.setInt(3, programa);
				results = pstm1.executeQuery();
				int pos = 0;
				while(results.next()){
					entidades.get(pos).setVigente(results.getDouble("vigente_"+mes));
					pos++;
				}
				results.close();
				pstm1.close();
				/*if(entidades.size()>1 && entidades.get(0).getEntidad()==0)
					entidades.remove(0);*/
			}
			catch(Exception e){
				CLogger.write("10", CEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return entidades.size()>0 ? entidades : null;
	}

	public static ArrayList<CEjecucion> getSubprogramasProyectosEjecucion(int entidad, int unidad_ejecutora, int programa, int subprograma, int mes, String fuentes, String clase_registro, String gruposGasto, boolean todosgrupos){
		final ArrayList<CEjecucion> entidades=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				DateTime now = new DateTime();
				PreparedStatement pstm1 =  conn.prepareStatement("select e.entidad, e.proyecto"
						+ ", e.nom_estructura, "+
					"SUM(CASE WHEN (g.ejercicio=?-5 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_1, "+
					"SUM(CASE WHEN (g.ejercicio=?-4 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_2, "+
					"SUM(CASE WHEN (g.ejercicio=?-3 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_3, "+
					"SUM(CASE WHEN (g.ejercicio=?-2 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_4, "+
					"SUM(CASE WHEN (g.ejercicio=?-1 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_5, "+
					"SUM(CASE WHEN (g.ejercicio=? AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_actual, "+
					"SUM(CASE WHEN (g.ejercicio=? AND g.mes<=?) THEN g.total ELSE 0 END) acumulado_actual "+
					"from minfin.cp_estructuras e left outer join minfin.vw_gasto_renglon_programa g  "+
					"on (g.entidad = e.entidad and g.ue = e.unidad_ejecutora and g.programa = e.programa "
					+ "and e.subprograma = g.subprograma "
					+ "and e.actividad=0 and e.obra=0 "
					+ "and g.fuente IN ("+fuentes+") and g.clase_registro IN ("+clase_registro+")"
							+ "and ((g.renglon - mod( g.renglon,10)) - (mod(((g.renglon - mod( g.renglon,10))) ,100)) in ("+gruposGasto+") )) "+
					"where e.ejercicio = ? "+
					"and e.entidad = ? " +
					(unidad_ejecutora >= 0 ? "and e.unidad_ejecutora="+unidad_ejecutora+" ": "") +
					"and e.programa = ? "+
					"and e.subprograma = ? "+
					"and e.nivel_estructura = 4 "+
					"group by e.entidad, e.proyecto, e.nom_estructura "+
					"order by e.entidad, e.proyecto, e.nom_estructura ");				
				pstm1.setInt(1, now.getYear());
				pstm1.setInt(2, mes);
				pstm1.setInt(3, now.getYear());
				pstm1.setInt(4, mes);
				pstm1.setInt(5, now.getYear());
				pstm1.setInt(6, mes);
				pstm1.setInt(7, now.getYear());
				pstm1.setInt(8, mes);
				pstm1.setInt(9, now.getYear());
				pstm1.setInt(10, mes);
				pstm1.setInt(11, now.getYear());
				pstm1.setInt(12, mes);
				pstm1.setInt(13, now.getYear());
				pstm1.setInt(14, mes);
				pstm1.setInt(15, now.getYear());
				pstm1.setInt(16, entidad);
				pstm1.setInt(17, programa);
				pstm1.setInt(18, subprograma);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CEjecucion ue = new CEjecucion(results.getInt("entidad"),results.getInt("proyecto"), results.getString("nom_estructura"), results.getDouble("ejecutado_1"), 
							results.getDouble("ejecutado_2"), results.getDouble("ejecutado_3"), results.getDouble("ejecutado_4"), results.getDouble("ejecutado_5"), 
							null, 0.0, 0.0, results.getDouble("ejecutado_actual"), results.getDouble("acumulado_actual"), 
							0.0);
					entidades.add(ue);
				}
				results.close();
				pstm1.close();
				pstm1 = conn.prepareStatement("select e.entidad, e.proyecto, sum(v.asignado) asignado, "+
						"sum(v.vigente_1) vigente_1, "+
						"sum(v.vigente_2) vigente_2, "+
						"sum(v.vigente_3) vigente_3, "+
						"sum(v.vigente_4) vigente_4, "+
						"sum(v.vigente_5) vigente_5, "+
						"sum(v.vigente_6) vigente_6, "+
						"sum(v.vigente_7) vigente_7, "+
						"sum(v.vigente_8) vigente_8, "+
						"sum(v.vigente_9) vigente_9, "+
						"sum(v.vigente_10) vigente_10, "+
						"sum(v.vigente_11) vigente_11, "+
						"sum(v.vigente_12) vigente_12, "+
						"sum(v.vigente_actual) vigente_actual "+
					"from cp_estructuras e left outer join vw_vigente_renglon_programa v "
					+ "on (e.entidad=v.entidad and e.unidad_ejecutora = v.ue and e.subprograma=v.subprograma and e.programa = v.programa "
					+ "and e.proyecto=v.proyecto "
					+ "and e.actividad=0 and e.obra=0 "
					+ "and e.ejercicio=v.ejercicio and v.fuente IN ("+fuentes+") and ((v.renglon - mod( v.renglon,10)) - (mod(((v.renglon - mod( v.renglon,10))) ,100)) in ("+gruposGasto+")) ) "+
					"where e.ejercicio=? "+
					"and e.entidad = ? "+ 
					(unidad_ejecutora >= 0 ? "and e.unidad_ejecutora="+unidad_ejecutora+" ": "") +
					"and e.programa = ? "+
					"and e.subprograma = ? "+
					"and e.nivel_estructura = 4 "+
					"group by e.entidad, e.proyecto "+
					"order by e.entidad, e.proyecto ");
				pstm1.setInt(1, now.getYear());
				pstm1.setInt(2, entidad);
				pstm1.setInt(3, programa);
				pstm1.setInt(4, subprograma);
				results = pstm1.executeQuery();
				int pos = 0;
				while(results.next()){
					entidades.get(pos).setVigente(results.getDouble("vigente_"+mes));
					pos++;
				}
				results.close();
				pstm1.close();
				/*if(entidades.size()>1 && entidades.get(0).getEntidad()==0)
					entidades.remove(0);*/
			}
			catch(Exception e){
				CLogger.write("11", CEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return entidades.size()>0 ? entidades : null;
	}

	public static ArrayList<CEjecucion> getProyectosActividadesObrasEjecucion(int entidad, int unidad_ejecutora, int programa, int subprograma, int proyecto, int mes, String fuentes, String clase_registro, String gruposGasto, boolean todosgrupos){
		final ArrayList<CEjecucion> entidades=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				DateTime now = new DateTime();
				PreparedStatement pstm1 =  conn.prepareStatement("select e.entidad, e.actividad, e.obra "
						+ ", e.nom_estructura, "+
					"SUM(CASE WHEN (g.ejercicio=?-5 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_1, "+
					"SUM(CASE WHEN (g.ejercicio=?-4 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_2, "+
					"SUM(CASE WHEN (g.ejercicio=?-3 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_3, "+
					"SUM(CASE WHEN (g.ejercicio=?-2 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_4, "+
					"SUM(CASE WHEN (g.ejercicio=?-1 AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_5, "+
					"SUM(CASE WHEN (g.ejercicio=? AND g.mes=?) THEN g.total ELSE 0 END) ejecutado_actual, "+
					"SUM(CASE WHEN (g.ejercicio=? AND g.mes<=?) THEN g.total ELSE 0 END) acumulado_actual "+
					"from minfin.cp_estructuras e left outer join minfin.vw_gasto_renglon_programa g  "+
					"on (g.entidad = e.entidad and g.ue = e.unidad_ejecutora and g.programa = e.programa and e.subprograma = g.subprograma and e.proyecto=g.proyecto and e.actividad = g.actividad and e.obra = g.obra and g.fuente IN ("+fuentes+") and g.clase_registro IN ("+clase_registro+")"
							+ "and ((g.renglon - mod( g.renglon,10)) - (mod(((g.renglon - mod( g.renglon,10))) ,100)) in ("+gruposGasto+") )) "+
					"where e.ejercicio = ? "+
					"and e.entidad = ? " +
					(unidad_ejecutora >= 0 ? "and e.unidad_ejecutora="+unidad_ejecutora+" ": "") +
					"and e.programa = ? "+
					"and e.subprograma = ? "+
					"and e.proyecto = ? "+
					"and e.nivel_estructura = 5 "+
					"group by e.entidad, e.actividad, e.obra, e.nom_estructura "+
					"order by e.entidad, e.actividad, e.obra, e.nom_estructura ");				
				pstm1.setInt(1, now.getYear());
				pstm1.setInt(2, mes);
				pstm1.setInt(3, now.getYear());
				pstm1.setInt(4, mes);
				pstm1.setInt(5, now.getYear());
				pstm1.setInt(6, mes);
				pstm1.setInt(7, now.getYear());
				pstm1.setInt(8, mes);
				pstm1.setInt(9, now.getYear());
				pstm1.setInt(10, mes);
				pstm1.setInt(11, now.getYear());
				pstm1.setInt(12, mes);
				pstm1.setInt(13, now.getYear());
				pstm1.setInt(14, mes);
				pstm1.setInt(15, now.getYear());
				pstm1.setInt(16, entidad);
				pstm1.setInt(17, programa);
				pstm1.setInt(18, subprograma);
				pstm1.setInt(19, proyecto);
				ResultSet results = pstm1.executeQuery();	
				while (results.next()){
					CEjecucion ue = new CEjecucion(results.getInt("entidad"),results.getInt("actividad")+results.getInt("obra"), results.getString("nom_estructura"), results.getDouble("ejecutado_1"), 
							results.getDouble("ejecutado_2"), results.getDouble("ejecutado_3"), results.getDouble("ejecutado_4"), results.getDouble("ejecutado_5"), 
							null, 0.0, 0.0, results.getDouble("ejecutado_actual"), results.getDouble("acumulado_actual"), 
							0.0);
					entidades.add(ue);
				}
				results.close();
				pstm1.close();
				pstm1 = conn.prepareStatement("select e.entidad, e.actividad, e.obra, sum(v.asignado) asignado, "+
						"sum(v.vigente_1) vigente_1, "+
						"sum(v.vigente_2) vigente_2, "+
						"sum(v.vigente_3) vigente_3, "+
						"sum(v.vigente_4) vigente_4, "+
						"sum(v.vigente_5) vigente_5, "+
						"sum(v.vigente_6) vigente_6, "+
						"sum(v.vigente_7) vigente_7, "+
						"sum(v.vigente_8) vigente_8, "+
						"sum(v.vigente_9) vigente_9, "+
						"sum(v.vigente_10) vigente_10, "+
						"sum(v.vigente_11) vigente_11, "+
						"sum(v.vigente_12) vigente_12, "+
						"sum(v.vigente_actual) vigente_actual "+
					"from cp_estructuras e left outer join vw_vigente_renglon_programa v "
					+ "on (e.entidad=v.entidad and e.unidad_ejecutora = v.ue and e.subprograma=v.subprograma and e.programa = v.programa and e.proyecto=v.proyecto and e.actividad=v.actividad and e.obra=v.obra and e.ejercicio=v.ejercicio and v.fuente IN ("+fuentes+") and ((v.renglon - mod( v.renglon,10)) - (mod(((v.renglon - mod( v.renglon,10))) ,100)) in ("+gruposGasto+")) ) "+
					"where e.ejercicio=? "+
					"and e.entidad = ? "+ 
					(unidad_ejecutora >= 0 ? "and e.unidad_ejecutora="+unidad_ejecutora+" ": "") +
					"and e.programa = ? "+
					"and e.subprograma = ? "+
					"and e.proyecto = ? " +
					"and e.nivel_estructura = 5 "+
					"group by e.entidad, e.actividad, e.obra "+
					"order by e.entidad, e.actividad, e.obra ");
				pstm1.setInt(1, now.getYear());
				pstm1.setInt(2, entidad);
				pstm1.setInt(3, programa);
				pstm1.setInt(4, subprograma);
				pstm1.setInt(5, proyecto);
				results = pstm1.executeQuery();
				int pos = 0;
				while(results.next()){
					entidades.get(pos).setVigente(results.getDouble("vigente_"+mes));
					pos++;
				}
				results.close();
				pstm1.close();
				/*if(entidades.size()>1 && entidades.get(0).getEntidad()==0)
					entidades.remove(0);*/
			}
			catch(Exception e){
				CLogger.write("12", CEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return entidades.size()>0 ? entidades : null;
	}

	public static ArrayList<CEjecucion> getActividadesOBrasRenglonEjecucion(int entidad, int unidad_ejecutora, int programa,int subprograma,int proyecto,int actividad_obra, int mes, int ejercicio, String fuentes, String clase_registro, String gruposGasto){
		final ArrayList<CEjecucion> lista=new ArrayList<CEjecucion>();
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				DateTime now = new DateTime();
				PreparedStatement pstm1 =  conn.prepareStatement("SELECT gasto.entidad, SUM(CASE WHEN (gasto.ejercicio=?-5 AND gasto.mes=?) THEN gasto.total ELSE 0 END) ejecutado_1, "+
					"SUM(CASE WHEN (gasto.ejercicio=?-4 AND gasto.mes=?) THEN gasto.total ELSE 0 END) ejecutado_2, "+
					"SUM(CASE WHEN (gasto.ejercicio=?-3 AND gasto.mes=?) THEN gasto.total ELSE 0 END) ejecutado_3, "+
					"SUM(CASE WHEN (gasto.ejercicio=?-2 AND gasto.mes=?) THEN gasto.total ELSE 0 END) ejecutado_4, "+
					"SUM(CASE WHEN (gasto.ejercicio=?-1 AND gasto.mes=?) THEN gasto.total ELSE 0 END) ejecutado_5, "+
					"SUM(CASE WHEN (gasto.ejercicio=? AND gasto.mes=?) THEN gasto.total ELSE 0 END) ejecutado_actual, "+ 
					"SUM(CASE WHEN (gasto.ejercicio=? AND gasto.mes<=?) THEN gasto.total ELSE 0 END) acumulado_actual,  "+
					"r.renglon, r.nombre renglon_nombre, r.grupo, r.subgrupo "+
				  "FROM renglones r left outer join vw_gasto_renglon_programa gasto "+
				  	"on (r.renglon = gasto.renglon and gasto.clase_registro IN (1,2,3,4) and gasto.fuente IN ("+fuentes+") "
				  			+ "and gasto.entidad = ? " +
							(unidad_ejecutora >= 0 ? "and gasto.ue="+unidad_ejecutora+" ": "") +
				  			"and gasto.programa = ? and gasto.subprograma = ? and gasto.proyecto = ? " +
				  			"and (gasto.actividad = ? or gasto.obra = ?)) "+
				  	"WHERE r.ejercicio = ? and r.grupo IN ("+gruposGasto+")"+
					  "group by gasto.entidad, r.renglon, r.nombre, r.grupo, r.subgrupo "+
					  "order by r.renglon");	
				pstm1.setInt(1, ejercicio);
				pstm1.setInt(2, mes);
				pstm1.setInt(3, ejercicio);
				pstm1.setInt(4, mes);
				pstm1.setInt(5, ejercicio);
				pstm1.setInt(6, mes);
				pstm1.setInt(7, ejercicio);
				pstm1.setInt(8, mes);
				pstm1.setInt(9, ejercicio);
				pstm1.setInt(10, mes);
				pstm1.setInt(11, ejercicio);
				pstm1.setInt(12, mes);
				pstm1.setInt(13, ejercicio);
				pstm1.setInt(14, mes);
				pstm1.setInt(15, entidad);
				pstm1.setInt(16, programa);
				pstm1.setInt(17, subprograma);
				pstm1.setInt(18, proyecto);
				pstm1.setInt(19, actividad_obra);
				pstm1.setInt(20, actividad_obra);
				pstm1.setInt(21, ejercicio);
				ResultSet results = pstm1.executeQuery();
				int grupo_actual=-1;
				int subgrupo_actual =-1;
				ArrayList<CEjecucion> subgrupos=new ArrayList<CEjecucion>();
				ArrayList<CEjecucion> renglones=new ArrayList<CEjecucion>();
				
				Double g_ano1=0.0, g_ano2=0.0,g_ano3=0.0,g_ano4=0.0,g_ano5=0.0, g_ejecutado=0.0, g_acumulado=0.0;
				Double sg_ano1=0.0, sg_ano2=0.0, sg_ano3=0.0, sg_ano4=0.0, sg_ano5=0.0, sg_ejecutado=0.0, sg_acumulado=0.0;
				CEjecucion egrupo=null, esubgrupo=null;
				boolean cambio_grupo=false;
				while (results.next()){
					if(grupo_actual!=results.getInt("grupo")){
						if(grupo_actual>-1){
							lista.add(egrupo);
							g_ano1+=sg_ano1; g_ano2+=sg_ano2; g_ano3+=sg_ano3; g_ano4+=sg_ano4; g_ano5+=sg_ano5;
							g_ejecutado += sg_ejecutado; g_acumulado += sg_acumulado;
							sg_ano1=0.0; sg_ano2=0.0; sg_ano3=0.0; sg_ano4=0.0; sg_ano5=0.0; sg_ejecutado=0.0; sg_acumulado=0.0;
							esubgrupo.setAno1(sg_ano1); esubgrupo.setAno2(sg_ano2); esubgrupo.setAno3(sg_ano3); esubgrupo.setAno4(sg_ano4); esubgrupo.setAno5(sg_ano5); 
							esubgrupo.setEjecutado(sg_ejecutado); esubgrupo.setEjecutado_acumulado(sg_acumulado);
							subgrupos.add(esubgrupo);
							subgrupos.addAll(renglones);
							lista.addAll(subgrupos);
							egrupo.setAno1(g_ano1); egrupo.setAno2(g_ano2); egrupo.setAno3(g_ano3); egrupo.setAno4(g_ano4); egrupo.setAno5(g_ano5); 
							egrupo.setEjecutado(g_ejecutado); egrupo.setEjecutado_acumulado(g_acumulado);
							subgrupos.clear();
							renglones.clear();
							cambio_grupo = true;
						}
						grupo_actual = results.getInt("grupo");
						egrupo = new CEjecucion(0,grupo_actual, results.getString("renglon_nombre"), 0.0, 
								0.0, 0.0, 0.0, 0.0,null, 0.0, 0.0, 0.0, 0.0,0.0);
						g_ano1=0.0; g_ano2=0.0; g_ano3=0.0; g_ano4=0.0; g_ano5=0.0; g_ejecutado=0.0; g_acumulado=0.0;
					}
					if(subgrupo_actual!=results.getInt("subgrupo")){
						if(subgrupo_actual>0 && !cambio_grupo){
							esubgrupo.setAno1(sg_ano1); esubgrupo.setAno2(sg_ano2); esubgrupo.setAno3(sg_ano3); esubgrupo.setAno4(sg_ano4); esubgrupo.setAno5(sg_ano5); 
							esubgrupo.setEjecutado(sg_ejecutado); esubgrupo.setEjecutado_acumulado(sg_acumulado);
							subgrupos.add(esubgrupo);
							subgrupos.addAll(renglones);
							renglones.clear();
						}
						else
							cambio_grupo=false;
						subgrupo_actual = results.getInt("subgrupo");
						esubgrupo = new CEjecucion(1,subgrupo_actual, results.getString("renglon_nombre"), 0.0, 
								0.0, 0.0, 0.0, 0.0,null, 0.0, 0.0, 0.0, 0.0,0.0);
						g_ano1+=sg_ano1; g_ano2+=sg_ano2; g_ano3+=sg_ano3; g_ano4+=sg_ano4; g_ano5+=sg_ano5;
						g_ejecutado += sg_ejecutado; g_acumulado += sg_acumulado;
						sg_ano1=0.0; sg_ano2=0.0; sg_ano3=0.0; sg_ano4=0.0; sg_ano5=0.0; sg_ejecutado=0.0; sg_acumulado=0.0;
						
					}
					CEjecucion renglon = new CEjecucion(null,results.getInt("renglon"), results.getString("renglon_nombre"), results.getDouble("ejecutado_1"), 
							results.getDouble("ejecutado_2"), results.getDouble("ejecutado_3"), results.getDouble("ejecutado_4"), results.getDouble("ejecutado_5"), 
							null, 0.0, 0.0, results.getDouble("ejecutado_actual"), results.getDouble("acumulado_actual"), 
							0.0);
					sg_ano1 += results.getDouble("ejecutado_1");
					sg_ano2 += results.getDouble("ejecutado_2");
					sg_ano3 += results.getDouble("ejecutado_3");
					sg_ano4 += results.getDouble("ejecutado_4");
					sg_ano5 += results.getDouble("ejecutado_5");
					sg_ejecutado += results.getDouble("ejecutado_actual");
					sg_acumulado += results.getDouble("acumulado_actual");
					renglones.add(renglon);
				}
				lista.add(egrupo);
				g_ano1+=sg_ano1; g_ano2+=sg_ano2; g_ano3+=sg_ano3; g_ano4+=sg_ano4; g_ano5+=sg_ano5;
				g_ejecutado += sg_ejecutado; g_acumulado += sg_acumulado;
				sg_ano1=0.0; sg_ano2=0.0; sg_ano3=0.0; sg_ano4=0.0; sg_ano5=0.0; sg_ejecutado=0.0; sg_acumulado=0.0;
				esubgrupo.setAno1(sg_ano1); esubgrupo.setAno2(sg_ano2); esubgrupo.setAno3(sg_ano3); esubgrupo.setAno4(sg_ano4); esubgrupo.setAno5(sg_ano5); 
				esubgrupo.setEjecutado(sg_ejecutado); esubgrupo.setEjecutado_acumulado(sg_acumulado);
				subgrupos.add(esubgrupo);
				subgrupos.addAll(renglones);
				lista.addAll(subgrupos);
				egrupo.setAno1(g_ano1); egrupo.setAno2(g_ano2); egrupo.setAno3(g_ano3); egrupo.setAno4(g_ano4); egrupo.setAno5(g_ano5); 
				egrupo.setEjecutado(g_ejecutado); egrupo.setEjecutado_acumulado(g_acumulado);
				results.close();
				pstm1.close();
				pstm1 = conn.prepareStatement("select r.renglon, SUM(v.asignado), "+
						"sum(v.vigente_1) vigente_1, "+
						"sum(v.vigente_2) vigente_2, "+
						"sum(v.vigente_3) vigente_3, "+
						"sum(v.vigente_4) vigente_4, "+
						"sum(v.vigente_5) vigente_5, "+
						"sum(v.vigente_6) vigente_6, "+
						"sum(v.vigente_7) vigente_7, "+
						"sum(v.vigente_8) vigente_8, "+
						"sum(v.vigente_9) vigente_9, "+
						"sum(v.vigente_10) vigente_10, "+
						"sum(v.vigente_11) vigente_11, "+
						"sum(v.vigente_12) vigente_12, "+
						"sum(v.vigente_actual) vigente_actual "+
					"from renglones r left outer join vw_vigente_renglon_programa v on ("
					+ "	v.entidad=? " +
					(unidad_ejecutora >= 0 ? "and v.ue="+unidad_ejecutora+" ": "") +
					"and v.programa=? and v.subprograma=? and v.proyecto=? and (v.actividad=? or v.obra=?) and r.renglon=v.renglon and r.ejercicio=v.ejercicio and v.fuente IN ("+fuentes+")) "+
					"where r.ejercicio=? and r.grupo in ("+gruposGasto+") "+
					"and mod(r.renglon,10)>0 "+
					"group by r.renglon "+
					"order by r.entidad, r.unidad_ejecutora, r.renglon ");
				pstm1.setInt(1, entidad);
				pstm1.setInt(2, programa);
				pstm1.setInt(3, subprograma);
				pstm1.setInt(4, proyecto);
				pstm1.setInt(5, actividad_obra);
				pstm1.setInt(6, actividad_obra);	
				pstm1.setInt(7, now.getYear());
				results = pstm1.executeQuery();
				int pos = 0;
				while(results.next()){
					for(int i=0; i<lista.size(); i++){
						if(lista.get(i).getEntidad()==results.getInt("renglon")){
							pos=i;
							i=lista.size();
						}
					}
					if(pos>-1)
						lista.get(pos).setVigente(results.getDouble("vigente_"+mes));
					pos=-1;
				}
				grupo_actual = -1;
				subgrupo_actual = -1;
				Double vigente=0.0, vigente_grupo=0.0;
				ArrayList<CEjecucion> todelete=new ArrayList<CEjecucion>();
				for(int i=lista.size()-1; i>-1; i--){
					if(subgrupo_actual!=lista.get(i).getEntidad() && lista.get(i).getParent()!=null && lista.get(i).getParent()==1){
						lista.get(i).setVigente(vigente);
						subgrupo_actual= lista.get(i).getEntidad();
						vigente_grupo += vigente;
						vigente=0.0;
					}
					else if(grupo_actual!=lista.get(i).getEntidad() && lista.get(i).getParent()!=null && lista.get(i).getParent()==0){
						lista.get(i).setVigente(vigente_grupo);
						grupo_actual = lista.get(i).getEntidad();
						vigente=0.0;
						vigente_grupo=0.0;
					}
					else
						vigente+=lista.get(i).getVigente();
					if(!(lista.get(i).getAno1()>0 || lista.get(i).getAno2()>0 || lista.get(i).getAno3()>0 || lista.get(i).getAno4()>0
							|| lista.get(i).getAno5()>0 || lista.get(i).getEjecutado_acumulado()>0 || lista.get(i).getVigente()>0))
						todelete.add(lista.get(i));
				}
				lista.removeAll(todelete);
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("13", CEjecucionDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return lista.size()>0 ? lista : null;
	}

}
