package com.controllers;


import com.dtos.MonopatinDTO;
import com.entities.Monopatin;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.servlet.http.PushBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import com.services.MonopatinService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/monopatines")
public class MonopatinController {

    private final MonopatinService monopatinService;

    @GetMapping("/{id}")
    public MonopatinDTO  getMonopatinById(@PathVariable("id") long id){
        return monopatinService.getMonopatinById(id);
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

}
