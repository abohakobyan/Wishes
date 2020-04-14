package com.abo.demo.lists;

import javax.persistence.Entity;
import javax.persistence.Id;



@Entity
public class Usercontent {
	@Id
	private String Cid;
	private String listtitle;
	private String uid;
	private boolean priv;
	
	public String getCid() {
		return Cid;
	}
	public void setCid(String cid) {
		Cid = cid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uuid) {
		uid = uuid;
	}
	public String getListtitle() {
		return listtitle;
	}
	public void setListtitle(String listtitle) {
		this.listtitle = listtitle;
	}
	public boolean isPriv() {
		return priv;
	}
	public void setPriv(boolean priv) {
		this.priv = priv;
	}
	}
