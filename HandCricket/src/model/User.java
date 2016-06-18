package model;

public class User {

	private String name;
	private String ipAddress;

	public User() {
		super();
	}

	public User(String name, String ipAddress) {
		super();
		this.name = name;
		this.ipAddress = ipAddress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}
