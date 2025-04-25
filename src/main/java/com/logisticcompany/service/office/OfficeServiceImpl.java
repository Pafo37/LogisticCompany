package com.logisticcompany.service.office;

import com.logisticcompany.data.dto.OfficeDTO;
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
    public List<OfficeDTO> getAllOffices() {
        return officeRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public OfficeDTO getOfficeById(Long id) {
        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Office not found"));
        return mapToDTO(office);
    }

    @Override
    public OfficeDTO saveOffice(OfficeDTO officeDTO) {
        Office office = mapToEntity(officeDTO);
        Office savedOffice = officeRepository.save(office);
        return mapToDTO(savedOffice);
    }

    @Override
    public void deleteOffice(Long id) {
        officeRepository.deleteById(id);
    }

    private OfficeDTO mapToDTO(Office office) {
        OfficeDTO dto = new OfficeDTO();
        dto.setId(office.getId());
        dto.setName(office.getName());
        dto.setAddress(office.getAddress());
        return dto;
    }

    private Office mapToEntity(OfficeDTO dto) {
        Office office = new Office();
        office.setName(dto.getName());
        office.setAddress(dto.getAddress());
        return office;
    }
}