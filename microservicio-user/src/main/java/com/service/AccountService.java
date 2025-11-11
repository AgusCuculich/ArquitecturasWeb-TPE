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

    public void save(AccountDTO dto){
        Account account = new Account();
        account.setTipo(dto.getTipo());
        account.setFecha_alta(dto.getFecha_alta());
        account.setSaldo(dto.getSaldo());

        repo.save(account);
    }

    public void delete(Long id){
        repo.deleteById(id);
    }



    public List<Account> getAllDebug() {
        return repo.findAll();
    }

    public List<AccountDTO> getAll() {
        return repo.getAll();
    }

    public Optional<AccountDTO> fetchById(Long id) {
        return repo.fetchById(id);
    }

    public void update(Long id, AccountDTO updatedAccount) {
        Account existingAccount = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe la cuenta con el id: " + id));

        if(updatedAccount.getTipo() != null) existingAccount.setTipo(updatedAccount.getTipo());
        if(updatedAccount.getFecha_alta() != null) existingAccount.setFecha_alta(updatedAccount.getFecha_alta());
        if(updatedAccount.getSaldo() != 0) existingAccount.setSaldo(updatedAccount.getSaldo());

        repo.save(existingAccount);
    }
}
