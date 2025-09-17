package com.logisticcompany.repository;

import com.logisticcompany.data.entity.Client;
import com.logisticcompany.data.entity.User;
import com.logisticcompany.data.repository.ClientRepository;
import com.logisticcompany.data.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    void testFindByUserWithKeycloakId() {
        String keycloakId = "id";
        User user = new User();
        user.setUsername("client1");
        user.setRole("ROLE_CLIENT");
        user.setKeycloakId(keycloakId);
        userRepository.save(user);

        Client client = new Client();
        client.setUser(user);
        client.setName("Pesho");
        client.setEmail("pesho@gmail.com");
        clientRepository.save(client);

        var found = clientRepository.findByUser_KeycloakId(keycloakId);
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("pesho@gmail.com");
    }

    @Test
    void testFindByUser() {
        String keycloakId = "id";
        User user = new User();
        user.setUsername("client1");
        user.setRole("ROLE_CLIENT");
        user.setKeycloakId(keycloakId);
        userRepository.save(user);

        Client client = new Client();
        client.setUser(user);
        client.setName("Pesho");
        client.setEmail("pesho@gmail.com");
        clientRepository.save(client);

        var found = clientRepository.findByUser(user);
        assertThat(found).isPresent();
        assertThat(found.get().getUser()).isEqualTo(client.getUser());
    }
}