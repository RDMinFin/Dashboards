package db.utilities;


import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import utilities.CLogger;
import utilities.CProperties;


public class CDatabase {
	private static Connection connection;
	private static Connection connection_des;
	private static String host;
	private static Integer port;
	private static String user;
	private static String password;
	private static String schema;
	private static String schema_des;

	
	static {
		host = CProperties.getmemsql_host();
		port = CProperties.getmemsql_port();
		user = CProperties.getmemsql_user();
		password = CProperties.getmemsql_password();
		schema = CProperties.getmemsql_schema();
		schema_des = CProperties.getmemsql_schema_des();
	}
	
	public static boolean connect(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(String.join("", "jdbc:mysql://",String.valueOf(host),":", String.valueOf(port),"/",schema), user, password);
			return !connection.isClosed();
		}
		catch(Exception e){
			CLogger.write("1", CDatabase.class, e);
		}
		return false;
	}
	
	public static boolean connectDes(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection_des = DriverManager.getConnection(String.join("", "jdbc:mysql://",String.valueOf(host),":", String.valueOf(port),"/",schema_des), user, password);
			return !connection_des.isClosed();
		}
		catch(Exception e){
			CLogger.write("2", CDatabase.class, e);
		}
		return false;
	}
	
	public static Connection getConnection(){
		return connection;
	}
	
	public static Connection getConnection_des(){
		return connection_des;
	}
	
	public static void close(){
		try {
			connection.close();
		} catch (SQLException e) {
			CLogger.write("3", CDatabase.class, e);
		}
	}
	
	public static void close_des(){
		try {
			connection_des.close();
		} catch (SQLException e) {
			CLogger.write("4", CDatabase.class, e);
		}
	}
	
	public static void close(Connection conn){
		try {
			conn.close();
		} catch (SQLException e) {
			CLogger.write("5", CDatabase.class, e);
		}
	}
}
