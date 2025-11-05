package com.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.repositories.MonopatinRepository;

@AllArgsConstructor
@Service
public class MonopatinService {

    @Autowired
    private final MonopatinRepository monopatinRepository;
}
