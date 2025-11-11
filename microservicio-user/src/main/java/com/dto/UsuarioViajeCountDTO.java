package com.dto;

import com.utils.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioViajeCountDTO {
        private Long id;
        private String nombre;
        private String apellido;
        private Roles rol;
        private Long cantidadViajes;


}
