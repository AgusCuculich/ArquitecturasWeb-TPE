package com.entity;

import com.utils.Roles;
import com.utils.TiposUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data // <- genera getters, setters, equals, hashCode, toString
@NoArgsConstructor // <- constructor vacío obligatorio
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private Long mobile;
    @Column
    private Roles rol;
    @Column
    private TiposUsuario tipo;

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public void setRol(Roles rol) {
        this.rol = rol;
    }

    public void setTipo(TiposUsuario tipo) {
        this.tipo = tipo;
    }
}
