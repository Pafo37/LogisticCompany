package com.logisticcompany.api;

import com.logisticcompany.data.dto.OfficeDTO;
import com.logisticcompany.data.entity.Office;
import com.logisticcompany.service.office.OfficeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offices")
@AllArgsConstructor
public class OfficeRestController {

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
    public OfficeDTO createOffice(@RequestBody OfficeDTO office) {
        return officeService.saveOffice(office);
    }

    @PutMapping("/{id}")
    public OfficeDTO updateOffice(@PathVariable Long id, @RequestBody Office updatedOffice) {
        OfficeDTO existing = officeService.getOfficeById(id);
        existing.setName(updatedOffice.getName());
        existing.setAddress(updatedOffice.getAddress());
        return officeService.saveOffice(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteOffice(@PathVariable Long id) {
        officeService.deleteOffice(id);
    }
}