package com.service;

import com.dto.FeeDTO;
import com.entity.Fee;
import com.dto.RideDTO;
import com.repository.FeeRepository;
import com.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeeService {
    private final FeeRepository repo;


    public List<Fee> getAllFeesDebug(){
        return repo.findAll();
    }

    public List<FeeDTO> getAllFees() {
        return repo.findAll()
                .stream()
                .map(fee -> new FeeDTO(
                        fee.getPrice(),
                        fee.getStartDate(),
                        fee.getEndDate()
                ))
                .collect(Collectors.toList());
    }

    public Optional<FeeDTO> getFee(String id){
        return repo.findById(id)
                .map(fee -> new FeeDTO(
                        fee.getPrice(),
                        fee.getStartDate(),
                        fee.getEndDate()
                ));
    }

    public void saveFee(FeeDTO dto){
        Fee fee = new Fee();

        fee.setPrice(dto.getPrice());
        fee.setStartDate(dto.getStartDate());
        fee.setEndDate(dto.getEndDate());

        repo.save(fee);

    }

    public void updateFee(String id, FeeDTO updatedFee) {
        Fee existingFee = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe la tarifa con el id: " + id));

        if (updatedFee.getPrice() != null) existingFee.setPrice(updatedFee.getPrice());
        if (updatedFee.getStartDate() != null) existingFee.setStartDate(updatedFee.getStartDate());
        if (updatedFee.getEndDate() != null) existingFee.setEndDate(updatedFee.getEndDate());

        repo.save(existingFee);
    }

    public void deleteRide(String id) {repo.deleteById(id);}
}
