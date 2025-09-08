package com.logisticcompany.service.courier;

import com.logisticcompany.data.entity.Courier;
import com.logisticcompany.data.entity.Shipment;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.data.repository.CourierRepository;
import com.logisticcompany.data.repository.ShipmentRepository;
import com.logisticcompany.data.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourierServiceImpl implements CourierService {

    private final CourierRepository courierRepository;
    private final UserRepository userRepository;
    private final ShipmentRepository shipmentRepository;

    @Override
    public Courier getById(Long id) {
        return courierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Courier not found: " + id));
    }

    @Override
    public List<Courier> getAll() {
        return courierRepository.findAll();
    }

    @Override
    public Optional<Courier> findByUserKeycloakId(String keycloakId) {
        return courierRepository.findByUser_KeycloakId(keycloakId);
    }

    @Override
    public Courier getByUserKeycloakId(String keycloakId) {
        return courierRepository.findByUser_KeycloakId(keycloakId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Courier profile not found for user sub: " + keycloakId));
    }

    @Override
    public Courier createForUser(User user) {
        Optional<Courier> existing = courierRepository.findByUser(user);
        if (existing.isPresent()) {
            return existing.get();
        }

        Courier profile = new Courier();
        profile.setUser(user);
        return courierRepository.save(profile);
    }

    @Override
    public Courier createForUserKeycloakId(String keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new EntityNotFoundException("User not found by sub: " + keycloakId));
        return createForUser(user);
    }

    @Override
    public void deleteById(Long id) {
        if (!courierRepository.existsById(id)) {
            throw new EntityNotFoundException("Courier not found: " + id);
        }
        courierRepository.deleteById(id);
    }

    @Override
    public List<Shipment> getShipments(Courier courier) {
        return shipmentRepository.findByAssignedCourier(courier);
    }

    @Override
    public List<Shipment> getShipmentsByUserKeycloakId(String keycloakId) {
        Courier courier = getByUserKeycloakId(keycloakId);
        return shipmentRepository.findByAssignedCourier(courier);
    }
}
