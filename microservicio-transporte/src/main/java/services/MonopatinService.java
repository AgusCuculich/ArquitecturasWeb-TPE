package services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.MonopatinRepository;

@AllArgsConstructor
@Service
public class MonopatinService {

    @Autowired
    private final MonopatinRepository monopatinRepository;
}
