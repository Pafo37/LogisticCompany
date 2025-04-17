package com.logisticcompany.service.revenue;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RevenueService {

    BigDecimal calculateRevenue(LocalDate startDate, LocalDate endDate);
}
