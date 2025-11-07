package com.service;

import com.dto.RideDTO;
import lombok.RequiredArgsConstructor;
import com.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RideService {
    private final RideRepository repo;

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
}
