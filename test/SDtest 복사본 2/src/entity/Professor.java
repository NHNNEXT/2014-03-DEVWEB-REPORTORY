package entity;

public class Professor {
	int id;
	String email;
	String password;
	String name;
	
	public Professor (int id, String email, String password, String name) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
	}
	
	public Professor (String email, String password, String name) {
		this.email = email;
		this.password = password;
		this.name = name;
	}
	
	public Professor (String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public int getId() {
		return id;
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
		return "Professor [id=" + id + ", email=" + email + ", password="
				+ password + ", name=" + name + "]";
	}

	
	
}
