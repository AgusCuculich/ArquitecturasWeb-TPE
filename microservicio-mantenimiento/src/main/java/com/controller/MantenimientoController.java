package com.controller;

import com.dto.MantenimientoDTO;
import com.entity.Mantenimiento;
import com.service.MantenimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mantenimientos")
public class MantenimientoController {

    private final MantenimientoService  mantenimientoService;

    @GetMapping("/")
    public List<MantenimientoDTO> listarMantenimientos() {
        return mantenimientoService.listarMantenimientos();
    }


    @PostMapping("/registrar")
    public Mantenimiento registrar(@RequestBody MantenimientoDTO dto) {
        return mantenimientoService.registrarMantenimiento(dto);
    }

}
