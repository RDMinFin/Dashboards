package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import db.utilities.CDatabase;
import pojo.CLastupdate;
import utilities.CLogger;

public class CLastupdateDAO {
	
	public static CLastupdate getLastupdate(String dashboard_name){
		CLastupdate update=null;
		if(CDatabase.connect()){
			Connection conn = CDatabase.getConnection();
			try{
				PreparedStatement pstm1 =  conn.prepareStatement("select * from update_log where dashboard_name=?");
				pstm1.setString(1, dashboard_name);
				ResultSet results = pstm1.executeQuery();	
				if (results.next()){
					update = new CLastupdate(results.getString("dashboard_name"), results.getTimestamp("last_update"));
				}
				results.close();
				pstm1.close();
			}
			catch(Exception e){
				CLogger.write("1", CLastupdateDAO.class, e);
			}
			finally{
				CDatabase.close(conn);
			}
		}
		return update;
	}
}
