package com.repositories;

import com.dtos.MonopatinDTO;
import com.entities.Monopatin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface MonopatinRepository extends JpaRepository<Monopatin,Long> {

    @Query("SELECT new com.dtos.MonopatinDTO(m.parada_id,m.estado)FROM Monopatin m WHERE m.monopatin_id = :monopatin_id")
    MonopatinDTO getMonopatinByMonopatin_id(@Param ("monopatin_id") long id);
}
