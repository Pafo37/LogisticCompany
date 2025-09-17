package com.logisticcompany.service;

import com.logisticcompany.data.dto.OfficeEmployeeDTO;
import com.logisticcompany.data.dto.RegistrationDTO;
import com.logisticcompany.data.entity.OfficeEmployee;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.repository.OfficeEmployeeRepository;
import com.logisticcompany.repository.UserRepository;
import com.logisticcompany.service.officeemployee.OfficeEmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfficeEmployeeServiceImplTest {

    @Mock
    private OfficeEmployeeRepository officeEmployeeRepository;

    @InjectMocks
    private OfficeEmployeeServiceImpl service;

    @Test
    void testFindById() {
        OfficeEmployee officeEmployee = new OfficeEmployee();
        officeEmployee.setId(1L);
        officeEmployee.setName("Toni Storaro");
        officeEmployee.setEmail("tonistoraro@gmail.com");

        User user = new User();
        user.setUsername("tonistoraro");
        officeEmployee.setUser(user);

        when(officeEmployeeRepository.findById(1L)).thenReturn(Optional.of(officeEmployee));

        OfficeEmployeeDTO dto = service.getById(1L);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Toni Storaro");
        assertThat(dto.getEmail()).isEqualTo("tonistoraro@gmail.com");
        assertThat(dto.getUsername()).isEqualTo("tonistoraro");

        verify(officeEmployeeRepository).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(officeEmployeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");

        verify(officeEmployeeRepository).findById(99L);
    }

    @Test
    void testGetAllOfficeEmployees() {
        OfficeEmployee officeEmployee1 = new OfficeEmployee();
        officeEmployee1.setId(1L);
        officeEmployee1.setName("Konstantin Petrov");
        officeEmployee1.setEmail("kpetrov@gmail.com");

        OfficeEmployee officeEmployee2 = new OfficeEmployee();
        officeEmployee2.setId(2L);
        officeEmployee2.setName("Nikolai Popov");
        officeEmployee2.setEmail("npopov@gmail.com");

        when(officeEmployeeRepository.findAll()).thenReturn(List.of(officeEmployee1, officeEmployee2));

        List<OfficeEmployeeDTO> result = service.getAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Konstantin Petrov");
        assertThat(result.get(1).getName()).isEqualTo("Nikolai Popov");

        verify(officeEmployeeRepository).findAll();
    }

    @Test
    void testCreateOfficeEmployee() {
        RegistrationDTO dto = new RegistrationDTO();
        dto.setFirstName("Momchil");
        dto.setLastName("Petrov");
        dto.setEmail("mpetrov@gmail.com");

        User user = new User();
        user.setId(5L);
        user.setUsername("mpetrov");

        OfficeEmployee saved = new OfficeEmployee();
        saved.setId(100L);
        saved.setName("Momchil Petrov");
        saved.setEmail("mpetrov@gmail.com");
        saved.setUser(user);

        when(officeEmployeeRepository.save(any(OfficeEmployee.class))).thenReturn(saved);

        OfficeEmployee result = service.createFromRegistration(dto, user);

        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getName()).isEqualTo("Momchil Petrov");
        assertThat(result.getEmail()).isEqualTo("mpetrov@gmail.com");
        assertThat(result.getUser()).isSameAs(user);

        ArgumentCaptor<OfficeEmployee> captor = ArgumentCaptor.forClass(OfficeEmployee.class);
        verify(officeEmployeeRepository).save(captor.capture());
        OfficeEmployee passed = captor.getValue();
        assertThat(passed.getName()).isEqualTo("Momchil Petrov");
        assertThat(passed.getEmail()).isEqualTo("mpetrov@gmail.com");
        assertThat(passed.getUser()).isSameAs(user);
    }

    @Test
    void testFindByUser() {
        User user = new User();
        user.setId(20L);
        user.setUsername("petko");

        OfficeEmployee officeEmployee = new OfficeEmployee();
        officeEmployee.setId(200L);
        officeEmployee.setUser(user);
        officeEmployee.setName("Petko Petkov");

        when(officeEmployeeRepository.findByUser(user)).thenReturn(Optional.of(officeEmployee));

        OfficeEmployee result = service.getByUser(user);

        assertThat(result).isSameAs(officeEmployee);
        verify(officeEmployeeRepository).findByUser(user);
    }

    @Test
    void testFindByUserNotFound() {
        User user = new User();
        user.setUsername("missing");

        when(officeEmployeeRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByUser(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("missing");

        verify(officeEmployeeRepository).findByUser(user);
    }
}