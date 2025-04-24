package com.logisticcompany.service.revenue;

import com.logisticcompany.data.repository.ShipmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RevenueServiceImpl implements RevenueService {

    private final ShipmentRepository shipmentRepository;

    @Override
    public BigDecimal calculateRevenue(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);
        return shipmentRepository.sumRevenueBetween(start, end);
    }
}
