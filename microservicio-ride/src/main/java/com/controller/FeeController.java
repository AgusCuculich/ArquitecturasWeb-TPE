package com.controller;

import com.dto.FeeDTO;
import com.dto.RideDTO;
import com.entity.Fee;
import com.entity.Ride;
import com.service.FeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fees")
public class FeeController {
    private final FeeService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FeeDTO> getAllFees(){
        return service.getAllFees();
    }

    @GetMapping("/debug")
    @ResponseStatus(HttpStatus.OK)
    public List<Fee> getAllFeesDebug(){
        return service.getAllFeesDebug();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<FeeDTO> getFee(@PathVariable("id") String id){
        return service.getFee(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveFee(@RequestBody FeeDTO fee){
        service.saveFee(fee);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateFee(@PathVariable("id") String id, @RequestBody FeeDTO updatedFee){
        service.updateFee(id,updatedFee);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFee(@PathVariable("id") String id){
        service.deleteRide(id);
    }


    @GetMapping("/facturacion")
    public double getTotalFees(
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {
        return service.getTotalFees(start, end);
    }

}
