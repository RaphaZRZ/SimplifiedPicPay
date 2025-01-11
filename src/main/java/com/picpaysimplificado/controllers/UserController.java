package com.picpaysimplificado.controllers;

import com.picpaysimplificado.dtos.UpdatePasswordDTO;
import com.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.models.User;
import com.picpaysimplificado.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    // GET MAPPINGS
    @Operation(description = "Find a user by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the user."),
            @ApiResponse(responseCode = "400", description = "Invalid format for ID."),
            @ApiResponse(responseCode = "404", description = "The ID doesn't belong to any user.")
    }
    )
    @GetMapping("/id/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) throws Exception {
        User obj = this.userService.findUserById(id);
        return ResponseEntity.ok().body(obj);
    }

    @Operation(description = "Find a user by document.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the user."),
            @ApiResponse(responseCode = "400", description = "Invalid format for document."),
            @ApiResponse(responseCode = "404", description = "The document doesn't belong to any user.")
    })
    @GetMapping("/document/{document}")
    public ResponseEntity<User> findUserByDocument(@PathVariable String document) throws Exception {
        User obj = this.userService.findUserByDocument(document);
        return ResponseEntity.ok().body(obj);
    }

    @Operation(description = "Find all users.")
    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> objs = this.userService.findAllUsers();
        return new ResponseEntity<>(objs, HttpStatus.OK);
    }

    // POST MAPPINGS
    @Operation(description = "Create a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid data to create a user."),
            @ApiResponse(responseCode = "409", description = "Document or Email already registered.")
    })
    @Validated // Validate the DTO before any processing or interaction with the database
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userData) throws Exception {
        User user = this.userService.createUser(userData);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // PUT MAPPINGS
    @Operation(description = "Change the user's password by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User's password changed successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid password."),
            @ApiResponse(responseCode = "404", description = "The ID doesn't belong to any user.")
    })
    @Validated
    @PutMapping("/id/{id}")
    public ResponseEntity<Void> updateUserById(@Valid @RequestBody UpdatePasswordDTO passwordData, @PathVariable Long id) throws Exception {
        this.userService.updateUserById(passwordData, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "Change the user's password by document.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User's password changed successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid password."),
            @ApiResponse(responseCode = "404", description = "The document doesn't belong to any user.")
    })
    @Validated
    @PutMapping("/document/{document}")
    public ResponseEntity<Void> updateUserByDocument(@Valid @RequestBody UpdatePasswordDTO passwordData, @PathVariable String document) throws Exception {
        this.userService.updateUserByDocument(passwordData, document);
        return ResponseEntity.noContent().build();
    }

    // DELETE MAPPINGS
    @Operation(description = "Delete a user by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully."),
            @ApiResponse(responseCode = "404", description = "The ID doesn't belong to any user.")
    })
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) throws Exception {
        this.userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "Delete a user by document.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully."),
            @ApiResponse(responseCode = "404", description = "The document doesn't belong to any user.")
    })
    @DeleteMapping("/document/{document}")
    public ResponseEntity<Void> deleteUserByDocument(@PathVariable String document) throws Exception {
        this.userService.deleteUserByDocument(document);
        return ResponseEntity.noContent().build();
    }
}
