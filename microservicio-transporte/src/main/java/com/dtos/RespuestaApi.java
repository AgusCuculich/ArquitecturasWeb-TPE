package com.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  // ← Constructor vacío
@AllArgsConstructor // ← Constructor con todos los parámetros
public class RespuestaApi<T> {
    private boolean success;
    private String message;
    private T data;
}
