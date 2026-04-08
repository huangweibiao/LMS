package com.lms.dto.response;

import java.util.Date;

/**
 * 登录响应DTO
 */
public class LoginResponse {
    
    /** 访问令牌 */
    private String token;
    
    /** 令牌类型 */
    private String tokenType = "Bearer";
    
    /** 用户信息 */
    private UserResponse user;
    
    // 构造方法
    public LoginResponse() {
    }
    
    public LoginResponse(String token, UserResponse user) {
        this.token = token;
        this.user = user;
    }
    
    // Getter和Setter
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public UserResponse getUser() { return user; }
    public void setUser(UserResponse user) { this.user = user; }
}