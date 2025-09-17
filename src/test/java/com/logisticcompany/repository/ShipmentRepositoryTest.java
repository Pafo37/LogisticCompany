package com.logisticcompany.repository;

import com.logisticcompany.data.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ShipmentRepositoryTest {

    @Autowired
    ShipmentRepository shipmentRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    OfficeRepository officeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourierRepository courierRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSumRevenueOnlyOneShipmentDelivered() {
        Client client1 = clientRepository.save(new Client());
        Client client2 = clientRepository.save(new Client());
        Office office = officeRepository.save(new Office());

        Shipment shipment1 = new Shipment();
        shipment1.setSender(client1);
        shipment1.setReceiver(client2);
        shipment1.setDeliveryOffice(office);
        shipment1.setWeight(2.0);
        shipment1.setPrice(10.0);
        shipment1.setStatus(Status.PENDING_ASSIGNMENT);
        shipmentRepository.save(shipment1);

        Shipment shipment2 = new Shipment();
        shipment2.setSender(client1);
        shipment2.setReceiver(client2);
        shipment2.setDeliveryAddress("ul.Ekont 45");
        shipment2.setWeight(3.0);
        shipment2.setPrice(15.5);
        shipment2.setStatus(Status.DELIVERED);
        shipmentRepository.save(shipment2);

        var start = java.time.LocalDateTime.now().minusHours(1);
        var end = java.time.LocalDateTime.now().plusHours(1);

        var sum = shipmentRepository.sumRevenueBetween(start, end);
        assertThat(sum).isEqualByComparingTo("15.5");
    }

    @Test
    void testSumRevenueNoShipmentDelivered() {
        Client client1 = clientRepository.save(new Client());
        Client client2 = clientRepository.save(new Client());
        Office office = officeRepository.save(new Office());

        Shipment shipment1 = new Shipment();
        shipment1.setSender(client1);
        shipment1.setReceiver(client2);
        shipment1.setDeliveryOffice(office);
        shipment1.setWeight(2.0);
        shipment1.setPrice(10.0);
        shipment1.setStatus(Status.PENDING_ASSIGNMENT);
        shipmentRepository.save(shipment1);

        Shipment shipment2 = new Shipment();
        shipment2.setSender(client1);
        shipment2.setReceiver(client2);
        shipment2.setDeliveryAddress("ul.Ekont 45");
        shipment2.setWeight(3.0);
        shipment2.setPrice(15.5);
        shipment2.setStatus(Status.PENDING_ASSIGNMENT);
        shipmentRepository.save(shipment2);

        var start = java.time.LocalDateTime.now().minusHours(1);
        var end = java.time.LocalDateTime.now().plusHours(1);

        var sum = shipmentRepository.sumRevenueBetween(start, end);
        assertThat(sum).isEqualByComparingTo("0");
    }

    @Test
    void testFindBySenderAndReceiver() {
        Client client1 = clientRepository.save(new Client());
        Client client2 = clientRepository.save(new Client());

        Shipment shipment1 = new Shipment();
        shipment1.setSender(client1);
        shipment1.setReceiver(client2);
        shipment1.setDeliveryAddress("ul.Petko voivoda 5");
        shipment1.setWeight(2.0);
        shipment1.setPrice(10.0);
        shipment1.setStatus(Status.PENDING_ASSIGNMENT);
        shipmentRepository.save(shipment1);

        List<Shipment> shipmentList = shipmentRepository.findBySenderOrReceiver(client1, client2);

        assertThat(shipmentList).hasSize(1).containsExactly(shipment1);
    }

    @Test
    void testFindBySenderAndReceiverWithWrongClient() {
        Client client1 = clientRepository.save(new Client());
        Client client2 = clientRepository.save(new Client());
        Client client3 = clientRepository.save(new Client());

        Shipment shipment1 = new Shipment();
        shipment1.setSender(client1);
        shipment1.setReceiver(client2);
        shipment1.setDeliveryAddress("ul.Petko voivoda 5");
        shipment1.setWeight(2.0);
        shipment1.setPrice(10.0);
        shipment1.setStatus(Status.PENDING_ASSIGNMENT);
        shipmentRepository.save(shipment1);

        List<Shipment> shipmentList = shipmentRepository.findBySenderOrReceiver(client3, client3);

        assertThat(shipmentList).hasSize(0);
        assertThat(shipmentList).doesNotContain(shipment1);
    }

    @Test
    void testFindShipmentByAssignedCourierWithKeyCloakId() {
        User user = new User();
        user.setKeycloakId("id");
        userRepository.save(user);

        Courier courier = new Courier();
        courier.setUser(user);
        courierRepository.save(courier);

        Shipment shipment = new Shipment();
        shipment.setAssignedCourier(courier);
        shipment.setDeliveryAddress("Somewhere");
        shipment.setWeight(2.0);
        shipment.setPrice(20.0);
        shipment.setStatus(Status.PENDING_ASSIGNMENT);
        shipmentRepository.save(shipment);

        List<Shipment> result =
                shipmentRepository.findAllByAssignedCourier_User_KeycloakId("id");

        assertThat(result).hasSize(1);
        assertThat(result).contains(shipment);
    }

}
