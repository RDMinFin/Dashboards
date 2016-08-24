package dao.transparencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import db.utilities.CDatabase;
import pojo.transparencia.CActividad;
import pojo.transparencia.CGuateCompra;
import utilities.CLogger;

public class CGuateCompraDAO {

	public static boolean crearCompra(CGuateCompra compra) {
		boolean ret = false;
		if (CDatabase.connect()) {
			try {
				PreparedStatement pstm = CDatabase.getConnection()
						.prepareStatement("INSERT INTO seg_compras (nog,npg,programa,subprograma,usuario_ing,fecha_ing)"
								+ "values (?,?,?,?,?,?)");

				if (compra.getNog() == null || compra.getNog().intValue() == 0)
					pstm.setNull(1, java.sql.Types.INTEGER);
				else
					pstm.setInt(1, compra.getNog().intValue());

				if (compra.getNpg() == null || compra.getNpg().isEmpty())
					pstm.setNull(2, java.sql.Types.VARCHAR);
				else
					pstm.setString(2, compra.getNpg());

				pstm.setInt(3, compra.getPrograma().intValue());
				pstm.setInt(4, compra.getSubprograma().intValue());
				pstm.setString(5, compra.getUsuario());
				pstm.setTimestamp(6, compra.getFecha());

				if (pstm.executeUpdate() > 0)
					ret = true;
			} catch (Exception e) {
				CLogger.write("1", CGuateCompraDAO.class, e);
			} finally {
				CDatabase.close();
			}
		}
		return ret;
	}

	public static boolean actualizarCompra(CGuateCompra compra) {
		boolean ret = false;
		if (CDatabase.connect()) {
			try {
				PreparedStatement pstm = CDatabase.getConnection()
						.prepareStatement("UPDATE seg_actividad SET " + "NOMBRE = ?, " + "DESCRIPCION = ?, "
								+ "FECHA_INICIO = ?, " + "FECHA_FIN = ?, " + "COORD_LAT=?, " + "COORD_LONG=?, "
								+ "PORCENTAJE_EJECUCION = ?, " + "ENTIDADES = ?,"
								+ "usuario_actualizacion=?, fecha_actualizacion=? " + "WHERE id = ?");
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
				if (pstm.executeUpdate() > 0)
					ret = true;
				pstm = CDatabase.getConnection().prepareStatement(
						"UPDATE seg_responsable SET " + "NOMBRE = ?, " + "CORREO = ?, " + "TELEFONO = ?, "
								+ "usuario_actualizacion=?, fecha_actualizacion=? " + "WHERE id = ?");
				pstm.setString(1, actividad.getResponsable().getNombre());
				pstm.setString(2, actividad.getResponsable().getCorreo());
				pstm.setString(3, actividad.getResponsable().getTelefono());
				pstm.setString(4, usuario);
				pstm.setTimestamp(5, new Timestamp(DateTime.now().getMillis()));
				pstm.setLong(6, actividad.getResponsable().getId());
				if (pstm.executeUpdate() > 0)
					ret = ret & true;
			} catch (Exception e) {
				CLogger.write("2", CGuateCompraDAO.class, e);
			} finally {
				CDatabase.close();
			}
		}
		return ret;
	}

	public static boolean deleteCompra(int id) {
		boolean ret = false;
		if (CDatabase.connect()) {
			try {
				PreparedStatement pstm = CDatabase.getConnection()
						.prepareStatement("DELETE FROM seg_documento " + "WHERE id =  " + id);
				if (pstm.executeUpdate() > 0)
					ret = true;
			} catch (Exception e) {
				CLogger.write("2", CGuateCompraDAO.class, e);
			} finally {
				CDatabase.close();
			}
		}
		return ret;
	}

	public static Integer numCompras() {
		Integer ret = 0;
		if (CDatabase.connect()) {
			try {
				PreparedStatement pstm = CDatabase.getConnection()
						.prepareStatement("select count(*) from seg_documento ");
				ResultSet rs = pstm.executeQuery();
				if (rs.next())
					ret = rs.getInt(1);
			} catch (Exception e) {
				CLogger.write("3", CGuateCompraDAO.class, e);
			} finally {
				CDatabase.close();
			}
		}
		return ret;
	}

	public static List<CGuateCompra> getCompras() {
		List<CGuateCompra> ret = new ArrayList<CGuateCompra>();
		if (CDatabase.connect()) {
			try {
				PreparedStatement pstm = CDatabase.getConnection().prepareStatement("select * from seg_documento "
						+ (id > 0 ? "where id_actividad=" + id : "") + " order by fecha_creacion ");
				ResultSet rs = pstm.executeQuery();
				while (rs.next()) {
					CDocumento documento = new CDocumento(rs.getInt("id"), rs.getInt("id_actividad"),
							rs.getString("nombre"), rs.getString("titulo"), rs.getString("ruta"), rs.getInt("tipo"),
							rs.getTimestamp("fecha_creacion"), rs.getString("usuario_creacion"));
					ret.add(documento);
				}
			} catch (Exception e) {
				CLogger.write("5", CGuateCompraDAO.class, e);
			} finally {
				CDatabase.close();
			}
		}
		return ret;
	}

}
