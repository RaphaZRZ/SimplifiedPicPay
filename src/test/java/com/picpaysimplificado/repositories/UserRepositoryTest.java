package com.picpaysimplificado.repositories;

import com.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.models.User;
import com.picpaysimplificado.models.UserType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;


@ActiveProfiles("test") // Select which application.properties file will be used
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;


    // Save the user in the in-memory database
    private void saveUser(UserDTO userData) throws Exception {
        User user = new User(userData);
        this.entityManager.persist(user);
    }

    @Test
    @DisplayName("Must get User Successfully from DB.")
    void findByDocumentSuccess() throws Exception {
        String document = "75292019735";
        UserDTO userData = new UserDTO("Magnus", "Petrikov", document, "magnus@gmail.com",
                "magnusPassword", new BigDecimal("50.00"), UserType.COMMON);
        this.saveUser(userData);

        Optional<User> result = this.userRepository.findByDocument(document);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Must not get User from DB when user not exists.")
    void findByDocumentFailedDocumentNotFound() {
        Optional<User> result = this.userRepository.findByDocument("99999999999");
        assertThat(result.isEmpty()).isTrue();
    }
}