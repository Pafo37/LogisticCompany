package com.logisticcompany.service.office;

import com.logisticcompany.data.dto.OfficeDTO;

import java.util.List;

public interface OfficeService {

    List<OfficeDTO> getAllOffices();

    OfficeDTO getOfficeById(Long id);

    OfficeDTO saveOffice(OfficeDTO office);

    void deleteOffice(Long id);

}
