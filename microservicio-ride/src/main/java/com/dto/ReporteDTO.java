package com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReporteDTO {
    private LocalTime pausa;
    private Double kilometros;
    private Long idmonopatin;
}
