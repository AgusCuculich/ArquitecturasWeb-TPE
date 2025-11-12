package com.service;


import com.client.TransporteClient;
import com.dto.MantenimientoDTO;

import com.entity.Mantenimiento;

import com.repository.MantenimientoRepository;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.utils.EstadoMonopatin.MANTENIMIENTO;

@Service
@AllArgsConstructor
public class MantenimientoService {


    private final MantenimientoRepository mantenimientoRepository;

    private final TransporteClient transporteClient;

    public Mantenimiento registrarMantenimiento(MantenimientoDTO dto) {

        transporteClient.actualizarEstadoMonopatin(dto.getMonopatin_id(), MANTENIMIENTO);

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


    public void deleteMantenimiento(long id) {
        if (!mantenimientoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mantenimiento no encontrado");
        }
        mantenimientoRepository.deleteById(id);
    }

}
