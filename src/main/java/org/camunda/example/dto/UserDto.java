package org.camunda.example.dto;

import org.camunda.bpm.engine.identity.User;

public class UserDto implements User  {
	 
	private String id;
	 private String firstName;
	 private String lastName;
	 private String password;
	 private String email;
	 
	 
	public UserDto(String id) {
		
		this.id = id;
		
	}
	
	public UserDto() {
		
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	 
	 
	 
}
