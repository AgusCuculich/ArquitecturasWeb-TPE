package com.repository;

import com.entity.Ride;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

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

}
