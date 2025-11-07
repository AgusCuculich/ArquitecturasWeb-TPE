package com.entity;

import com.mongodb.lang.Nullable;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Fee {
    @Id
    private String id;

    @Field
    private Double price;

    @Field("start_date")
    private Date startDate;

    @Field("end_date")
    @Nullable
    private Date endDate;
}
