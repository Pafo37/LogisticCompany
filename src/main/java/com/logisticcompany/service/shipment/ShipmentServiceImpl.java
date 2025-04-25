package com.logisticcompany.service.shipment;

import com.logisticcompany.data.dto.ShipmentDTO;
import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.Shipment;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.data.repository.ShipmentRepository;
import com.logisticcompany.data.repository.UserRepository;
import com.logisticcompany.service.client.ClientService;
import com.logisticcompany.service.employee.EmployeeService;
import com.logisticcompany.service.office.OfficeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

    private final ClientService clientService;

    private final OfficeService officeService;

    private final EmployeeService employeeService;

    private final UserRepository userRepository;

    @Override
    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    @Override
    public Shipment getShipmentById(long id) {
        return shipmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid shipment id" + id));
    }

    @Override
    public void saveShipment(Shipment shipment) {
        shipmentRepository.save(shipment);
    }

    @Override
    public void deleteShipment(Long id) {
        shipmentRepository.deleteById(id);
    }

    @Override
    public List<Shipment> getShipmentsByClient(Client client) {
        return shipmentRepository.findBySenderOrReceiver(client, client);
    }

    @Override
    public List<Shipment> findShipmentsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return shipmentRepository.findAllByCreatedAtBetween(
                startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
    }

    @Override
    public BigDecimal calculateTotalRevenue(LocalDate startDate, LocalDate endDate) {
        List<Shipment> shipments = findShipmentsBetweenDates(startDate, endDate);
        return shipments.stream()
                .map(shipment -> BigDecimal.valueOf(shipment.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Shipment> getShipmentsForClient(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        Client client = clientService.getByUser(user);

        return shipmentRepository.findBySenderOrReceiver(client, client);
    }

    @Override
    public Shipment createShipmentFromDTO(ShipmentDTO shipmentDTO, Principal principal) {
        Shipment shipment = new Shipment();
        mapDTOToShipment(shipmentDTO, shipment, principal);
        return shipmentRepository.save(shipment);
    }

    @Override
    public Shipment updateShipmentFromDTO(Long id, ShipmentDTO shipmentDTO, Principal principal) {
        Shipment shipment = getShipmentById(id);
        mapDTOToShipment(shipmentDTO, shipment, principal);
        return shipmentRepository.save(shipment);
    }

    private void mapDTOToShipment(ShipmentDTO dto, Shipment shipment, Principal principal) {
        shipment.setWeight(dto.getWeight());
        shipment.setDeliveredToOffice(dto.isDeliveredToOffice());
        shipment.setDeliveryAddress(dto.getDeliveryAddress());

        shipment.setSender(clientService.getClientById(dto.getSenderId()));
        shipment.setReceiver(clientService.getClientById(dto.getReceiverId()));

        if (dto.isDeliveredToOffice() && dto.getDeliveryOfficeId() != null) {
            shipment.setDeliveryOffice(officeService.getOfficeById(dto.getDeliveryOfficeId()));
        } else {
            shipment.setDeliveryOffice(null);
        }

        shipment.setPrice(calculatePrice(shipment.getWeight(), shipment.isDeliveredToOffice()));
        shipment.setRegisteredBy(employeeService.findEntityByUsername(principal.getName()));
    }

    public double calculatePrice(double weight, boolean deliveredToOffice) {
        double basePrice = 5.0;
        double weightFactor = weight * 0.5;

        if (deliveredToOffice) {
            return basePrice + weightFactor;
        } else {
            return (basePrice + weightFactor) * 1.5; // More expensive if delivered to home
        }
    }

}