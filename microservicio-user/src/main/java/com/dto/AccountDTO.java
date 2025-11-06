package com.dto;

import lombok.RequiredArgsConstructor;

import java.util.Date;

@RequiredArgsConstructor
public class AccountDTO {
    private final String tipo;
    private final Date fecha_alta;
    private final float saldo;

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
