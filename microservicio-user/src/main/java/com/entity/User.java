package com.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.utils.Roles;
import com.utils.TiposUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@ToString(exclude = "accounts")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column
    private String password;
    @Column
    private String surname;
    @Column
    private Long mobile;
    @Column
    private Roles rol;
    @Column
    private TiposUsuario tipo;
    @Column(columnDefinition = "boolean default false")
    private boolean isDisabled = false;
    @ManyToMany
    @JoinTable(
            name = "usuario_account",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    @JsonManagedReference
    private List<Account> accounts;

    public void setName(String name) {
        this.username = name;
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

    public void setDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }
}
