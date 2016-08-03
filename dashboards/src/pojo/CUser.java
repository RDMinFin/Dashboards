package pojo;

public class CUser {
	String username;
	String password;
	String firstname;
	String secondname;
	String lastname;
	String secondlastname;
	String dependence;
	String position;
	String salt;
	
	public CUser(String username, String password, String firstname, String secondname, String lastname,
			String secondlastname, String dependence, String position, String salt) {
		super();
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.secondname = secondname;
		this.lastname = lastname;
		this.secondlastname = secondlastname;
		this.dependence = dependence;
		this.position = position;
		this.salt = salt;
	}
	
	public CUser(String username, String password, String firstname, String lastname, 
			String dependence, String position, String salt){
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.dependence = dependence;
		this.position = position;
		this.salt = salt;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSecondname() {
		return secondname;
	}

	public void setSecondname(String secondname) {
		this.secondname = secondname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getSecondlastname() {
		return secondlastname;
	}

	public void setSecondlastname(String secondlastname) {
		this.secondlastname = secondlastname;
	}

	public String getDependence() {
		return dependence;
	}

	public void setDependence(String dependence) {
		this.dependence = dependence;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	public String getSalt(){
		return salt;
	}
	
	public void setSalt(String salt){
		this.salt = salt;
	}
	
	
}
