package com.controller;

import com.dto.UserDTO;
import com.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;


    @GetMapping
    public List<UserDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/debug")
    public List<User> getAllDEBUG() {
        return service.getAllDebug();
    }

    @GetMapping("/{id}")
    public Optional<UserDTO> getById(@PathVariable("id") Long id){
        return service.fetchById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void save(@RequestBody UserDTO user) {
        service.save(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        service.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") Long id, @RequestBody UserDTO usuarioActualizado){
        service.update(id,usuarioActualizado);
    }

    @GetMapping("/accounts/{userId}")
    public List<Long> getOtherUsers(@PathVariable("userId") Long userId) {
        return service.getOtherUsers(userId);
    }

    @PostMapping("/{userId}/linkAccount/{accountId}")
    public void linkAccount(
            @PathVariable("userId") Long userId,
            @PathVariable("accountId") Long accountId) {
        service.linkAccount(userId, accountId);
    }



}
