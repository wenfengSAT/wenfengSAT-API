package com.wenfengSAT.api.redis;

import com.wenfengSAT.api.model.User;

/**
 * Token的Model类，可以增加字段提高安全性，例如时间戳、url签名
 */
public class Token {

    private User user;
    //随机生成的uuid
    private String token;

    public Token(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
