package com.controller;

import com.dto.FeeDTO;
import com.dto.RespuestaApi;
import com.dto.RideDTO;
import com.entity.Fee;
import com.entity.Ride;
import com.service.FeeService;
import com.service.MongoExecutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fees")
public class FeeController {
    private final FeeService service;
    @Autowired
    private MongoExecutorService mongoExecutorService;

    @PostMapping("/execute-mongo")
    public ResponseEntity<RespuestaApi> executeMongo(@RequestBody Map<String, String> request) {
        String json = request.get("mongoQuery");
        return ResponseEntity.ok(mongoExecutorService.executeMongoQuery(json));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO', 'MANTENIMIENTO')")
    public List<FeeDTO> getAllFees(){
        return service.getAllFees();
    }

    @GetMapping("/debug")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<Fee> getAllFeesDebug(){
        return service.getAllFeesDebug();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO', 'MANTENIMIENTO')")
    public Optional<FeeDTO> getFee(@PathVariable("id") String id){
        return service.getFee(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void saveFee(@RequestBody FeeDTO fee){
        service.saveFee(fee);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void updateFee(@PathVariable("id") String id, @RequestBody FeeDTO updatedFee){
        service.updateFee(id,updatedFee);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void deleteFee(@PathVariable("id") String id){
        service.deleteRide(id);
    }


    @GetMapping("/facturacion")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public double getTotalFees(
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {
        return service.getTotalFees(start, end);
    }

}
