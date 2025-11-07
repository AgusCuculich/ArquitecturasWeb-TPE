package com.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FeeDTO {
    private Double price;
    private Date startDate;
    private Date endDate;
}
