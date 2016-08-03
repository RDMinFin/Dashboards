package pojo;

public class CGridState {
	private String username;
	private String grid;
	private String state;
	
	public CGridState(String username, String grid, String state) {
		super();
		this.username = username;
		this.grid = grid;
		this.state = state;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGrid() {
		return grid;
	}

	public void setGrid(String grid) {
		this.grid = grid;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}
