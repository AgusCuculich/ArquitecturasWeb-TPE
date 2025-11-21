package com.dto;

public class Token {

    private String token;
    private String error;

    // Constructor usado en el ejemplo del PDF [cite: 180, 181]
    public Token(String token, String error) {
        this.token = token;
        this.error = error;
    }

    // Getters y Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}

