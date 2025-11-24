package com.repository;

import com.dto.*;
import com.dto.ReporteProjection;
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
            // Extraemos el año desde start_date
            "{ $addFields: { anio: { $year: '$start_date' } } }",

            // Filtramos por el año que llega como parámetro
            "{ $match: { anio: ?0 } }",

            // Agrupamos por monopatín y contamos los viajes
            "{ $group: { _id: '$id_scooter', totalRides: { $sum: 1 } } }",

            // Aplicamos condición tipo HAVING
            "{ $match: { totalRides: { $gte: ?1 } } }",

            // Proyectamos resultado limpio
            "{ $project: { _id: 0, idScooter: '$_id'} }"
    })
    List<RideCountResult> findMonopatinesConMasViajesEnAnio(int anio, int x);

    // ===== REPORTE SIN PAUSAS =====
    @Aggregation(pipeline = {
            "{ $match: { start_date: { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { " +
                    "_id: \"$id_scooter\", " +
                    "sumaKilometros: { $sum: \"$kilometers\" } " +
                    "} }",
            "{ $project: { " +
                    "idmonopatin: \"$_id\", " +
                    "kilometros: \"$sumaKilometros\", " +
                    "pausa: { $literal: null }, " +
                    "_id: 0 " +
                    "} }"
    })
    List<ReporteDTO> obtenerReporteSinPausa(Date fechaInicio, Date fechaFin);


    // ===== REPORTE CON PAUSAS =====
    @Aggregation(pipeline = {
            "{ $match: { start_date: { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { " +
                    "_id: \"$id_scooter\", " +
                    "sumaKilometros: { $sum: \"$kilometers\" }, " +
                    "tiempoPausa: { $first: \"$break_time\" } " +
                    "} }",
            "{ $project: { " +
                    "idmonopatin: \"$_id\", " +
                    "kilometros: \"$sumaKilometros\", " +
                    "pausa: \"$tiempoPausa\", " +
                    "_id: 0 " +
                    "} }"
    })
    List<ReporteDTO> obtenerReporteConPausa(Date fechaInicio, Date fechaFin);



    //devuelve todos los usuarios con la cantidad de viajes realizados en un rango de fechas(inciso e)
    @Aggregation(pipeline = {
            "{ $match: { start_date: { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { _id: '$id_user', cantidadViajes: { $sum: 1 } } }",
            "{ $project: { _id: 0, id: '$_id', cantidadViajes: 1 } }"
    })
    List<UsuarioViajeCountDTO> contarViajesPorUsuario(Date desde, Date hasta);


    List<Ride> findByStartDateBetween(Date start, Date end);


}
