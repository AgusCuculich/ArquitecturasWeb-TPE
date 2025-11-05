package com.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import com.utils.EstadoMonopatin;

@Entity
@AllArgsConstructor
@Getter
@Setter
public class Monopatin {

    @Id
    @Column
    private Long monopatin_id;

    @Column
    private Long parada_id;

    @Column
    private Long GPS;

    @Enumerated(EnumType.STRING)
    private EstadoMonopatin estado;
}
