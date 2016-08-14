package com.lg.api.comm.body;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.lg.api.comm.BodyWrapper;

public class IMUserBody implements BodyWrapper {
	
	private String userName;
	
	private String password;
	
	public IMUserBody(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ContainerNode<?> getBody() {
		return JsonNodeFactory.instance.objectNode().put("username", userName).put("password", password);
	}

	public Boolean validate() {
		return StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password);
	}

}
