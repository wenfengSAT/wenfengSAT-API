package com.wenfengSAT.api.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonUtil {

	public static void writer(HttpServletResponse response, JSONObject result) {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    public static void writer(HttpServletResponse response, Object result) {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().print(JSONArray.toJSON(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
