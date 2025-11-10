package com.repository;

import com.dto.RideCountResult;
import com.entity.Ride;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends MongoRepository<Ride,String> {

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
}
