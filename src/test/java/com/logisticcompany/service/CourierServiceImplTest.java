package com.logisticcompany.service;

import com.logisticcompany.data.dto.CourierDTO;
import com.logisticcompany.data.dto.RegistrationDTO;
import com.logisticcompany.data.entity.Courier;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.repository.CourierRepository;
import com.logisticcompany.service.courier.CourierServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourierServiceImplTest {

    @Mock
    private CourierRepository courierRepository;

    @InjectMocks
    private CourierServiceImpl service;

    @Test
    void testGetById() {
        Courier courierEntity = new Courier();
        courierEntity.setId(42L);
        courierEntity.setFirstName("Petar");
        courierEntity.setLastName("Dimitrov");
        courierEntity.setEmail("pdimitrov@gmail.com");

        when(courierRepository.findById(42L)).thenReturn(Optional.of(courierEntity));

        CourierDTO courierDTO = service.getById(42L);

        assertThat(courierDTO.getId()).isEqualTo(42L);
        assertThat(courierDTO.getFirstName()).isEqualTo("Petar");
        assertThat(courierDTO.getLastName()).isEqualTo("Dimitrov");
        assertThat(courierDTO.getEmail()).isEqualTo("pdimitrov@gmail.com");
        verify(courierRepository).findById(42L);
    }

    @Test
    void testGetByIdNotFound() {
        when(courierRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("99");

        verify(courierRepository).findById(99L);
    }

    @Test
    void testGetAllCouriers() {
        Courier courier1 = new Courier();
        courier1.setId(1L);
        courier1.setFirstName("Georgi");
        courier1.setLastName("Ivanov");
        courier1.setEmail("givanov@gmail.com");

        Courier courier2 = new Courier();
        courier2.setId(2L);
        courier2.setFirstName("Dimitar");
        courier2.setLastName("Todorov");
        courier2.setEmail("dtodorov@gmail.com");

        when(courierRepository.findAll()).thenReturn(List.of(courier1, courier2));

        List<CourierDTO> courierList = service.getAll();

        assertThat(courierList).hasSize(2);
        assertThat(courierList.get(0).getId()).isEqualTo(1L);
        assertThat(courierList.get(0).getFirstName()).isEqualTo("Georgi");
        assertThat(courierList.get(0).getLastName()).isEqualTo("Ivanov");
        assertThat(courierList.get(0).getEmail()).isEqualTo("givanov@gmail.com");
        assertThat(courierList.get(1).getId()).isEqualTo(2L);
        assertThat(courierList.get(1).getFirstName()).isEqualTo("Dimitar");
        assertThat(courierList.get(1).getLastName()).isEqualTo("Todorov");
        assertThat(courierList.get(1).getEmail()).isEqualTo("dtodorov@gmail.com");

        verify(courierRepository).findAll();
    }

    @Test
    void testCreateCourier() {
        RegistrationDTO dto = new RegistrationDTO();
        dto.setFirstName("Ivan");
        dto.setLastName("Georgiev");
        dto.setEmail("igeorgiev@gmail.com");

        User user = new User();
        user.setId(7L);
        user.setUsername("ivan27");

        ArgumentCaptor<Courier> argumentCaptor = ArgumentCaptor.forClass(Courier.class);

        when(courierRepository.save(any(Courier.class))).thenAnswer(inv -> {
            Courier courier = inv.getArgument(0);
            courier.setId(123L);
            return courier;
        });

        Courier createdCourier = service.createFromRegistration(dto, user);

        verify(courierRepository).save(argumentCaptor.capture());
        Courier savedCourier = argumentCaptor.getValue();

        assertThat(savedCourier.getUser()).isSameAs(user);
        assertThat(savedCourier.getFirstName()).isEqualTo("Ivan");
        assertThat(savedCourier.getLastName()).isEqualTo("Georgiev");
        assertThat(savedCourier.getEmail()).isEqualTo("igeorgiev@gmail.com");

        assertThat(createdCourier.getId()).isEqualTo(123L);
        assertThat(createdCourier.getUser()).isSameAs(user);
        assertThat(createdCourier.getFirstName()).isEqualTo("Ivan");
        assertThat(createdCourier.getLastName()).isEqualTo("Georgiev");
        assertThat(createdCourier.getEmail()).isEqualTo("igeorgiev@gmail.com");
    }
}