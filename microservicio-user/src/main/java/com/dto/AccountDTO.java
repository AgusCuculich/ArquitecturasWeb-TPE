package com.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String tipo;
    private Date fecha_alta;
    private float saldo;

    public String getTipo() {
        return tipo;
    }

    public Date getFecha_alta() {
        return fecha_alta;
    }

    public float getSaldo() {
        return saldo;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "tipo='" + tipo + '\'' +
                ", fecha_alta=" + fecha_alta +
                ", saldo=" + saldo +
                '}';
    }
}
