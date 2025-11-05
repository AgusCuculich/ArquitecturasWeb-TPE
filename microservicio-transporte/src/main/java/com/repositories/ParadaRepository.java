package com.repositories;

import com.dtos.ParadaDTO;
import com.entities.Parada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParadaRepository extends JpaRepository<Parada,Long> {

    @Query("SELECT new com.dtos.ParadaDTO(p.GPS) FROM Parada p WHERE p.parada_id = :parada_id")
    ParadaDTO findByParada_id(@Param("parada_id") Long parada_id);

    @Query("SELECT new com.dtos.ParadaDTO(p.GPS) FROM Parada p")
    List<ParadaDTO>getAllParadas();
}
