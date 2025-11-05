package com.controller;

import com.dto.UserDTO;
import com.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public List<UserDTO> findAll() {
        return service.findAll();
    }

    @PostMapping
    public void save(@RequestBody User user) {
        service.save(user);
    }
}
