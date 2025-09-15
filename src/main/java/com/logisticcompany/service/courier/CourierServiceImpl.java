package com.logisticcompany.service.courier;

import com.logisticcompany.data.dto.CourierDTO;
import com.logisticcompany.data.dto.RegistrationDTO;
import com.logisticcompany.data.entity.Courier;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.data.repository.CourierRepository;
import com.logisticcompany.mapper.CourierMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourierServiceImpl implements CourierService {

    private final CourierRepository courierRepository;

    @Override
    public CourierDTO getById(Long id) {
        return courierRepository.findById(id)
                .map(CourierMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Courier not found with id: " + id));
    }

    @Override
    public List<CourierDTO> getAll() {
        return courierRepository.findAll()
                .stream()
                .map(CourierMapper::toDTO)
                .toList();
    }

    @Override
    public Courier createFromRegistration(RegistrationDTO dto, User user) {
        Courier courier = new Courier();
        courier.setUser(user);
        courier.setFirstName(dto.getFirstName());
        courier.setLastName(dto.getLastName());
        courier.setEmail(dto.getEmail());
        return courierRepository.save(courier);
    }

}