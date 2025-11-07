package com.services;

import com.dtos.MonopatinDTO;
import com.entities.Monopatin;
import com.utils.EstadoMonopatin;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.repositories.MonopatinRepository;

@AllArgsConstructor
@Service
public class MonopatinService {

    private final MonopatinRepository monopatinRepository;

    public void saveMonopatin(Monopatin monopatin){
        monopatinRepository.save(monopatin);
    }

    public void deleteMonopatin(long id){
        monopatinRepository.deleteById(id);
    }

    public MonopatinDTO ubicarMonopatinEnParada(Long monopatinId, Long paradaId) {
        Monopatin monopatin = monopatinRepository.findById(monopatinId)
                .orElseThrow(() -> new RuntimeException("Monopatín no encontrado"));

        monopatin.setParada_id(paradaId);
        monopatinRepository.save(monopatin);
        return monopatinRepository.getMonopatinByMonopatin_id(monopatinId);
    }

    public MonopatinDTO getMonopatinById(Long id){
        return monopatinRepository.getMonopatinByMonopatin_id(id);
    }

    public void actualizarEstado(Long id, EstadoMonopatin estado) {

        Monopatin mono = monopatinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monopatín no encontrado"));

        mono.setEstado(estado);
        monopatinRepository.save(mono);
    }


}
