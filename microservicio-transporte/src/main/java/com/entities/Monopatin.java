package com.entities;

import jakarta.persistence.*;
import lombok.*;
import com.utils.EstadoMonopatin;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Monopatin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long monopatin_id;

    @Column
    private Long parada_id;

    @Column
    private Long GPS;

    @Enumerated(EnumType.STRING)
    private EstadoMonopatin estado;
}
