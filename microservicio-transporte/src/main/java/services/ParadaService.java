package services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.ParadaRepository;

@Service
@AllArgsConstructor
public class ParadaService {

    @Autowired
    private final ParadaRepository paradaRepository;
}
