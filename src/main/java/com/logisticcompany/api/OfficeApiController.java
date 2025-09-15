package com.logisticcompany.api;

import com.logisticcompany.data.dto.CreateOfficeDTO;
import com.logisticcompany.data.dto.OfficeDTO;
import com.logisticcompany.service.office.OfficeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/offices")
public class OfficeApiController {

    private final OfficeService officeService;

    @GetMapping
    public List<OfficeDTO> getAllOffices() {
        return officeService.getAllOffices();
    }

    @GetMapping("/{id}")
    public OfficeDTO getOfficeById(@PathVariable Long id) {
        return officeService.getOfficeById(id);
    }

    @PostMapping
    public OfficeDTO createOffice(@RequestBody CreateOfficeDTO officeDTO) {
        return officeService.createOffice(officeDTO);
    }

    @PutMapping("/{id}")
    public OfficeDTO updateOffice(@PathVariable Long id, @RequestBody CreateOfficeDTO officeDTO) {
        return officeService.updateOffice(id, officeDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteOffice(@PathVariable Long id) {
        officeService.deleteOffice(id);
    }
}