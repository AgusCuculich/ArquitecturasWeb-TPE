package com.dto;

public class LoginInfo {

    private String username;
    private String password;

    // Getters y Setters necesarios para @RequestBody [cite: 177, 178]
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
