package com.repository;

import com.dto.ReporteProjection;
import com.dto.ReporteProjection;
import com.dto.UsuarioViajeCountDTO;
import com.entity.Ride;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface RideRepository extends MongoRepository<Ride,String> {

    long countByIdUserAndStartDateBetween(Long idUser, Date start, Date end);


    @Aggregation(pipeline = {
            "{ '$match': { 'idUser': { $in: ?0 }, 'startDate': { $gte: ?1, $lte: ?2 } } }",
            "{ '$group': { '_id': '$idUser' } }"
    })
    List<Long> findDistinctUserIdsWithRides(List<Long> userIds, Date startDate, Date endDate);


    // SIN pausas
    @Aggregation(pipeline = {
            "{ $match: { start_date: { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { _id: \"$id_scooter\", totalKilometros: { $sum: \"$kilometers\" } } }",
            "{ $project: { idScooter: \"$_id\", totalKilometros: 1, _id: 0 } }"
    })
    List<ReporteProjection> obtenerReporteSinPausa(Date fechaInicio, Date fechaFin);


    // CON pausas
    @Aggregation(pipeline = {
            "{ $match: { start_date: { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { _id: \"$id_scooter\", totalKilometros: { $sum: \"$kilometers\" }, pausas: { $push: \"$break_time\" } } }",
            "{ $project: { idScooter: \"$_id\", totalKilometros: 1, pausas: 1, _id: 0 } }"
    })
    List<ReporteProjection> obtenerReporteConPausa(Date fechaInicio, Date fechaFin);


    //devuelve todos los usuarios con la cantidad de viajes realizados en un rango de fechas(inciso e)
    @Aggregation(pipeline = {
            "{ $match: { start_date: { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { _id: '$id_user', cantidadViajes: { $sum: 1 } } }",
            "{ $project: { _id: 0, idUsuario: '$_id', cantidadViajes: 1 } }"
    })
    List<UsuarioViajeCountDTO> contarViajesPorUsuario(Date desde, Date hasta);



}
