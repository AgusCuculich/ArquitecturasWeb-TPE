package com.repository;

import com.entity.Fee;
import com.entity.Ride;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FeeRepository extends MongoRepository<Fee, String> {

    @Query("""
        {
          $and: [
            { "start_date": { $lte: ?1 } },
            { 
              $or: [
                { "end_date": { $gte: ?0 } },
                { "end_date": null }
              ]
            }
          ]
        }
        """)
    Fee findFeeForRange(Date start, Date end);

}
