package com.example.oftapp.backend.dto;

public class LoginRequest {
    private String email;
    private String password;
    private String rol;

    public LoginRequest() {
    }

    public LoginRequest(String email, String password, String rol) {
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
