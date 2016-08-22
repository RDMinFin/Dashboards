package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;
import org.joda.time.DateTime;

import db.utilities.CDatabase;
import pojo.CUser;
import utilities.CLogger;

public class CUserDAO {
	CUser user;

	public static boolean createUser(String username, String plainPassword, String firstname, String secondname,
			String lastname, String secondlastname, String dependence, String position) {
		try {
			RandomNumberGenerator rng = new SecureRandomNumberGenerator();
			ByteSource salt = rng.nextBytes();

			CUser user = new CUser(username, new Sha256Hash(plainPassword, salt, 1024).toBase64(), firstname,
					secondname, lastname, secondlastname, dependence, position, salt.toBase64());
			if (CDatabase.connect()) {
				PreparedStatement pstm = CDatabase.getConnection().prepareStatement(
						"INSERT INTO user(username, password, firstname, secondname, lastname, secondlastname, dependence, position, salt) "
								+ " VALUES(?,?,?,?,?,?,?,?,?) ");
				pstm.setString(1, username);
				pstm.setString(2, user.getPassword());
				pstm.setString(3, firstname);
				pstm.setString(4, secondname);
				pstm.setString(5, lastname);
				pstm.setString(6, secondlastname);
				pstm.setString(7, dependence);
				pstm.setString(8, position);
				pstm.setString(9, user.getSalt());

				if (pstm.executeUpdate() > 0) {
					pstm.close();
					CDatabase.close();
					return true;
				}
			}
		} catch (Exception e) {
			CLogger.write("1", CUserDAO.class, e);
		}
		return false;
	}

	public static boolean userLoginHistory(String username) {
		try {
			boolean ret = false;
			DateTime datetime = new DateTime();
			if (CDatabase.connect()) {
				PreparedStatement pstm = CDatabase.getConnection()
						.prepareStatement("INSERT INTO user_login VALUES(?,?)");
				pstm.setString(1, username);
				pstm.setTimestamp(2, new Timestamp(datetime.getMillis()));
				ret = pstm.executeUpdate() > 0;
				pstm.close();
				CDatabase.close();
				return ret;
			}
		} catch (Exception e) {
			CLogger.write("2", CUserDAO.class, e);
		}
		return false;
	}

	public static CUser getUser(String username) {
		CUser ret = null;
		try {
			if (CDatabase.connect()) {
				PreparedStatement pstm = CDatabase.getConnection()
						.prepareStatement("select * from user where username=?");
				pstm.setString(1, username);
				ResultSet rs = pstm.executeQuery();
				if (rs.next()) {
					ret = new CUser(rs.getString("username"), rs.getString("password"), rs.getString("firstname"),
							rs.getString("secondname"), rs.getString("lastname"), rs.getString("secondlastname"),
							rs.getString("dependence"), rs.getString("position"), rs.getString("salt"));
				}
				rs.close();
				pstm.close();
				CDatabase.close();
			}
		} catch (Exception e) {
			CLogger.write("3", CUserDAO.class, e);
		}
		return ret;
	}

	public static boolean hasUserPermiso(String username, String permission) {
		boolean ret = false;
		try {
			if (CDatabase.connect()) {
				PreparedStatement pstm = CDatabase.getConnection()
						.prepareStatement("select count(*) from user_permiso where username = ?");
				pstm.setString(1, username);
				ResultSet rs = pstm.executeQuery();
				if (rs.next())
					ret = rs.getInt(1) > 0;
				rs.close();
				pstm.close();
			}
		} catch (Throwable e) {
			CLogger.write("4", CUserDAO.class, e);
		}
		return ret;
	}
}
