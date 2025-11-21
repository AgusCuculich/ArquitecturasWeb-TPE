package com.dto;

import com.utils.Roles;
import com.utils.TiposUsuario;
import lombok.*;

// ... otras importaciones

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = false) // Asegura que toString funcione limpiamente
public class UserDTO {

    // **CAMPO CLAVE AÑADIDO: password**
    private String password;

    // **CAMPO RENOMBRADO para claridad (usando 'username' en lugar de 'name')**
    private String username;

    private String surname;
    private Long mobile;
    private Roles rol;
    private TiposUsuario tipo;

    // Si estás usando Lombok @ToString, puedes eliminar el método manual.
    // Si necesitas el constructor que incluye todos los campos (incluyendo los que faltan en el DTO original):
    // public UserDTO(String username, String surname, Long mobile, Roles rol, TiposUsuario tipo) { ... }

    // **Si necesitas un constructor para el listado GetAll que usa tu repositorio (repo.getAll()):**
    public UserDTO(String username, String surname, Long mobile, Roles rol, TiposUsuario tipo) {
        this.username = username;
        this.surname = surname;
        this.mobile = mobile;
        this.rol = rol;
        this.tipo = tipo;
        // La contraseña no se devuelve en el DTO de consulta
    }
}