package com.controller;

import com.dto.RideDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.service.RideService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rides")
public class RideController {
    private final RideService service;

    @GetMapping
    public List<RideDTO> getAllRides() {
        return service.getAllRides();
    }
}
