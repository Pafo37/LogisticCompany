package com.logisticcompany.service.office;

import com.logisticcompany.data.dto.CreateOfficeDTO;
import com.logisticcompany.data.dto.OfficeDTO;
import com.logisticcompany.data.entity.Office;

import java.util.List;

public interface OfficeService {

    List<OfficeDTO> getAllOffices();

    OfficeDTO getOfficeById(Long id);

    OfficeDTO saveOffice(OfficeDTO office);

    OfficeDTO createOffice(CreateOfficeDTO dto);

    OfficeDTO updateOffice(Long id, CreateOfficeDTO dto);

    OfficeDTO updateOffice(Long id, OfficeDTO officeDTO);

    void deleteOffice(Long id);

    Office findEntityById(Long id);

}
