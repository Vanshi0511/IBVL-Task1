package com.vanshika.ibvltask1.model;
public class LoginResponse {
    private String token;
    public String getToken() { return token; }

    public LoginResponse(String token) {
        this.token = token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
