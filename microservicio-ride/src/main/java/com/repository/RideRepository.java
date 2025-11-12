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

}
