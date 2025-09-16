package com.logisticcompany.service;

import com.logisticcompany.data.dto.UpdateClientDTO;
import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.data.repository.ClientRepository;
import com.logisticcompany.data.repository.UserRepository;
import com.logisticcompany.service.client.ClientServiceImpl;
import com.logisticcompany.service.keycloak.KeyCloakService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ClientServiceImplKeycloakTest {

    @Mock
    ClientRepository clientRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    KeyCloakService keyCloakService;

    @InjectMocks
    ClientServiceImpl service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateClientAndKeycloak_updatesBoth() {
        String keycloakId = "id";
        var user = new User();
        user.setId(9L);
        user.setKeycloakId(keycloakId);

        var client = new Client();
        client.setId(1L);
        client.setUser(user);
        client.setEmail("gosho@gmail.com");
        client.setName("Gosho");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        UpdateClientDTO dto = new UpdateClientDTO();
        dto.setEmail("ivan@gmail.com");
        dto.setName("Ivan");
        dto.setUsername("Vankata");

        service.updateClientAndKeycloak(1L, dto);

        verify(keyCloakService).updateUser(keycloakId, "ivan@gmail.com", "Ivan", "Vankata");
        verify(clientRepository).save(client);
        assertThat(client.getEmail()).isEqualTo("ivan@gmail.com");
    }
}