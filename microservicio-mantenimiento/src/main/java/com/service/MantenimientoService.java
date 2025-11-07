package com.service;


import com.client.TransporteClient;
import com.dto.MantenimientoDTO;

import com.entity.Mantenimiento;

import com.repository.MantenimientoRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

import static com.utils.EstadoMonopatin.MANTENIMIENTO;

@Service
@AllArgsConstructor
public class MantenimientoService {


    private final MantenimientoRepository mantenimientoRepository;

    private final TransporteClient transporteClient; // <— usamos el componente

    public Mantenimiento registrarMantenimiento(MantenimientoDTO dto) {

        // 1) Cambiar el estado del monopatín en el microservicio de transporte
        transporteClient.actualizarEstadoMonopatin(dto.getMonopatin_id(), MANTENIMIENTO);
     
        // 2) Guardar mantenimiento en BD local
        Mantenimiento mantenimiento = new Mantenimiento();
        mantenimiento.setFecha_inicio(dto.getFecha_inicio());
        mantenimiento.setFecha_fin(dto.getFecha_fin());
        mantenimiento.setMonopatin_id(dto.getMonopatin_id());
        mantenimiento.setEncargado_id(dto.getEncargado_id());

        return mantenimientoRepository.save(mantenimiento);
    }

    public List<MantenimientoDTO> listarMantenimientos() {
        return mantenimientoRepository.getAllMantenimientos();
    }





}
