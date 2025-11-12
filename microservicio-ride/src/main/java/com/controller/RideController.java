package com.controller;

import com.dto.RideCountResult;
import com.dto.ReporteDTO;
import com.dto.RideDTO;
import com.entity.Ride;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.service.RideService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rides")
public class RideController {
    private final RideService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RideDTO> getAllRides() {
        return service.getAllRides();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<RideDTO> getRide(@PathVariable("id") String id) {return service.getRide(id);}

    @GetMapping("/reporte")
    @ResponseStatus(HttpStatus.OK)
    public List<ReporteDTO> obtenerReporte(
            @RequestParam("inicio") @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @RequestParam("fin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin,
            @RequestParam(value = "incluirPausas", defaultValue = "false") boolean incluirPausas
    ) {
        return service.generarReporte(inicio, fin, incluirPausas);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveRide(@RequestBody RideDTO ride) {service.saveRide(ride);}

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRide(@PathVariable("id") String id) {service.deleteRide(id);}

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateRide(@PathVariable("id") String id, @RequestBody RideDTO updatedRide) {service.updateRide(id, updatedRide);}

    @GetMapping("/stats")
    public List<RideCountResult> getMonopatinesConMasViajes(
            @RequestParam("anio") int anio,
            @RequestParam("viajes") int viajes) {
        return service.findMonopatinesConMasViajesEnAnio(anio, viajes);
    }
}
