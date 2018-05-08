package com.wenfengSAT.api.common;

/**
 * 自定义请求状态码
 */
public enum ResultStatus {
	OK(0, "成功"),
    SUCCESS(100, "成功"),
    NOT_FOUND(404,"接口不存在"),
    INTERNAL_SERVER_ERROR(500,"服务器内部错误"),
    UNAUTHORIZED(-1000,"未认证"),//未认证（签名错误）
    USERNAME_OR_PASSWORD_ERROR(-1001, "用户名或密码错误"),
    USER_NOT_FOUND(-1002, "用户不存在"),
    USER_NOT_LOGIN(-1003, "用户未登录"),
    TOKEN_IS_NULL(-1004, "token未传入"),
    TOKEN_FAIL(-1005, "token失效，需重新获取"),
    ERROR(200, "失败"),
	ERROR_PARAM_LACK(-2001, "参数错误,缺少参数"),
	ERROR_PARAM_TYPE(-2002, "参数错误,参数类型错误"),
	REQUEST_METHOD_ERROR(-2003, "请求方式错误"),
	ERROR_PARAM_HEAD(-2004, "参数错误,缺少头信息参数");
    /**
     * 返回码
     */
    private int code;
    /**
     * 返回结果描述
     */
    private String message;

    ResultStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
