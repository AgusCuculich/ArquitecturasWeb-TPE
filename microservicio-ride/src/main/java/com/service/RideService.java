package com.service;

import com.dto.RideDTO;
import com.entity.Ride;
import lombok.RequiredArgsConstructor;
import com.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RideService {
    private final RideRepository repo;

    public List<Ride> getAllRidesDebug(){
        return repo.findAll();
    }

    public List<RideDTO> getAllRides() {
        return repo.findAll()
                .stream()
                .map(ride -> new RideDTO(
                        ride.getStartDate(),
                        ride.getEndDate(),
                        ride.getBreakTime(),
                        ride.getKilometers(),
                        ride.getIdUser(),
                        ride.getIdScooter()
                ))
                .collect(Collectors.toList());
    }

    public Optional<RideDTO> getRide(String id) {
        return repo.findById(id)
                .map(ride -> new RideDTO(
                        ride.getStartDate(),
                        ride.getEndDate(),
                        ride.getBreakTime(),
                        ride.getKilometers(),
                        ride.getIdUser(),
                        ride.getIdScooter()
                ));

    }

    public void saveRide(RideDTO dto) {
        Ride ride = new Ride();

        ride.setStartDate(dto.getStartDate());
        ride.setEndDate(dto.getEndDate());
        ride.setBreakTime(dto.getBreakTime());
        ride.setKilometers(dto.getKilometers());
        ride.setIdUser(dto.getIdUser());
        ride.setIdScooter(dto.getIdScooter());

        // Mongo genera automáticamente el id
        repo.save(ride);
    }


    public void deleteRide(String id) {repo.deleteById(id);}

    public void updateRide(String id, RideDTO updatedRide) {
        Ride existingRide = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el viaje con id: " + id));

        if(updatedRide.getStartDate() != null) existingRide.setStartDate(updatedRide.getStartDate());
        if(updatedRide.getEndDate() != null) existingRide.setEndDate(updatedRide.getEndDate());
        if(updatedRide.getBreakTime() != null) existingRide.setBreakTime(updatedRide.getBreakTime());
        if(updatedRide.getKilometers() != null) existingRide.setKilometers(updatedRide.getKilometers());
        if(updatedRide.getIdUser() != null) existingRide.setIdUser(updatedRide.getIdUser());
        if(updatedRide.getIdScooter() != null) existingRide.setIdScooter(updatedRide.getIdScooter());

        repo.save(existingRide);
    }
}
