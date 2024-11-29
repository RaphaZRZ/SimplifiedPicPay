package com.picpaysimplificado.services;

import com.picpaysimplificado.dtos.UsuarioDTO;
import com.picpaysimplificado.model.Usuario;
import com.picpaysimplificado.model.UsuarioType;
import com.picpaysimplificado.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;


    public void validateTransaction(Usuario sender, BigDecimal amount) throws Exception {
        if (sender.getUsuarioType() == UsuarioType.MERCHANT)
            throw new Exception("Usuário do tipo lojista não está autorizado a realizar transação.");

        if (sender.getBalance().compareTo(amount) < 0)
            throw new Exception("Saldo insuficiente.");
    }

    public Usuario findUsuarioById(Long id) throws Exception {
        return this.usuarioRepository.findById(id).orElseThrow(() -> new Exception("Usuário não encontrado"));
    }

    public List<Usuario> findAllUsuarios() {
        return this.usuarioRepository.findAll();
    }

    public void saveUsuario(Usuario usuario) {
        this.usuarioRepository.save(usuario);
    }

    public Usuario createUsuario(UsuarioDTO usuarioData) throws Exception{
        Usuario Usuario = new Usuario(usuarioData);
        saveUsuario(Usuario);
        return Usuario;
    }
}
