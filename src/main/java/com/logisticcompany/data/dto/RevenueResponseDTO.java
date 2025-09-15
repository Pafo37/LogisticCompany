package com.logisticcompany.data.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RevenueResponseDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalRevenue;
}