package com.picpaysimplificado.repositories;

import com.picpaysimplificado.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Verificar existência de informações no banco de dados
    boolean existsByEmail(String email);

    boolean existsByDocument(String document);

    // Encontrar informações no banco de dados
    Optional<Usuario> findUsuarioByDocument(String document);
}
