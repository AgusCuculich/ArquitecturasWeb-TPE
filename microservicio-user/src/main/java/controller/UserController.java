package controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    
}
