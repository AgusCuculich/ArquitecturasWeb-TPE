package com.dto;

import com.entity.Account;
import com.utils.Roles;
import com.utils.TiposUsuario;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
