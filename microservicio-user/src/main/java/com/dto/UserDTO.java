package com.dto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserDTO {
    private final String name;
    private final String username;
    private final Long mobile;

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public Long getMobile() {
        return mobile;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", mobile=" + mobile +
                '}';
    }
}
