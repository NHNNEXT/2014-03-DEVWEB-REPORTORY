package entity;

public class Professor {
	String email;
	String password;
	String name;
	
	public Professor (String email, String password, String name) {
		
		this.email = email;
		this.password = password;
		this.name = name;
	}
	
	public Professor (String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "Professor [name=" + name + ", email=" + email + ", password="
				+ password + "]";
	}
	
	
}
