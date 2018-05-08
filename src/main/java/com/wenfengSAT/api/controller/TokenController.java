package com.wenfengSAT.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wenfengSAT.api.annotation.Authorization;
import com.wenfengSAT.api.common.R;
import com.wenfengSAT.api.common.ResultStatus;
import com.wenfengSAT.api.model.User;
import com.wenfengSAT.api.redis.TokenManager;
import com.wenfengSAT.api.redis.Token;
import com.wenfengSAT.api.repository.UserRepository;

/**
 * 获取和删除token的请求地址，在Restful设计中其实就对应着登录和退出登录的资源映射
 */
@RestController
@RequestMapping("/tokens")
public class TokenController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenManager tokenManager;

    @PostMapping("/login")
    public ResponseEntity<R> login(@RequestParam String username, @RequestParam String password) {
        Assert.notNull(username, "username can not be empty");
        Assert.notNull(password, "password can not be empty");
        User user = userRepository.findByUsername(username);
        if (user == null ||  //未注册
                !user.getPassword().equals(password)) {  //密码错误
            //提示用户名或密码错误
            return new ResponseEntity<>(R.error(ResultStatus.USERNAME_OR_PASSWORD_ERROR), HttpStatus.NOT_FOUND);
        }
        
        //生成一个token，保存用户登录状态
        Token model = tokenManager.createToken(user);
        return new ResponseEntity<>(R.ok(model), HttpStatus.OK);
    }

    @GetMapping("/logout")
    @Authorization
    public ResponseEntity<R> logout(@RequestHeader("token") String token) {
    	if(token == null || "".equals(token)){
    		return new ResponseEntity<>(R.error(ResultStatus.TOKEN_IS_NULL), HttpStatus.FAILED_DEPENDENCY);
    	}else{
    		tokenManager.deleteToken(token);
            return new ResponseEntity<>(R.ok(), HttpStatus.OK);
    	}
    }

}
