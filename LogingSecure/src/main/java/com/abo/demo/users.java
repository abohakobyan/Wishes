package com.abo.demo;

import javax.persistence.Entity;
import javax.persistence.Id;



@Entity
public class users {
	private String username;
	private String pass;
	@Id
	private int id;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
