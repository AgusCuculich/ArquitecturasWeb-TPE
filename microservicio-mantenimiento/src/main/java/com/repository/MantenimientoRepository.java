package com.repository;

import com.dto.MantenimientoDTO;
import com.entity.Mantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long> {

    @Query("SELECT new com.dto.MantenimientoDTO(m.fecha_inicio, m.fecha_fin, m.monopatin_id,m.encargado_id) FROM Mantenimiento m")
    public List<MantenimientoDTO> getAllMantenimientos();
}
