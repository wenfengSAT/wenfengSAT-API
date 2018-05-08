package com.wenfengSAT.api.config.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.wenfengSAT.api.annotation.Authorization;
import com.wenfengSAT.api.common.Constants;
import com.wenfengSAT.api.common.R;
import com.wenfengSAT.api.common.ResultStatus;
import com.wenfengSAT.api.redis.Token;
import com.wenfengSAT.api.redis.TokenManager;
import com.wenfengSAT.api.util.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;

/**
 * 自定义拦截器，判断token是否有效
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private TokenManager manager;

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler){
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //从header中得到token
        String authorization = request.getHeader(Constants.AUTHORIZATION);
        if(authorization == null){//token获取失败
        	JsonUtil.writer(response,R.error(ResultStatus.TOKEN_IS_NULL));
            return false; 
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //如果验证token失败，并且方法注明了Authorization，返回401错误
        if (method.getAnnotation(Authorization.class) != null) {
            JsonUtil.writer(response,R.error(HttpServletResponse.SC_UNAUTHORIZED, "token验证错误！"));
            return false;  
        }
        //验证token
        Token model = manager.getToken(authorization);
        if (model != null) {
        	manager.checkToken(model.getToken());
            //如果token验证成功，将token对应的用户id存在request中，便于之后注入
            request.setAttribute(Constants.CURRENT_USER_ID, model.getUser());
            return true;
        }else{
        	JsonUtil.writer(response,R.error(ResultStatus.TOKEN_FAIL));
            return false;
        }
    }
    
}
