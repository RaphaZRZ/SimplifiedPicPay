package com.picpaysimplificado.repositories;

import com.picpaysimplificado.dtos.UsuarioDTO;
import com.picpaysimplificado.models.Usuario;
import com.picpaysimplificado.models.UsuarioType;
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
class UsuarioRepositoryTest {
    @Autowired
    EntityManager entityManager;

    @Autowired
    UsuarioRepository usuarioRepository;


    @Test
    @DisplayName("Must get User Successfully from DB.")
    void findUsuarioByDocumentSuccess() throws Exception {
        String document = "75292019735";
        UsuarioDTO userData = new UsuarioDTO("Magnus", "Petrikov", document, "magnus@gmail.com",
                "magnusSenha", new BigDecimal("50.00"), UsuarioType.COMMON);
        this.createUser(userData);

        Optional<Usuario> result = this.usuarioRepository.findUsuarioByDocument(document);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Must not get User from DB when user not exists.")
    void findUsuarioByDocumentFailedDocumentNotFound() {
        Optional<Usuario> result = this.usuarioRepository.findUsuarioByDocument("99999999999");
        assertThat(result.isEmpty()).isTrue();
    }

    private void createUser(UsuarioDTO userData) throws Exception {
        Usuario user = new Usuario(userData);
        this.entityManager.persist(user);
    }
}