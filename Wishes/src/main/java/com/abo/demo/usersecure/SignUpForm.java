package com.abo.demo.usersecure;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@FieldMatch(first = "password1", second = "password2", message = "The password fields must match")
public class SignUpForm {

	@NotEmpty
	@Size(min=2, max=30)
	private String username;

	@NotNull
	@Size(min=6, max=30)
	private String password1;
	@NotNull
	@Size(min=6, max=30)
	private String password2;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword1() {
		return password1;
	}
	public void setPassword1(String password1) {
		this.password1 = password1;
	}
	public String getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2 = password2;
	}
			
	
	
}
