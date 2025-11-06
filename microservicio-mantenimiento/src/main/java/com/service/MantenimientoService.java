package com.service;

import com.dto.MantenimientoDTO;
import com.entities.Monopatin;
import com.entity.Mantenimiento;
import com.repositories.MonopatinRepository;
import com.repository.MantenimientoRepository;
import com.utils.EstadoMonopatin;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MantenimientoService {

    private final MantenimientoRepository mantenimientoRepository;
    private final MonopatinRepository monopatinRepository;


    /*
    public Mantenimiento registrarMantenimiento(MantenimientoDTO dto) {
        Monopatin monopatin = monopatinRepository.findById(dto.getMonopatin_id())
                .orElseThrow(() -> new RuntimeException("Monopatín no encontrado"));

        monopatin.setEstado(EstadoMonopatin.MANTENIMIENTO);
        monopatinRepository.save(monopatin);

        Mantenimiento mantenimiento = new Mantenimiento();
        mantenimiento.setFecha_inicio(dto.getFecha_inicio());
        mantenimiento.setFecha_fin(dto.getFecha_fin());
        mantenimiento.setMonopatin_id(dto.getMonopatin_id());
        mantenimiento.setEncargado_id(dto.getEncargado_id());

        return mantenimientoRepository.save(mantenimiento);
    }

     */

    public List<MantenimientoDTO> listarMantenimientos() {
        return mantenimientoRepository.getAllMantenimientos();
    }





}
