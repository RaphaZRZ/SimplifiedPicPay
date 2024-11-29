package com.picpaysimplificado.controllers;

import com.picpaysimplificado.dtos.UsuarioDTO;
import com.picpaysimplificado.model.Usuario;
import com.picpaysimplificado.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@Validated
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;


    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findUsuarioById(@PathVariable Long id) throws Exception {
        Usuario obj = this.usuarioService.findUsuarioById(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> findAllUsuarios() {
        List<Usuario> objs = this.usuarioService.findAllUsuarios();
        return new ResponseEntity<>(objs, HttpStatus.OK);
    }

    @Validated
    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@Valid @RequestBody UsuarioDTO data) throws Exception{
        Usuario usuario = this.usuarioService.createUsuario(data);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }
}
