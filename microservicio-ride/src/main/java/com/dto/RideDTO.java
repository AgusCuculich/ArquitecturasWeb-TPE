package com.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalTime;
import java.util.Date;

@ToString
@Getter
@AllArgsConstructor
public class RideDTO {
    private Date startDate;
    private Date endDate;
    private LocalTime breakTime;
    private Double kilometers;
    private Long idUser;
    private Long idScooter;
}
