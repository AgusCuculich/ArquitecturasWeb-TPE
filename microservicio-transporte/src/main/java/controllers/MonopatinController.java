package controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import services.MonopatinService;

@RestController
@RequiredArgsConstructor

public class MonopatinController {

    @Autowired
    private final MonopatinService monopatinService;

}
