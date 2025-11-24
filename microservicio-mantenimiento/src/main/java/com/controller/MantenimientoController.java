package com.controller;

import com.dto.MantenimientoDTO;
import com.entity.Mantenimiento;
import com.service.MantenimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mantenimientos")
public class MantenimientoController {

    private final MantenimientoService  mantenimientoService;

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANTENIMIENTO')")
    public List<MantenimientoDTO> listarMantenimientos() {
        return mantenimientoService.listarMantenimientos();
    }


    @PostMapping("/registrar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANTENIMIENTO')")
    public Mantenimiento registrar(@RequestBody MantenimientoDTO dto) {
        return mantenimientoService.registrarMantenimiento(dto);
    }
    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANTENIMIENTO')")
    public void eliminar(@PathVariable("id") Long id) {
        mantenimientoService.deleteMantenimiento(id);
    }


}
