package com.controllers;


import com.dtos.MonopatinDTO;
import com.dtos.ReporteMonopatinesDTO;
import com.entities.Monopatin;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.utils.EstadoMonopatin;
import jakarta.servlet.http.PushBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.services.MonopatinService;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/monopatines")
public class MonopatinController {

    private final MonopatinService monopatinService;

    @GetMapping("/{id}")
    public MonopatinDTO  getMonopatinById(@PathVariable("id") long id){
        return monopatinService.getMonopatinById(id);
    }

    @GetMapping
    public List<MonopatinDTO> getAllMonopatines(){
        return monopatinService.getAllMonopatines();
    }

    @GetMapping("/reporte")
    public List<ReporteMonopatinesDTO> obtenerReporteMonopatines(
            @RequestParam("inicio") @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @RequestParam("fin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin,
            @RequestParam(value = "incluirPausas", defaultValue = "false") boolean incluirPausas
    ) {
        return monopatinService.generarReporteMonopatines(inicio, fin, incluirPausas);
    }

    @PostMapping
    public void saveMonopatin(@RequestBody Monopatin monopatin){
        monopatinService.saveMonopatin(monopatin);
    }

    @DeleteMapping("/{id}")
    public void deleteMonopatinById(@PathVariable("id")long id){
        monopatinService.deleteMonopatin(id);
    }

    @PutMapping("/{id}/parada/{parada_id}")
    public MonopatinDTO ubicarMonopatinEnParada(@PathVariable("id")long id, @PathVariable("parada_id")long parada_id){
        return monopatinService.ubicarMonopatinEnParada(id,parada_id);
    }

    @PutMapping("/{id}/estado")
    public void actualizarEstado(@PathVariable("id") Long id, @RequestParam("estado") EstadoMonopatin estado) {
        monopatinService.actualizarEstado(id, estado);
    }





}
