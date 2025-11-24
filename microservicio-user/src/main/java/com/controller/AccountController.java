package com.controller;

import com.dto.AccountDTO;
import com.entity.Account;
import com.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;

    @GetMapping("/debug")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<Account> getAllDEBUG(){
        return service.getAllDebug();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<AccountDTO> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public Optional<AccountDTO> getById(@PathVariable("id") Long id){
        return service.fetchById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public void save (@RequestBody AccountDTO account){
        service.save(account);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void delete(@PathVariable("id") Long id){
        service.delete(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void update(@PathVariable("id") Long id, @RequestBody AccountDTO account){
        service.update(id,account);
    }
}
