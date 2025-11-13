package com.dtos;

import com.utils.EstadoMonopatin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReporteMonopatinesDTO {
    private LocalTime pausa;
    private Double kilometros;
    private Long idmonopatin;
    private String requiereMantenimiento;
}

