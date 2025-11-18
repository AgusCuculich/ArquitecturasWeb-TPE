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

    public List<Monopatin> getAllMonopatinesDebug() {
        return monopatinRepository.findAll();
    }

    public List<ReporteMonopatinesDTO> generarReporteMonopatines(Date inicio, Date fin, boolean incluirPausas) {

        // 1) Llamamos al servicio de rides
        List<ReporteMonopatinesDTO> reporteRides = rideClient.obtenerReporte(
                // Puedes usar SimpleDateFormat si hace falta
                inicio,
                fin,
                incluirPausas
        );
        System.out.println("reporteRides de mongo"+reporteRides);

        // 2) Traemos los monopatines que ya están en mantenimiento
        List<Long> idsEnMantenimiento = monopatinRepository.findIdsEnMantenimiento();
        System.out.println("ides monopatinMantenimiiento"+idsEnMantenimiento);


        // 3) Filtramos para excluir esos monopatines
        List<ReporteMonopatinesDTO> filtrado = reporteRides.stream()
                .filter(r -> !idsEnMantenimiento.contains(r.getIdmonopatin()))
                .toList();
        System.out.println("filtrados"+filtrado);

        // 4) Convertimos a ReporteMonopatinesDTO
        return reporteRides.stream()
                .filter(r -> !idsEnMantenimiento.contains(r.getIdmonopatin()))
                .map(r -> {
                    ReporteMonopatinesDTO dto = new ReporteMonopatinesDTO();
                    dto.setIdmonopatin(r.getIdmonopatin());
                    dto.setKilometros(r.getKilometros());
                    dto.setRequiereMantenimiento(r.getKilometros()>= 100 ? "SI" : "NO");
                    dto.setPausa(incluirPausas ? r.getPausa(): null);
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

    public List<MonopatinDTO> getAllMonopatines(){
        return monopatinRepository.getAllMonopatines();
    }

    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        // Método para calcular la distancia entre dos puntos (Fórmula de Haversine)
        final int R = 6371000; // Radio de la tierra en metros

        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // distancia en metros
    }


    public List<MonopatinDTO> getAllNearbyScooters(double latitud, double longitud, double radioMetros) {
        List<Monopatin> monopatinesDisponibles = monopatinRepository.getAllAvailableScooters();
    List<MonopatinDTO> resultado = new ArrayList<>();
        for (Monopatin monopatin : monopatinesDisponibles) {
            double distancia = calcularDistancia(monopatin.getLatitud(), monopatin.getLongitud(), latitud, longitud);
            if(distancia <= radioMetros) {
                resultado.add(new MonopatinDTO(monopatin.getParada_id(), monopatin.getEstado()));
            }
        }
        return resultado;
    }
}
