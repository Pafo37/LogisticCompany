package com.logisticcompany.service;

import com.logisticcompany.data.dto.CreateOfficeDTO;
import com.logisticcompany.data.dto.OfficeDTO;
import com.logisticcompany.data.entity.Office;
import com.logisticcompany.repository.OfficeRepository;
import com.logisticcompany.service.office.OfficeServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfficeServiceImplTest {

    @Mock
    OfficeRepository officeRepository;

    @InjectMocks
    OfficeServiceImpl service;

    private static Office office(Long id, String name, String address) {
        Office office = new Office();
        office.setId(id);
        office.setName(name);
        office.setAddress(address);
        return office;
    }

    @Test
    void testGetOffices() {
        Office office1 = office(1L, "Ekont", "ul.Ekont 45");
        Office office2 = office(2L, "Speedy", "ul.Speedy 45");
        when(officeRepository.findAll()).thenReturn(List.of(office1, office2));

        List<OfficeDTO> offices = service.getAllOffices();

        assertThat(offices).hasSize(2);
        assertThat(offices.get(0).getId()).isEqualTo(1L);
        assertThat(offices.get(0).getName()).isEqualTo("Ekont");
        assertThat(offices.get(1).getId()).isEqualTo(2L);
        assertThat(offices.get(1).getName()).isEqualTo("Speedy");
    }

    @Test
    void testSaveOffice() {
        OfficeDTO officeDTO = new OfficeDTO();
        officeDTO.setName("Ekont");
        officeDTO.setAddress("ul.Ekont 45");

        Office officeEntity = office(7L, "Ekont", "ul.Ekont 45");

        ArgumentCaptor<Office> argumentCaptor = ArgumentCaptor.forClass(Office.class);
        when(officeRepository.save(argumentCaptor.capture())).thenReturn(officeEntity);

        OfficeDTO result = service.saveOffice(officeDTO);

        Office savedOffice = argumentCaptor.getValue();
        assertThat(savedOffice.getName()).isEqualTo("Ekont");
        assertThat(savedOffice.getAddress()).isEqualTo("ul.Ekont 45");

        assertThat(result.getId()).isEqualTo(7L);
        assertThat(result.getName()).isEqualTo("Ekont");
        assertThat(result.getAddress()).isEqualTo("ul.Ekont 45");
    }

    @Test
    void testCreateOffice() {
        CreateOfficeDTO createOfficeDTO = new CreateOfficeDTO();
        createOfficeDTO.setName("Ekont");
        createOfficeDTO.setAddress("ul.Ekont 45");

        ArgumentCaptor<Office> argumentCaptor = ArgumentCaptor.forClass(Office.class);
        Office saved = office(3L, "Ekont", "ul.Ekont 45");
        when(officeRepository.save(argumentCaptor.capture())).thenReturn(saved);

        OfficeDTO result = service.createOffice(createOfficeDTO);

        Office toSave = argumentCaptor.getValue();
        assertThat(toSave.getName()).isEqualTo("Ekont");
        assertThat(toSave.getAddress()).isEqualTo("ul.Ekont 45");

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("Ekont");
    }

    @Test
    void testUpdateOfficeSuccess() {
        Office currentOffice = office(11L, "Ekont", "ul.Ekont 45");
        when(officeRepository.findById(11L)).thenReturn(Optional.of(currentOffice));

        OfficeDTO officeDTO = new OfficeDTO();
        officeDTO.setId(11L);
        officeDTO.setName("Speedy");
        officeDTO.setAddress("ul.Speedy 45");

        ArgumentCaptor<Office> argumentCaptor = ArgumentCaptor.forClass(Office.class);
        when(officeRepository.save(argumentCaptor.capture())).thenAnswer(inv -> inv.getArgument(0));

        OfficeDTO updatedOffice = service.updateOffice(11L, officeDTO);

        Office savedOffice = argumentCaptor.getValue();
        assertThat(savedOffice.getId()).isEqualTo(11L);
        assertThat(savedOffice.getName()).isEqualTo("Speedy");
        assertThat(savedOffice.getAddress()).isEqualTo("ul.Speedy 45");
        assertThat(updatedOffice.getName()).isEqualTo("Speedy");
    }

    @Test
    void testUpdateOfficeWithMissingId() {
        when(officeRepository.findById(77L)).thenReturn(Optional.empty());

        CreateOfficeDTO createOfficeDTO = new CreateOfficeDTO();
        createOfficeDTO.setName("Ekont");
        createOfficeDTO.setAddress("ul.Ekont 45");

        assertThatThrownBy(() -> service.updateOffice(77L, createOfficeDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Office not found");
    }

    @Test
    void testDeleteOfficeById() {
        service.deleteOffice(5L);
        verify(officeRepository).deleteById(5L);
    }

    @Test
    void testFindOfficeById() {
        Office office = office(9L, "Dragan", "ul.Dragan 27");
        when(officeRepository.findById(9L)).thenReturn(Optional.of(office));

        Office result = service.findEntityById(9L);

        assertThat(result.getId()).isEqualTo(9L);
        assertThat(result.getName()).isEqualTo("Dragan");
    }

    @Test
    void testNoOfficeFound() {
        when(officeRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findEntityById(404L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Office not found for ID: 404");
    }
}
