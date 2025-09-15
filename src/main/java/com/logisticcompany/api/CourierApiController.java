package com.logisticcompany.api;

import com.logisticcompany.data.dto.CourierDTO;
import com.logisticcompany.service.courier.CourierService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/couriers")
public class CourierApiController {

    private final CourierService courierService;

    @GetMapping
    public List<CourierDTO> getAllCouriers() {
        return courierService.getAll();
    }

    @GetMapping("/{id}")
    public CourierDTO getCourier(@PathVariable Long id) {
        return courierService.getById(id);
    }
}

