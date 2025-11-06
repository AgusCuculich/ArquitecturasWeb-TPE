package com.service;

import com.dto.AccountDTO;
import com.entity.Account;
import com.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository repo;

    public void save(Account account){
        repo.save(account);
    }


    public void delete(Long id){
        repo.deleteById(id);
    }

    public List<AccountDTO> getAll() {
        return repo.getAll();
    }

    public Optional<AccountDTO> fetchById(Long id) {
        return repo.fetchById(id);
    }
}
