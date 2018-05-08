package com.wenfengSAT.api.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wenfengSAT.api.common.R;
import com.wenfengSAT.api.common.ResultStatus;
import com.wenfengSAT.api.config.interceptor.AuthorizationInterceptor;
import com.wenfengSAT.api.util.JsonUtil;


/**
 * 配置类，增加自定义拦截器
 * @see AuthorizationInterceptor
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(MvcConfig.class);
    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;

    //权限拦截
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	InterceptorRegistration ir = registry.addInterceptor(authorizationInterceptor);
    	ir.addPathPatterns("/**");
    	ir.excludePathPatterns("/tokens/login");
    	ir.excludePathPatterns("/tokens/logout");
    }
    
    //统一异常处理
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new HandlerExceptionResolver() {
            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
                R r = new R();
                if (e instanceof ServiceException) {//业务失败的异常，如“账号或密码错误”
                	r = R.error(HttpServletResponse.SC_UNAUTHORIZED, "token：无效！");
                    logger.error("统一异常处理："+e.getMessage());
                } else if (e instanceof NoHandlerFoundException) {//没有接口，名字错误
                	r = R.error(ResultStatus.NOT_FOUND);
                	logger.error("统一异常处理："+e.getMessage());
                } else if (e instanceof MissingServletRequestParameterException) {//缺少参数
                	r = R.error(ResultStatus.ERROR_PARAM_LACK);
                	logger.error("统一异常处理："+e.getMessage());
                } else if (e instanceof ServletRequestBindingException) {//缺少头信息
                	r = R.error(ResultStatus.ERROR_PARAM_HEAD);
                	logger.error("统一异常处理："+e.getMessage());
                } else if (e instanceof MethodArgumentTypeMismatchException) {//参数类型错误
                	r = R.error(ResultStatus.ERROR_PARAM_TYPE);
                	logger.error("统一异常处理："+e.getMessage());
                } else if (e instanceof HttpRequestMethodNotSupportedException) {//请求方式错误
                	r = R.error(ResultStatus.REQUEST_METHOD_ERROR);
                	logger.error("统一异常处理："+e.getMessage());
                } else if (e instanceof ServletException) {//无法访问(包括很多错误)
                	r = R.error(ResultStatus.INTERNAL_SERVER_ERROR);
                	logger.error("统一异常处理："+e.getMessage());
                } else {
                	r = R.error(ResultStatus.INTERNAL_SERVER_ERROR);
                    String message;
                    if (handler instanceof HandlerMethod) {
                        HandlerMethod handlerMethod = (HandlerMethod) handler;
                        message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                                request.getRequestURI(),
                                handlerMethod.getBean().getClass().getName(),
                                handlerMethod.getMethod().getName(),
                                e.getMessage());
                    } else {
                        message = e.getMessage();
                    }
                    logger.error("统一异常处理："+message);
                }
                JsonUtil.writer(response,r);
                return new ModelAndView();
            }

        });
    }

    //解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
    
    /**
     * 一个简单的签名认证，规则：
     * 1. 将请求参数按ascii码排序
     * 2. 拼接为a=value&b=value...这样的字符串（不包含sign）
     * 3. 混合密钥（secret）进行md5获得签名，与请求的签名进行比较
     */
    private boolean validateSign(HttpServletRequest request) {
        String requestSign = request.getParameter("sign");//获得请求签名，如sign=19e907700db7ad91318424a97c54ed57
        if (StringUtils.isEmpty(requestSign)) {
            return false;
        }
        List<String> keys = new ArrayList<String>(request.getParameterMap().keySet());
        keys.remove("sign");//排除sign参数
        Collections.sort(keys);//排序

        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key).append("=").append(request.getParameter(key)).append("&");//拼接字符串
        }
        String linkString = sb.toString();
        linkString = StringUtils.substring(linkString, 0, linkString.length() - 1);//去除最后一个'&'

        String secret = "Potato";//密钥，自己修改
        String sign = DigestUtils.md5Hex(linkString + secret);//混合密钥md5

        return StringUtils.equals(sign, requestSign);//比较
    }


}
