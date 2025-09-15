package com.logisticcompany.controller;

import com.logisticcompany.service.revenue.RevenueService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/revenue")
@AllArgsConstructor
public class RevenueController {

    private final RevenueService revenueService;

    @GetMapping
    public Map<String, BigDecimal> getRevenue(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        BigDecimal revenue = revenueService.calculateRevenue(from, to);
        return Map.of("totalRevenue", revenue);
    }
}