package com.dto;

import com.entity.Account;
import com.utils.Roles;
import com.utils.TiposUsuario;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {
    private String name;
    private String surname;
    private Long mobile;
    private Roles rol;
    private TiposUsuario tipo;



    @Override
    public String toString() {
        return "UserDTO{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", mobile=" + mobile +
                ", rol=" + rol +
                ", tipo=" + tipo +
                '}';
    }
}
