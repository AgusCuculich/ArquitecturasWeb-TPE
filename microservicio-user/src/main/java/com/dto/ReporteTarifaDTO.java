package com.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class ReporteTarifaDTO {
    private Double price;
    private Date startDate;
    private Date endDate;
}
