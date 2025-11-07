package com.entity;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Document
public class Ride {
    @Id
    private Long id;

    @Field("start_date")
    private Date startDate;

    @Field("end_date")
    @Nullable
    private Date endDate;

    @Field("break_time")
    private LocalTime breakTime;

    @Field
    private Double kilometers;

    @Field("id_user")
    private Long idUser;

    @Field("id_scooter")
    private Long idScooter;
}
