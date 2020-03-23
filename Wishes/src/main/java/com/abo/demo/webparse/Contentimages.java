package com.abo.demo.webparse;




import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class Contentimages {
	@Id
	private int img_id;
	private String imgpath;
	private int uid;
	
	
	
	
	public int getCid() {
		return img_id;
	}
	public void setCid(int cid) {
		this.img_id = cid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	
	public String getImgpath() {
		return imgpath;
	}
	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}
	
}
