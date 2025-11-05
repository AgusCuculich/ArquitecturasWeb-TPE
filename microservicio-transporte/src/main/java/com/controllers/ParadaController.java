package com.controllers;

import com.dtos.ParadaDTO;
import com.entities.Parada;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.services.ParadaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paradas")
public class ParadaController {

    private final ParadaService paradaService;

    @GetMapping("/{id}")
    public ParadaDTO getParada(@PathVariable("id") Long id){
        return paradaService.getParada(id);
    }

    @GetMapping("/")
    public List<ParadaDTO> getParadas(){
        return paradaService.getParadas();
    }

    @PostMapping
    public void saveParada(@RequestBody Parada parada){
        paradaService.saveParada(parada);
    }

    @DeleteMapping("/{id}")
    public void deleteParada(@PathVariable("id") Long id){
        paradaService.deleteParada(id);
    }

}
