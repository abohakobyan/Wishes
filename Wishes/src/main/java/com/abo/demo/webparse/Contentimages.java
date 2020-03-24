package com.abo.demo.webparse;




import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class Contentimages {
	@Id
	private int img_id;
	private String imgpath;
	private String cid;
	public int getImg_id() {
		return img_id;
	}
	public void setImg_id(int img_id) {
		this.img_id = img_id;
	}
	public String getImgpath() {
		return imgpath;
	}
	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String ccid) {
		cid = ccid;
	}
	
	
	
	
	
}
