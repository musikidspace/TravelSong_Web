package com.lg.api;

import java.io.File;

import com.lg.api.comm.BodyWrapper;
import com.lg.api.comm.HeaderWrapper;
import com.lg.api.comm.QueryWrapper;
import com.lg.api.comm.ResponseWrapper;

public interface RestAPIInvoker {
	ResponseWrapper sendRequest(String method, String url, HeaderWrapper header, BodyWrapper body, QueryWrapper query);
	ResponseWrapper uploadFile(String url, HeaderWrapper header, File file);
    ResponseWrapper downloadFile(String url, HeaderWrapper header, QueryWrapper query);
}
