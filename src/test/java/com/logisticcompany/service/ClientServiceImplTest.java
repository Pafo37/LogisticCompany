package com.logisticcompany.service;

import com.logisticcompany.data.dto.CourierDTO;
import com.logisticcompany.data.dto.RegistrationDTO;
import com.logisticcompany.data.entity.Courier;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.data.repository.CourierRepository;
import com.logisticcompany.service.courier.CourierServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    private CourierRepository courierRepository;

    @InjectMocks
    private CourierServiceImpl courierService;

    private Courier sampleCourier(Long id, String first, String last, String email, String username) {
        User user = new User();
        user.setId(id + 1000);
        user.setUsername(username);
        user.setRole("ROLE_COURIER");
        user.setKeycloakId("id" + username);

        Courier courier = new Courier();
        courier.setId(id);
        courier.setFirstName(first);
        courier.setLastName(last);
        courier.setEmail(email);
        courier.setUser(user);
        return courier;
    }

    @Test
    void testFindById() {
        Courier entity = sampleCourier(1L, "Ivan", "Ivanov", "ivanov@gmail.com", "vankata21");
        when(courierRepository.findById(1L)).thenReturn(Optional.of(entity));

        CourierDTO dto = courierService.getById(1L);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getFirstName()).isEqualTo("Ivan");
        assertThat(dto.getLastName()).isEqualTo("Ivanov");
        assertThat(dto.getEmail()).isEqualTo("ivanov@gmail.com");
        assertThat(dto.getUsername()).isEqualTo("vankata21");
        verify(courierRepository).findById(1L);
        verifyNoMoreInteractions(courierRepository);
    }

    @Test
    void testExceptionForGetById() {
        when(courierRepository.findById(4L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courierService.getById(4L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Courier not found with id: 4");

        verify(courierRepository).findById(4L);
        verifyNoMoreInteractions(courierRepository);
    }

    @Test
    void testGetAllCouriers() {
        Courier courier1 = sampleCourier(1L, "Georgi", "Dimitrov", "gdimitrov@gmail.com", "courier1");
        Courier courier2 = sampleCourier(2L, "Stoyan", "Ganev", "sganev@gmail.com", "courier2");
        when(courierRepository.findAll()).thenReturn(List.of(courier1, courier2));

        List<CourierDTO> out = courierService.getAll();

        assertThat(out.get(0).getId()).isEqualTo(1L);
        assertThat(out.get(0).getFirstName()).isEqualTo("Georgi");
        assertThat(out.get(0).getUsername()).isEqualTo("courier1");
        assertThat(out.get(1).getId()).isEqualTo(2L);
        assertThat(out.get(1).getLastName()).isEqualTo("Ganev");
        assertThat(out.get(1).getEmail()).isEqualTo("sganev@gmail.com");
        verify(courierRepository).findAll();
        verifyNoMoreInteractions(courierRepository);
    }

    @Test
    void testCreateFromRegistration() {
        RegistrationDTO dto = new RegistrationDTO();
        dto.setFirstName("Petar");
        dto.setLastName("Angelov");
        dto.setEmail("pangelov@gmail.com");

        User user = new User();
        user.setId(10L);
        user.setUsername("pangelov");
        user.setRole("ROLE_COURIER");
        user.setKeycloakId("id");

        ArgumentCaptor<Courier> captor = ArgumentCaptor.forClass(Courier.class);

        Courier savedCourier = sampleCourier(5L, "Petar", "Angelov", "pangelov@gmail.com", "pangelov");
        when(courierRepository.save(any(Courier.class))).thenReturn(savedCourier);

        Courier result = courierService.createFromRegistration(dto, user);

        verify(courierRepository).save(captor.capture());
        Courier courier = captor.getValue();
        assertThat(courier.getUser()).isSameAs(user);
        assertThat(courier.getFirstName()).isEqualTo("Petar");
        assertThat(courier.getLastName()).isEqualTo("Angelov");
        assertThat(courier.getEmail()).isEqualTo("pangelov@gmail.com");

        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getUser().getUsername()).isEqualTo("pangelov");

        verifyNoMoreInteractions(courierRepository);
    }
}
