package com.repository;

import com.entity.Fee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FeeRepository extends MongoRepository<Fee,String> {

    @Query("{ 'startDate': { '$lte': ?1 }, 'endDate': { '$gte': ?0 } }")
    List<Fee> totalFacturado(Date inicio, Date fin);

}
