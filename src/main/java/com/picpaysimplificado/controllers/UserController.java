package com.picpaysimplificado.controllers;

import com.picpaysimplificado.dtos.UpdatePasswordDTO;
import com.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.models.User;
import com.picpaysimplificado.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    UserService userService;


    @GetMapping("/id/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) throws Exception {
        User obj = this.userService.findUserById(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/document/{document}")
    public ResponseEntity<User> findUserByDocument(@PathVariable String document) throws Exception {
        User obj = this.userService.findUserByDocument(document);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> objs = this.userService.findAllUsers();
        return new ResponseEntity<>(objs, HttpStatus.OK);
    }

    // Valida do DTO antes de qualquer processamento ou interação com o banco de dados.
    @Validated
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userData) throws Exception {
        User user = this.userService.createUser(userData);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Validated
    @PutMapping("/id/{id}")
    public ResponseEntity<Void> updateUser(@Valid @RequestBody UpdatePasswordDTO passwordData, @PathVariable Long id) throws Exception {
        this.userService.updateUser(passwordData, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws Exception {
        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
