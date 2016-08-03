package utilities;

import java.io.InputStream;
import java.util.Properties;

public class CProperties {
	private static Properties properties;
	private static String memsql_host="";
	private static Integer memsql_port=null;
	private static String memsql_user="";
	private static String memsql_password="";
	private static String memsql_schema=null;
	private static String memsql_schema_des=null;
	
	static{
		InputStream input;
		properties = new Properties();
		input = CProperties.class.getClassLoader().getResourceAsStream("config.properties");
		try{
			properties.load(input);
			memsql_host = properties.getProperty("memsql_host");
			memsql_port = properties.getProperty("memsql_port") != null ? 
					Integer.parseInt(properties.getProperty("memsql_port")) : null;
			memsql_schema = properties.getProperty("memsql_schema");
			memsql_schema_des = properties.getProperty("memsql_schemades");
			memsql_user = properties.getProperty("memsql_user");
			memsql_password = properties.getProperty("memsql_password");
		}
		catch(Throwable e){
			CLogger.write("1", CProperties.class, e);
		}
		finally{
			
		}
	}

	public static Properties getProperties() {
		return properties;
	}

	public static void setProperties(Properties properties) {
		CProperties.properties = properties;
	}

	public static String getmemsql_host() {
		return memsql_host;
	}

	public static void setmemsql_host(String memsql_host) {
		CProperties.memsql_host = memsql_host;
	}

	public static Integer getmemsql_port() {
		return memsql_port;
	}

	public static void setmemsql_port(Integer memsql_port) {
		CProperties.memsql_port = memsql_port;
	}

	public static String getmemsql_user() {
		return memsql_user;
	}

	public static void setmemsql_user(String memsql_user) {
		CProperties.memsql_user = memsql_user;
	}

	public static String getmemsql_password() {
		return memsql_password;
	}

	public static void setmemsql_password(String memsql_password) {
		CProperties.memsql_password = memsql_password;
	}

	public static String getmemsql_schema(){
		return memsql_schema;
	}
	
	public void setmemsql_schema(String memsql_schema){
		CProperties.memsql_schema = memsql_schema;
	}
	
	public static String getmemsql_schema_des(){
		return memsql_schema_des;
	}
	
	public void setmemsql_schema_des(String memsql_schema_des){
		CProperties.memsql_schema_des = memsql_schema_des;
	}
}
