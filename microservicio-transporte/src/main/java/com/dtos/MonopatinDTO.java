package com.dtos;

import com.utils.EstadoMonopatin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonopatinDTO {
    private Long parada_id;
    private EstadoMonopatin estado;


}
