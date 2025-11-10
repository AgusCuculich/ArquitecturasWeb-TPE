package com.dtos;

import com.utils.EstadoMonopatin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReporteMonopatinesDTO {
    private Double kilometrosRecorridos;
    private String requiereMantenimiento;
    private LocalTime tiempoPausa;
}
