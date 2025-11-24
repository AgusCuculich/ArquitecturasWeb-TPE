package com.controllers;


import com.dtos.RespuestaApi;
import com.dtos.MonopatinDTO;
import com.dtos.ReporteMonopatinesDTO;
import com.entities.Monopatin;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.services.SqlExecutorService;
import com.utils.EstadoMonopatin;
import jakarta.servlet.http.PushBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.services.MonopatinService;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/monopatines")
public class MonopatinController {

    private final MonopatinService monopatinService;
    @Autowired
    private SqlExecutorService sqlExecutorService;

    @PostMapping("/execute-sql")
    public ResponseEntity<RespuestaApi> executeSql(@RequestBody Map<String, String> request) {
        String sql = request.get("sql");
        return sqlExecutorService.executeQuery(sql);
    }

    @GetMapping("/debug")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<Monopatin> debug() {return monopatinService.getAllMonopatinesDebug();}

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANTENIMIENTO', 'USUARIO')")
    public MonopatinDTO  getMonopatinById(@PathVariable("id") long id){
        return monopatinService.getMonopatinById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANTENIMIENTO')")
    public List<MonopatinDTO> getAllMonopatines(){
        return monopatinService.getAllMonopatines();
    }

    @GetMapping("/reporte")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANTENIMIENTO')")
    public List<ReporteMonopatinesDTO> obtenerReporteMonopatines(
            @RequestParam("inicio") @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @RequestParam("fin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin,
            @RequestParam(value = "incluirPausas", defaultValue = "false") boolean incluirPausas
    ) {
        return monopatinService.generarReporteMonopatines(inicio, fin, incluirPausas);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void saveMonopatin(@RequestBody Monopatin monopatin){
        monopatinService.saveMonopatin(monopatin);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void deleteMonopatinById(@PathVariable("id")long id){
        monopatinService.deleteMonopatin(id);
    }

    @PutMapping("/{id}/parada/{parada_id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANTENIMIENTO')")
    public MonopatinDTO ubicarMonopatinEnParada(@PathVariable("id")long id, @PathVariable("parada_id")long parada_id){
        return monopatinService.ubicarMonopatinEnParada(id,parada_id);
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANTENIMIENTO')")
    public void actualizarEstado(@PathVariable("id") Long id, @RequestParam("estado") EstadoMonopatin estado) {
        monopatinService.actualizarEstado(id, estado);
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANTENIMIENTO')")
    public List<MonopatinDTO> getMonopatinesConMasViajes(
            @RequestParam("anio") int anio,
            @RequestParam("viajes") int viajes) {
        return monopatinService.getScooterStats(anio, viajes);
    }

    @GetMapping("/nearby")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public List<MonopatinDTO> getAllNearbyScooters(
            @RequestParam("latitud") double latitud,
            @RequestParam("longitud") double longitud,
            @RequestParam("radio") double radio) {
        return monopatinService.getAllNearbyScooters(latitud, longitud, radio);
    }

}
