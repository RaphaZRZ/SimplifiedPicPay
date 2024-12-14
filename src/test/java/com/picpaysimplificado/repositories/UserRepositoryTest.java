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


@ActiveProfiles("test") // Alterar qual application.properties será usado
@DataJpaTest // Indica que é uma classe de testes
class UserRepositoryTest {
    @Autowired
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;


    // Salva o usuário no banco de dados temporário
    private void saveUser(UserDTO userData) throws Exception {
        User user = new User(userData);
        this.entityManager.persist(user);
    }

    @Test
    @DisplayName("Must get User Successfully from DB.")
    void findUserByDocumentSuccess() throws Exception {
        String document = "75292019735";
        UserDTO userData = new UserDTO("Magnus", "Petrikov", document, "magnus@gmail.com",
                "magnusSenha", new BigDecimal("50.00"), UserType.COMMON);
        this.saveUser(userData);

        Optional<User> result = this.userRepository.findUserByDocument(document);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Must not get User from DB when user not exists.")
    void findUserByDocumentFailedDocumentNotFound() {
        Optional<User> result = this.userRepository.findUserByDocument("99999999999");
        assertThat(result.isEmpty()).isTrue();
    }
}