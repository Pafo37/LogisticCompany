package com.logisticcompany.api;

import com.logisticcompany.data.dto.RevenueResponseDTO;
import com.logisticcompany.service.shipment.ShipmentService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/revenue")
@AllArgsConstructor
public class RevenueApiController {

    private final ShipmentService shipmentService;

    @GetMapping
    @PreAuthorize("hasRole('OFFICE_EMPLOYEE')")
    public ResponseEntity<RevenueResponseDTO> getRevenue(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        if (end.isBefore(start)) {
            return ResponseEntity.badRequest().build();
        }

        BigDecimal totalRevenue = shipmentService.calculateTotalRevenue(start, end);

        RevenueResponseDTO response = new RevenueResponseDTO();
        response.setStartDate(start);
        response.setEndDate(end);
        response.setTotalRevenue(totalRevenue);

        return ResponseEntity.ok(response);
    }
}