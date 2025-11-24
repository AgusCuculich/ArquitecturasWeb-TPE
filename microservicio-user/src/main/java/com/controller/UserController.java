package com.controller;

import com.dto.ReporteTarifaDTO;
import com.dto.UserDTO;
import com.dto.UsuarioViajeCountDTO;
import com.entity.User;
import com.utils.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.service.UserService;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<UserDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/debug")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<User> getAllDEBUG() {
        return service.getAllDebug();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public UserDTO getById(@PathVariable("id") Long id) {
        return service.fetchById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping("/ranking")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<UsuarioViajeCountDTO> rankingPorPeriodoYRol(
            @RequestParam("desde") @DateTimeFormat(pattern = "yyyy-MM-dd") Date desde,
            @RequestParam("hasta") @DateTimeFormat(pattern = "yyyy-MM-dd") Date hasta,
            @RequestParam("rol") String rol
    ) {
        return service.rankingPorPeriodoYRol(desde, hasta, rol);
    }

    @GetMapping("/reporte-tarifas")
    @PreAuthorize("hasRole('ADMINISTRADOR','MANTENIMIENTO')")
    @ResponseStatus(HttpStatus.OK)
    public ReporteTarifaDTO getReporteTarifas(
            @RequestParam("desde") @DateTimeFormat(pattern = "yyyy-MM-dd") Date desde,
            @RequestParam("hasta") @DateTimeFormat(pattern = "yyyy-MM-dd") Date hasta
    ) {
        // Llama al nuevo método del servicio
        return service.getReporteTarifas(desde, hasta);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void save(@RequestBody UserDTO user) {
        service.save(user);

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void delete(@PathVariable("id") Long id){
        service.delete(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void update(@PathVariable("id") Long id, @RequestBody UserDTO usuarioActualizado){
        service.update(id,usuarioActualizado);
    }

    @PutMapping("/disable/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void disableUser(@PathVariable("id") Long id) {service.disableUser(id);}

    @GetMapping("/accounts/{userId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public List<Long> getOtherUsers(@PathVariable("userId") Long userId) {
        return service.getOtherUsers(userId);
    }

    @PostMapping("/{userId}/linkAccount/{accountId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public void linkAccount(
            @PathVariable("userId") Long userId,
            @PathVariable("accountId") Long accountId) {
        service.linkAccount(userId, accountId);
    }



}
