package com.services;

import com.client.RideClient;
import com.dtos.MonopatinDTO;
import com.dtos.ReporteMonopatinesDTO;
import com.entities.Monopatin;
import com.utils.EstadoMonopatin;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.repositories.MonopatinRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class MonopatinService {

    private final MonopatinRepository monopatinRepository;

    private final RideClient rideClient;

    public List<ReporteMonopatinesDTO> generarReporteMonopatines(Date inicio, Date fin, boolean incluirPausas) {

        // 1) Llamamos al servicio de rides
        List<ReporteMonopatinesDTO> reporteRides = rideClient.obtenerReporte(
                // Puedes usar SimpleDateFormat si hace falta
                inicio,
                fin,
                incluirPausas
        );

        // 2) Traemos los monopatines que ya están en mantenimiento
        List<Long> idsEnMantenimiento = monopatinRepository.findIdsEnMantenimiento();

        // 3) Filtramos para excluir esos monopatines
        List<ReporteMonopatinesDTO> filtrado = reporteRides.stream()
                .filter(r -> !idsEnMantenimiento.contains(r.getIdMonopatin()))
                .toList();

        // 4) Convertimos a ReporteMonopatinesDTO
        return reporteRides.stream()
                .filter(r -> !idsEnMantenimiento.contains(r.getIdMonopatin()))
                .map(r -> {
                    ReporteMonopatinesDTO dto = new ReporteMonopatinesDTO();
                    dto.setIdMonopatin(r.getIdMonopatin());
                    dto.setKilometrosRecorridos(r.getKilometrosRecorridos());
                    dto.setRequiereMantenimiento(r.getKilometrosRecorridos()>= 100 ? "SI" : "NO");
                    dto.setTiempoPausa(incluirPausas ? r.getTiempoPausa(): null);
                    return dto;
                })
                .toList();
    }

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

    public List<MonopatinDTO> getScooterStats(int anio, int viajes) {
        List<Long> ids = rideClient.getScooterStats(anio, viajes);
        List<Monopatin> scooterFiltrados = monopatinRepository.findAllById(ids);

        List<MonopatinDTO> monopatinesDTO =  new ArrayList<>();
        for(Monopatin monopatin : scooterFiltrados){
            MonopatinDTO dto = new MonopatinDTO(monopatin.getParada_id(), monopatin.getEstado());
            monopatinesDTO.add(dto);
        }

        return monopatinesDTO;
    }

}
