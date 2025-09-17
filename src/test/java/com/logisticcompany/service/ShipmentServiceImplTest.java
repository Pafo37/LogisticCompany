package com.logisticcompany.service;


import com.logisticcompany.data.dto.CreateShipmentDTO;
import com.logisticcompany.data.dto.ShipmentDTO;
import com.logisticcompany.data.entity.*;
import com.logisticcompany.repository.*;
import com.logisticcompany.service.officeemployee.OfficeEmployeeServiceImpl;
import com.logisticcompany.service.shipment.ShipmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Principal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ShipmentServiceImplTest {

    @Mock
    ShipmentRepository shipmentRepository;
    @Mock
    ClientRepository clientRepository;
    @Mock
    OfficeRepository officeRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    CourierRepository courierRepository;
    @Mock
    OfficeEmployeeRepository officeEmployeeRepository;
    @Mock
    OfficeEmployeeServiceImpl officeEmployeeService;

    @InjectMocks
    ShipmentServiceImpl service;


    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createShipment_toOffice_setsStatusAndPrice() {
        String keycloakId = "id";
        String officeName = "Ekont";
        Principal principal = () -> keycloakId;

        User senderUser = new User();
        senderUser.setKeycloakId(keycloakId);

        Client sender = new Client();
        sender.setId(1L);
        sender.setUser(senderUser);
        sender.setName("Gosho");

        Client receiver = new Client();
        receiver.setId(2L);
        receiver.setName("Ivan");

        Office office = new Office();
        office.setId(3L);
        office.setName(officeName);

        when(clientRepository.findByUser_KeycloakId(keycloakId)).thenReturn(Optional.of(sender));
        when(clientRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(officeRepository.findById(3L)).thenReturn(Optional.of(office));
        when(shipmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        CreateShipmentDTO dto = new CreateShipmentDTO();
        dto.setReceiverId(2L);
        dto.setDeliveryOfficeId(3L);
        dto.setWeight(4.0);

        ShipmentDTO out = service.createShipment(dto, principal);

        assertThat(out.getStatus()).isEqualTo("PENDING_ASSIGNMENT");
        assertThat(out.getPrice()).isEqualTo(7.0);
        assertThat(out.getDeliveryOfficeName()).isEqualTo(officeName);
        assertThat(out.getDeliveryAddress()).isNull();

        verify(shipmentRepository).save(any(Shipment.class));
    }

    @Test
    void assignCourier_setsRegisteredByAndStatusAssigned() {
        Shipment shipment = new Shipment();
        shipment.setId(10L);
        shipment.setStatus(Shipment.Status.PENDING_ASSIGNMENT);

        Courier courier = new Courier();
        courier.setId(5L);
        User courierUser = new User();
        courierUser.setUsername("courier1");
        courier.setUser(courierUser);

        Principal principal = () -> "sub-oe";
        User officEmployeeUser = new User();
        officEmployeeUser.setKeycloakId("sub-oe");
        OfficeEmployee officeEmployee = new OfficeEmployee();
        officeEmployee.setUser(officEmployeeUser);

        when(shipmentRepository.findById(10L)).thenReturn(Optional.of(shipment));
        when(courierRepository.findById(5L)).thenReturn(Optional.of(courier));
        when(userRepository.findByKeycloakId("sub-oe")).thenReturn(Optional.of(officEmployeeUser));
        when(officeEmployeeRepository.findByUser(officEmployeeUser)).thenReturn(Optional.of(officeEmployee));
        when(officeEmployeeService.getByUser(officEmployeeUser)).thenReturn(officeEmployee);

        service.assignCourier(10L, 5L, principal);

        assertThat(shipment.getAssignedCourier()).isEqualTo(courier);
        assertThat(shipment.getRegisteredBy()).isEqualTo(officeEmployee);
        assertThat(shipment.getStatus()).isEqualTo(Shipment.Status.ASSIGNED);

        verify(shipmentRepository).save(shipment);
    }
}
