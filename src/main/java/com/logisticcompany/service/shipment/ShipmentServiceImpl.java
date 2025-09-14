package com.logisticcompany.service.shipment;

import com.logisticcompany.data.dto.ShipmentDTO;
import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.Courier;
import com.logisticcompany.data.entity.Shipment;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.data.repository.*;
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

    private final OfficeRepository officeRepository;

    private final OfficeEmployeeRepository officeEmployeeRepository;

    private final CourierRepository courierRepository;

    @Override
    public List<ShipmentDTO> getAllShipments() {
        return shipmentRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public ShipmentDTO getShipmentById(long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found"));
        return mapToDTO(shipment);
    }

    @Override
    public ShipmentDTO saveShipment(ShipmentDTO shipmentDTO, Principal principal) {
        Shipment shipment = new Shipment();
        mapDTOToShipment(shipmentDTO, shipment, principal);
        shipment.setStatus(Shipment.Status.PENDING_ASSIGNMENT);
        Shipment saved = shipmentRepository.save(shipment);
        return mapToDTO(saved);
    }

    @Override
    public void deleteShipment(Long id) {
        shipmentRepository.deleteById(id);
    }

    @Override
    public List<ShipmentDTO> getShipmentsByClient(Client client) {
        return shipmentRepository.findBySenderOrReceiver(client, client)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<ShipmentDTO> findShipmentsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return shipmentRepository.findAllByCreatedAtBetween(
                        startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay())
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public BigDecimal calculateTotalRevenue(LocalDate startDate, LocalDate endDate) {
        List<Shipment> shipments = findShipmentEntitiesBetweenDates(startDate, endDate);
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
    public ShipmentDTO updateShipmentFromDTO(Long id, ShipmentDTO shipmentDTO, Principal principal) {
        Shipment shipment = findShipmentEntityById(id);
        mapDTOToShipment(shipmentDTO, shipment, principal);
        Shipment updated = shipmentRepository.save(shipment);
        return mapToDTO(updated);
    }

    @Override
    public void assignCourier(Long shipmentId, Long courierId, Principal principal) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found"));
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new IllegalArgumentException("Courier not found"));

        shipment.setAssignedCourier(courier);
        shipment.setStatus(Shipment.Status.ASSIGNED);
        shipmentRepository.save(shipment);
    }

    @Override
    public List<ShipmentDTO> getShipmentsForCourier(Principal principal) {
        String sub = principal.getName();
        return shipmentRepository.findAllByAssignedCourier_User_KeycloakId(sub).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public void markDelivered(Long shipmentId, Principal principal) {
        String courierSub = principal.getName();
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found"));

        if (shipment.getAssignedCourier() == null
                || shipment.getAssignedCourier().getUser() == null
                || !courierSub.equals(shipment.getAssignedCourier().getUser().getKeycloakId())) {
            throw new IllegalStateException("You are not assigned to this shipment");
        }

        shipment.setStatus(Shipment.Status.DELIVERED);
        shipmentRepository.save(shipment);
    }



    private void mapDTOToShipment(ShipmentDTO dto, Shipment shipment, Principal principal) {
        shipment.setDeliveryAddress(null);
        shipment.setDeliveryOffice(null);

        shipment.setWeight(dto.getWeight());
        shipment.setDeliveryAddress(dto.getDeliveryAddress());
        shipment.setReceiver(clientService.getClientById(dto.getReceiverId()));

        if (dto.getDeliveryOfficeId() != null) {
            shipment.setDeliveryOffice(officeService.findEntityById(dto.getDeliveryOfficeId()));
        } else if (dto.getDeliveryAddress() != null && !dto.getDeliveryAddress().isBlank()) {
            shipment.setDeliveryAddress(dto.getDeliveryAddress().trim());
        } else {
            throw new IllegalArgumentException("Must provide either a delivery office or a delivery address.");
        }

        boolean deliveredToOffice = dto.getDeliveryOfficeId() != null;
        shipment.setPrice(calculatePrice(dto.getWeight(), deliveredToOffice));

        // Sender is always the logged-in client
        shipment.setSender(clientService.findClientById(principal.getName()));
    }

    private ShipmentDTO mapToDTO(Shipment shipment) {
        ShipmentDTO dto = new ShipmentDTO();
        dto.setId(shipment.getId());
        dto.setSenderId(shipment.getSender().getId());
        dto.setSenderName(shipment.getSender().getName());
        dto.setReceiverId(shipment.getReceiver().getId());
        dto.setReceiverName(shipment.getReceiver().getName());
        dto.setDeliveryAddress(shipment.getDeliveryAddress());
        dto.setWeight(shipment.getWeight());
        dto.setPrice(shipment.getPrice());
        dto.setStatus(shipment.getStatus().name());

        if (shipment.getDeliveryOffice() != null) {
            dto.setDeliveryOfficeId(shipment.getDeliveryOffice().getId());
            dto.setDeliveryOfficeName(shipment.getDeliveryOffice().getName());
        }

        if (shipment.getAssignedCourier() != null && shipment.getAssignedCourier().getUser() != null) {
            dto.setAssignedCourierName(shipment.getAssignedCourier().getUser().getUsername());
        }

        if (shipment.getRegisteredBy() != null) {
            dto.setRegisteredByName(shipment.getRegisteredBy().getUser().getUsername());
        }

        return dto;
    }

    private List<Shipment> findShipmentEntitiesBetweenDates(LocalDate startDate, LocalDate endDate) {
        return shipmentRepository.findAllByCreatedAtBetween(
                startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay()
        );
    }

    private Shipment findShipmentEntityById(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found"));
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