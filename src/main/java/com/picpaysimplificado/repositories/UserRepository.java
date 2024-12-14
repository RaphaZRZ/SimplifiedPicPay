package com.picpaysimplificado.repositories;

import com.picpaysimplificado.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Verificar existência de informações no banco de dados
    boolean existsByEmail(String email);

    boolean existsByDocument(String document);

    // Encontrar informações no banco de dados
    Optional<User> findUserByDocument(String document);
}
