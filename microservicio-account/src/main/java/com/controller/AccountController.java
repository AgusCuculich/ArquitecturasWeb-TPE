package com.controller;

import com.dto.AccountDTO;
import com.entity.Account;
import com.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;


    @GetMapping
    public List<AccountDTO> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Optional<AccountDTO> getById(@PathVariable("id") Long id){
        return service.fetchById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void save (@RequestBody Account account){
        service.save(account);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        service.delete(id);
    }

}
