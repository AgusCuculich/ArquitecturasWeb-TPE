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
    private Long idMonopatin;
    private Double kilometrosRecorridos;
    private String requiereMantenimiento;
    private LocalTime tiempoPausa;

   /* public ReporteMonopatinesDTO(LocalTime tiempoPausa, Double kilometrosRecorridos, Long idMonopatin) {
        this.tiempoPausa = tiempoPausa;
        this.kilometrosRecorridos = kilometrosRecorridos;
        this.idMonopatin = idMonopatin;
    }

    */
    private String tiempoPausa;
}
