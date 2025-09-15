package com.logisticcompany.service.shipment;

import com.logisticcompany.data.dto.CreateShipmentDTO;
import com.logisticcompany.data.dto.ShipmentDTO;
import com.logisticcompany.data.entity.*;
import com.logisticcompany.data.repository.*;
import com.logisticcompany.service.client.ClientService;
import com.logisticcompany.service.office.OfficeService;
import com.logisticcompany.service.officeemployee.OfficeEmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

    private final ClientService clientService;

    private final OfficeService officeService;

    private final UserRepository userRepository;

    private final CourierRepository courierRepository;

    private final OfficeEmployeeService officeEmployeeService;

    private final ClientRepository clientRepository;

    private final OfficeRepository officeRepository;

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
    public BigDecimal calculateTotalRevenue(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay();

        return shipmentRepository.sumRevenueBetween(start, end);
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
        String userName = principal.getName();
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found"));
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new IllegalArgumentException("Courier not found"));

        User user = userRepository.findByKeycloakId(userName)
                .orElseThrow(() -> new IllegalArgumentException("User not found for keycloakId: " + userName));

        OfficeEmployee officeEmployee = officeEmployeeService.getByUser(user);

        if (shipment.getRegisteredBy() == null) {
            shipment.setRegisteredBy(officeEmployee);
        }

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
    public List<ShipmentDTO> getShipmentsForCurrentClient(Principal principal) {
        String sub = principal.getName();
        var client = clientService.getByUserKeycloakId(sub);
        return shipmentRepository.findBySenderOrReceiver(client, client).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public ShipmentDTO createShipment(CreateShipmentDTO dto, Principal principal) {
        String keycloakID = principal.getName();

        Client sender = clientRepository.findByUser_KeycloakId(keycloakID)
                .orElseThrow(() -> new IllegalArgumentException("Sender (client) not found"));

        Client receiver = clientRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        Shipment shipment = new Shipment();
        shipment.setSender(sender);
        shipment.setReceiver(receiver);
        shipment.setWeight(dto.getWeight());
        shipment.setStatus(Shipment.Status.PENDING_ASSIGNMENT);
        shipment.setCreatedAt(LocalDateTime.now());

        boolean deliveredToOffice = false;

        if (dto.getDeliveryOfficeId() != null) {
            shipment.setDeliveryOffice(officeRepository.findById(dto.getDeliveryOfficeId())
                    .orElseThrow(() -> new IllegalArgumentException("Office not found")));
            shipment.setDeliveryAddress(null);
            deliveredToOffice = true;
        } else {
            shipment.setDeliveryAddress(dto.getDeliveryAddress());
            shipment.setDeliveryOffice(null);
        }

        shipment.setPrice(calculatePrice(dto.getWeight(), deliveredToOffice));

        shipment = shipmentRepository.save(shipment);
        return mapToDTO(shipment);
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

        if (shipment.getRegisteredBy() != null && shipment.getRegisteredBy().getUser() != null) {
            dto.setRegisteredByName(shipment.getRegisteredBy().getUser().getUsername());
        } else {
            dto.setRegisteredByName("—");
        }

        return dto;
    }

    private Shipment findShipmentEntityById(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found"));
    }

    public double calculatePrice(double weight, boolean isDeliveredToOffice) {
        double basePrice = 5.0;
        double weightFactor = weight * 0.5;

        if (isDeliveredToOffice) {
            return basePrice + weightFactor;
        } else {
            return (basePrice + weightFactor) * 1.5; // More expensive if delivered to home
        }
    }

}