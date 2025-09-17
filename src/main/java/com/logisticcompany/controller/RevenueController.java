package com.logisticcompany.controller;

import com.logisticcompany.service.revenue.RevenueService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/revenue")
@AllArgsConstructor
public class RevenueController {

    private final RevenueService revenueService;

    @GetMapping
    public String showForm(
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model
    ) {
        if (from == null || to == null) {
            return "revenue";
        }

        if (to.isBefore(from)) {
            model.addAttribute("error", "'To' date cannot be before 'From' date.");
            model.addAttribute("from", from);
            model.addAttribute("to", to);
            return "revenue";
        }

        var total = revenueService.calculateRevenue(from, to);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("totalRevenue", total);
        return "revenue";
    }

    @PostMapping
    public String calculate(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model
    ) {
        if (to.isBefore(from)) {
            model.addAttribute("error", "'To' date cannot be before 'From' date.");
            model.addAttribute("from", from);
            model.addAttribute("to", to);
            return "revenue";
        }

        var total = revenueService.calculateRevenue(from, to);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("totalRevenue", total);
        return "revenue";
    }
}