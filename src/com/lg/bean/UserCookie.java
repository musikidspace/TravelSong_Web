package com.lg.bean;

public class UserCookie {
	private String usercode;
	private String cookieverify;
	private long expires;
	private String path;
	private String Purpose;
	private String createtime;
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public String getCookieverify() {
		return cookieverify;
	}
	public void setCookieverify(String cookieverify) {
		this.cookieverify = cookieverify;
	}
	public long getExpires() {
		return expires;
	}
	public void setExpires(long expires) {
		this.expires = expires;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPurpose() {
		return Purpose;
	}
	public void setPurpose(String purpose) {
		Purpose = purpose;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
}
