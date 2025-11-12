package com.dto;

import java.time.LocalTime;
import java.util.List;

public interface ReporteProjection {
    Long getIdScooter();
    Double getTotalKilometros();
    List<LocalTime> getPausas();
}
