package com.abo.demo.lists;

import javax.persistence.Entity;
import javax.persistence.Id;



@Entity
public class Usercontent {
	@Id
	private String Cid;
	private String listtitle;
	private int uid;
	
	
	public String getCid() {
		return Cid;
	}
	public void setCid(String cid) {
		Cid = cid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uuid) {
		uid = uuid;
	}
	public String getListtitle() {
		return listtitle;
	}
	public void setListtitle(String listtitle) {
		this.listtitle = listtitle;
	}
	}
