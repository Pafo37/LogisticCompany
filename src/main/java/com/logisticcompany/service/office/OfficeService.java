package com.logisticcompany.service.office;

import com.logisticcompany.data.dto.OfficeDTO;

import java.util.List;

public interface OfficeService {

    List<OfficeDTO> getAllOffices();

    OfficeDTO getOfficeById(Long id);

    OfficeDTO saveOffice(OfficeDTO office);

    OfficeDTO updateOffice(Long id, OfficeDTO officeDTO);

    void deleteOffice(Long id);

}
