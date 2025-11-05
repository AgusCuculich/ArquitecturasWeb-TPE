package com.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.services.ParadaService;

@RestController
@RequiredArgsConstructor

public class ParadaController {

    @Autowired
    private final ParadaService paradaService;

}
