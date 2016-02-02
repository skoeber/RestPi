package de.skoeber.gpio;

/**
 * 
 * @author skoeber
 *
 */
public class RestPiPin {
	
	private String name;
	private int state;
	private String mode;
	
	public RestPiPin(String name, String mode, int state) {
		this.name = name;
		this.mode = mode;
		this.state = state;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}

}
