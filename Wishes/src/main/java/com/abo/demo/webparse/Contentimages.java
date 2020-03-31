package com.abo.demo.webparse;




import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class Contentimages {
	@Id
	private int img_id;
	private String imgpath;
	private String cid;
	private String title;
	private String link;
	public int getImg_id() {
		return img_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
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
