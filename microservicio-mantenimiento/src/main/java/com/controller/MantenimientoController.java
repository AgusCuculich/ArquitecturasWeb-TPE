package com.controller;

import com.dto.MantenimientoDTO;
import com.dto.RespuestaApi;
import com.entity.Mantenimiento;
import com.service.MantenimientoService;
import com.service.SqlExecutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mantenimientos")
public class MantenimientoController {

    private final MantenimientoService  mantenimientoService;
    @Autowired
    private SqlExecutorService sqlExecutorService;

    @PostMapping("/execute-sql")
    public ResponseEntity<RespuestaApi> executeSql(@RequestBody Map<String, String> request) {
        String sql = request.get("sql");
        return sqlExecutorService.executeQuery(sql);
    }

    @GetMapping("/")
    public List<MantenimientoDTO> listarMantenimientos() {
        return mantenimientoService.listarMantenimientos();
    }


    @PostMapping("/registrar")
    public Mantenimiento registrar(@RequestBody MantenimientoDTO dto) {
        return mantenimientoService.registrarMantenimiento(dto);
    }
    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable("id") Long id) {
        mantenimientoService.deleteMantenimiento(id);
    }


}
