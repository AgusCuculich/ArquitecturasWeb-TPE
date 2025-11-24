package com.controller;

import com.dto.*;
import com.entity.Ride;
import com.dto.ScootersUseDTO;

import com.service.MongoExecutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.service.RideService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rides")
public class RideController {
    private final RideService service;
    @Autowired
    private MongoExecutorService mongoExecutorService;

    @PostMapping("/execute-mongo")
    public ResponseEntity<RespuestaApi> executeMongo(@RequestBody Map<String, String> request) {
        String json = request.get("mongoQuery");
        return ResponseEntity.ok(mongoExecutorService.executeMongoQuery(json));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<RideDTO> getAllRides() {
        return service.getAllRides();
    }

    @GetMapping("/debug")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<Ride> getAllRidesDebug() {
        return service.getAllRidesDebug();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public Optional<RideDTO> getRide(@PathVariable("id") String id) {return service.getRide(id);}

    @GetMapping("/reporte")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANTENIMIENTO')")
    public List<ReporteDTO> obtenerReporte(
            @RequestParam("inicio") @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @RequestParam("fin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin,
            @RequestParam(value = "incluirPausas", defaultValue = "false") boolean incluirPausas
    ) {
        return service.generarReporte(inicio, fin, incluirPausas);
    }


    @GetMapping("/ranking-usuarios")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<UsuarioViajeCountDTO> rankingUsuarios(
            @RequestParam("desde") @DateTimeFormat(pattern = "yyyy-MM-dd") Date desde,
            @RequestParam("hasta") @DateTimeFormat(pattern = "yyyy-MM-dd") Date hasta
    ) {
        return service.getUsuariosConCantidadDeViajes(desde, hasta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public void saveRide(@RequestBody RideDTO ride) {service.saveRide(ride);}

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void deleteRide(@PathVariable("id") String id) {service.deleteRide(id);}

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public void updateRide(@PathVariable("id") String id, @RequestBody RideDTO updatedRide) {service.updateRide(id, updatedRide);}

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANTENIMIENTO')")
    public List<RideCountResult> getMonopatinesConMasViajes(
            @RequestParam("anio") int anio,
            @RequestParam("viajes") int viajes) {
        return service.findMonopatinesConMasViajesEnAnio(anio, viajes);
    }


    @GetMapping("/scooterUse/{userId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public ScootersUseDTO getScooterUse(
            @PathVariable("userId") Long userId,
            @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        return service.getScootersUseByUser(userId, startDate, endDate);
    }


}
