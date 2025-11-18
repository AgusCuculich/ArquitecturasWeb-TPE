package com.repositories;

import com.dtos.MonopatinDTO;
import com.entities.Monopatin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface MonopatinRepository extends JpaRepository<Monopatin,Long> {

    @Query("SELECT new com.dtos.MonopatinDTO(m.parada_id,m.estado)FROM Monopatin m WHERE m.monopatin_id = :monopatin_id")
    MonopatinDTO getMonopatinByMonopatin_id(@Param ("monopatin_id") long id);

    @Query("SELECT m.monopatin_id FROM Monopatin m WHERE m.estado = com.utils.EstadoMonopatin.MANTENIMIENTO")
    List<Long> findIdsEnMantenimiento();

    @Query("SELECT new com.dtos.MonopatinDTO(m.parada_id,m.estado) FROM Monopatin m")
    List<MonopatinDTO> getAllMonopatines();

    @Query("SELECT m FROM Monopatin m " +
            "WHERE m.estado = 'DISPONIBLE'")
    List<Monopatin> getAllAvailableScooters();

}
