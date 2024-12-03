package com.picpaysimplificado.services;

import com.picpaysimplificado.dtos.UpdatePasswordDTO;
import com.picpaysimplificado.dtos.UsuarioDTO;
import com.picpaysimplificado.exceptions.InsufficientBalanceException;
import com.picpaysimplificado.exceptions.MerchantTransactionNotAllowedException;
import com.picpaysimplificado.exceptions.UserNotFoundException;
import com.picpaysimplificado.models.Usuario;
import com.picpaysimplificado.models.UsuarioType;
import com.picpaysimplificado.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;


    public void validateTransaction(Usuario sender, BigDecimal amount) throws Exception {
        if (sender.getUsuarioType() == UsuarioType.MERCHANT)
            throw new MerchantTransactionNotAllowedException();

        if (sender.getBalance().compareTo(amount) < 0)
            throw new InsufficientBalanceException();
    }

    public Usuario findUsuarioById(Long id) throws Exception {
        return this.usuarioRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public Usuario findUsuarioByDocument(String document) throws Exception {
        return this.usuarioRepository.findUsuarioByDocument(document).orElseThrow(UserNotFoundException::new);
    }

    public List<Usuario> findAllUsuarios() {
        return this.usuarioRepository.findAll();
    }

    public void saveUsuario(Usuario usuario) {
        this.usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario createUsuario(UsuarioDTO usuarioData) throws Exception{
        Usuario usuario = new Usuario(usuarioData);
        saveUsuario(usuario);
        return usuario;
    }

    @Transactional
    public void updateUsuario(UpdatePasswordDTO passwordData, Long id) throws Exception {
        Usuario updatedUser = this.usuarioRepository.findById(id).orElseThrow(UserNotFoundException::new);
        updatedUser.setPassword(passwordData.password());
        this.usuarioRepository.save(updatedUser);
    }
}
