package com.services;

import com.dtos.ParadaDTO;
import com.entities.Parada;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.repositories.ParadaRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ParadaService {

    private final ParadaRepository paradaRepository;

    public void saveParada(Parada parada){
        paradaRepository.save(parada);
    }

    public void deleteParada(long id){
        paradaRepository.deleteById(id);
    }
    public ParadaDTO getParada(long id){
       return paradaRepository.findByParada_id(id);
    }

    public List<ParadaDTO> getParadas(){
        return paradaRepository.getAllParadas();
    }
}
