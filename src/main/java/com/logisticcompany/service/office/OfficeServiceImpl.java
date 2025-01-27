package com.logisticcompany.service.office;

import com.logisticcompany.data.entity.Office;
import com.logisticcompany.data.repository.OfficeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OfficeServiceImpl implements OfficeService {

    private OfficeRepository officeRepository;

    @Override
    public List<Office> getAllOffices() {
        return officeRepository.findAll();
    }

    @Override
    public Office getOfficeById(Long id) {
        return officeRepository.findById(id).orElseThrow(() -> new RuntimeException("Office not found"));
    }

    @Override
    public Office saveOffice(Office office) {
        return officeRepository.save(office);
    }

    @Override
    public void deleteOffice(Long id) {
        officeRepository.deleteById(id);
    }

    @Override
    public List<Office> searchOfficesByName(String name) {
        return officeRepository.findByNameContaining(name);
    }

    @Override
    public List<Office> searchOfficesByAddress(String address) {
        return officeRepository.findByAddressContaining(address);
    }
}
