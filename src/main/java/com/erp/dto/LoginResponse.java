package com.erp.dto;

public class LoginResponse {

    private String token;
    private String role;
    private int empCode;

    public LoginResponse() {
    }

    public LoginResponse(String token, String role, int empCode) {
        this.token = token;
        this.role = role;
        this.empCode = empCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getEmpCode() {
        return empCode;
    }

    public void setEmpCode(int empCode) {
        this.empCode = empCode;
    }

}
