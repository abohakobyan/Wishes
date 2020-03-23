package com.abo.demo;

import javax.persistence.Entity;
import javax.persistence.Id;



@Entity
public class Usercontent {
	@Id
	private String Cid;
	private String link;
	private int uid;
	
	public String getLink() {
		return link;
	}
	public String getCid() {
		return Cid;
	}
	public void setCid(String cid) {
		Cid = cid;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uuid) {
		uid = uuid;
	}
	}
