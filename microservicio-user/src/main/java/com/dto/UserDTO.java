package com.dto;

import com.utils.Roles;
import com.utils.TiposUsuario;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserDTO {
    private final String name;
    private final String username;
    private final Long mobile;
    private final Roles rol;
    private final TiposUsuario tipo;

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public Long getMobile() {
        return mobile;
    }

    public Roles getRol() {
        return rol;
    }

    public TiposUsuario getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", mobile=" + mobile +
                ", rol=" + rol +
                ", tipo=" + tipo +
                '}';
    }
}
