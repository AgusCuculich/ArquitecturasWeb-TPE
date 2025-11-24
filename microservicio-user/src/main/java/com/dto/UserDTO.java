package com.dto;

import com.utils.Roles;
import com.utils.TiposUsuario;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = false) // Asegura que toString funcione limpiamente
public class UserDTO {


    private String password;


    private String username;

    private String surname;
    private Long mobile;
    private Roles rol;
    private TiposUsuario tipo;



    public UserDTO(String username, String surname, Long mobile, Roles rol, TiposUsuario tipo) {
        this.username = username;
        this.surname = surname;
        this.mobile = mobile;
        this.rol = rol;
        this.tipo = tipo;
        // La contraseña no se devuelve en el DTO de consulta
    }
}