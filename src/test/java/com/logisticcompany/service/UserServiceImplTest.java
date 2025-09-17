package com.logisticcompany.service;

import com.logisticcompany.data.dto.RegistrationDTO;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.repository.UserRepository;
import com.logisticcompany.service.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void testFindByUsername() {
        User user = new User();
        user.setId(10L);
        user.setUsername("stamat7");
        user.setRole("ROLE_CLIENT");
        user.setKeycloakId("id");
        when(userRepository.findByUsername("stamat7")).thenReturn(Optional.of(user));

        User result = service.findByUsername("stamat7");

        assertThat(result).isSameAs(user);
        verify(userRepository).findByUsername("stamat7");
    }

    @Test
    void testFindByUsernameNotFound() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByUsername("missing"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("missing");

        verify(userRepository).findByUsername("missing");
    }

    @Test
    void testCreateRegistration() {
        RegistrationDTO dto = new RegistrationDTO();
        dto.setUsername("stamat7");
        dto.setRole("ROLE_COURIER");

        String keycloakId = "id";

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User toSave = inv.getArgument(0);
            toSave.setId(77L);
            return toSave;
        });

        User saved = service.createFromRegistration(dto, keycloakId);

        verify(userRepository).save(captor.capture());
        User passed = captor.getValue();

        assertThat(passed.getUsername()).isEqualTo("stamat7");
        assertThat(passed.getRole()).isEqualTo("ROLE_COURIER");
        assertThat(passed.getKeycloakId()).isEqualTo(keycloakId);

        assertThat(saved.getId()).isEqualTo(77L);
        assertThat(saved.getUsername()).isEqualTo("stamat7");
        assertThat(saved.getRole()).isEqualTo("ROLE_COURIER");
        assertThat(saved.getKeycloakId()).isEqualTo(keycloakId);
    }
}
