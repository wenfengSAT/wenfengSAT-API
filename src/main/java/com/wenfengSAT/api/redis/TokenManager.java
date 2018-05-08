package com.wenfengSAT.api.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wenfengSAT.api.common.Constants;
import com.wenfengSAT.api.model.User;


import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 通过Redis存储和验证token的实现类
 */
@Component
public class TokenManager{

	@Autowired
    private RedisTemplate<String, String> redisTemplate;


    public Token createToken(User user) {
        //使用uuid作为源token
        String token = UUID.randomUUID().toString().replace("-", "");
        Token model = new Token(user, token);
        //存储到redis并设置过期时间
        redisTemplate.boundValueOps(token).set(JSON.toJSONString(user), Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return model;
    }

    public Token getToken(String token) {
        if (token == null || token.length() == 0) {
            return null;
        }
        String value = redisTemplate.opsForValue().get(token);
        User user = null;
        if(value != null){
        	user = JSON.parseObject(value, new TypeReference<User>() {});
        }
        return new Token(user, token);
    }

    public boolean checkToken(String token) {
    	if (token == null || token.length() == 0) {
            return false;
        }
    	String value = redisTemplate.opsForValue().get(token);
        if (value == null) {
            return false;
        }
        //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        redisTemplate.boundValueOps(token).expire(Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return true;
    }

    public void deleteToken(String token) {
    	redisTemplate.delete(token);
    }
}
