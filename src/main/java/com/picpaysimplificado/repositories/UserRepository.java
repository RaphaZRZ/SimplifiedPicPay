package com.picpaysimplificado.repositories;

import com.picpaysimplificado.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Check if the data already exists in the database
    boolean existsByEmail(String email);
    boolean existsByDocument(String document);

    Optional<User> findByDocument(String document);
}
