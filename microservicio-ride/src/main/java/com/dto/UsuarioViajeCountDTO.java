package com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioViajeCountDTO {
    private Long idUsuario;
    private Long cantidadViajes;
}
