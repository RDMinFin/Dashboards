package utilities;

import org.apache.log4j.Logger;

public class CLogger {

	private static Logger log;
	
	public CLogger(){
		
	}
	
	static public void write(String str, Object obj, Throwable e){
		log=Logger.getLogger(obj.getClass());
		log.error(str,e);
	}
	
	static public void write_simple(String error_num, Object obj, String error){
		log=Logger.getLogger(obj.getClass());
		log.error(String.join(" ",obj.toString(), error_num,"\n"+error));		
	}	
}
