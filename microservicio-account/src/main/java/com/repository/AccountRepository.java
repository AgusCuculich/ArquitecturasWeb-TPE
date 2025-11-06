package com.repository;

import com.dto.AccountDTO;
import com.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    @Query("SELECT new com.dto.AccountDTO(a.tipo, a.fecha_alta, a.saldo) FROM Account a")
    List<AccountDTO> getAll();

    @Query("SELECT new com.dto.AccountDTO(a.tipo, a.fecha_alta, a.saldo) FROM Account a WHERE a.id = :id")
    Optional<AccountDTO> fetchById(@Param("id") Long id);
}
