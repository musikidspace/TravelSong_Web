package com.lg.bean;

public class ResultMsg {
	private User data;
	private String msg;
	private boolean success;
	private String sts;
	
	public User getData() {
		return data;
	}
	public boolean isSuccess() {
		return success;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getSts() {
		return sts;
	}
	public void setData(User data) {
		this.data = data;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public void setSts(String sts) {
		this.sts = sts;
	}
	
}
