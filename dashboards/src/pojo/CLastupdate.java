package pojo;

import java.sql.Timestamp;

public class CLastupdate {
	String dashboard_name;
	Timestamp last_update;
	
	public CLastupdate(String dashboard_name, Timestamp last_update) {
		super();
		this.dashboard_name = dashboard_name;
		this.last_update = last_update;
	}

	public String getDashboard_name() {
		return dashboard_name;
	}

	public void setDashboard_name(String dashboard_name) {
		this.dashboard_name = dashboard_name;
	}

	public Timestamp getLast_update() {
		return last_update;
	}

	public void setLast_update(Timestamp last_update) {
		this.last_update = last_update;
	}
	
	
}
