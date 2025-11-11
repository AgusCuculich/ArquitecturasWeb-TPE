package com.controller;

import com.dto.UserDTO;
import com.dto.UsuarioViajeCountDTO;
import com.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.service.UserService;

import java.time.LocalDateTime;
import java.util.Date;
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

    @GetMapping("/ranking")
    public List<UsuarioViajeCountDTO> rankingPorPeriodoYRol(
            @RequestParam("desde") @DateTimeFormat(pattern = "yyyy-MM-dd") Date desde,
            @RequestParam("hasta") @DateTimeFormat(pattern = "yyyy-MM-dd") Date hasta,
            @RequestParam("rol") String rol
    ) {
        return service.rankingPorPeriodoYRol(desde, hasta, rol);
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
