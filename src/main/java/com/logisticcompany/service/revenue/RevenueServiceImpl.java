package com.logisticcompany.service.revenue;

import com.logisticcompany.service.shipment.ShipmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class RevenueServiceImpl implements RevenueService{

    private final ShipmentService  shipmentService;

    @Override
    public BigDecimal calculateRevenue(LocalDate startDate, LocalDate endDate) {
        return shipmentService.calculateTotalRevenue(startDate, endDate);
    }
}
