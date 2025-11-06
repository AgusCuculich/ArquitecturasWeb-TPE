package com.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mantenimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mantenimiento_id;

    @Column
    private Date fecha_inicio;

    @Column
    private Date fecha_fin;

    @Column
    private Long monopatin_id;

    @Column
    private Long encargado_id;
}
