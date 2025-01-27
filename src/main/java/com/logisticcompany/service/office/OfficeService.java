package com.logisticcompany.service.office;

import com.logisticcompany.data.entity.Office;

import java.util.List;

public interface OfficeService {

    List<Office> getAllOffices();

    Office getOfficeById(Long id);

    Office saveOffice(Office office);

    void deleteOffice(Long id);

    List<Office> searchOfficesByName(String name);

    List<Office> searchOfficesByAddress(String address);

}
