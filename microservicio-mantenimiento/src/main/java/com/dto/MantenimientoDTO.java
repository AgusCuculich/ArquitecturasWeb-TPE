package com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MantenimientoDTO {
    private Date fecha_inicio;
    private Date fecha_fin;
    private Long monopatin_id;
    private Long encargado_id;
}
