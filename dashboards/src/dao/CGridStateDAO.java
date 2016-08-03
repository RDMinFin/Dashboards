package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import db.utilities.CDatabase;
import pojo.CGridState;
import utilities.CLogger;

public class CGridStateDAO {
	
	public static boolean saveGridState(CGridState gridstate){
		boolean ret=false;
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm =  CDatabase.getConnection().prepareStatement("INSERT INTO grid_state(username, grid, state) VALUES (?,?,?) "+ 
			"ON DUPLICATE KEY UPDATE state = ?");
				pstm.setString(1, gridstate.getUsername());
				pstm.setString(2, gridstate.getGrid());
				pstm.setString(3, gridstate.getState());
				pstm.setString(4, gridstate.getState());
				if (pstm.executeUpdate()>0)
					ret=true;
				pstm.close();
			}
			catch(Exception e){
				CLogger.write("1", CGridStateDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return ret;
	}
	
	public static String getGridState(String username, String grid){
		String ret = "";
		if(CDatabase.connect()){
			try{
				PreparedStatement pstm = CDatabase.getConnection().prepareStatement("SELECT * FROM grid_state WHERE username = ? AND grid = ?");
				pstm.setString(1, username);
				pstm.setString(2, grid);
				ResultSet rs = pstm.executeQuery();
				if(rs.next()){
					ret = rs.getString("state");
				}
				rs.close();
				pstm.close();
				
			}
			catch(Exception e){
				CLogger.write("2", CGridStateDAO.class, e);
			}
			finally{
				CDatabase.close();
			}
		}
		return ret;
	}
}
