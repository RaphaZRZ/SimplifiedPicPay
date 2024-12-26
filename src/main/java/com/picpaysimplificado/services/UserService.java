package com.picpaysimplificado.services;

import com.picpaysimplificado.dtos.UpdatePasswordDTO;
import com.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.exceptions.*;
import com.picpaysimplificado.models.User;
import com.picpaysimplificado.models.UserType;
import com.picpaysimplificado.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public User findUserById(Long id) throws Exception {
        return this.userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User findUserByDocument(String document) throws Exception {
        return this.userRepository.findUserByDocument(document).orElseThrow(UserNotFoundException::new);
    }

    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    public void saveUser(User user) {
        this.userRepository.save(user);
    }

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {
        if (sender.getUserType() == UserType.MERCHANT)
            throw new MerchantTransactionNotAllowedException();

        if (sender.getBalance().compareTo(amount) < 0)
            throw new InsufficientBalanceException();
    }

    @Transactional
    public User createUser(UserDTO userData) throws Exception {
        // Verifica se as informações únicas já estão cadastradas no banco de dados
        if (this.userRepository.existsByEmail(userData.email()))
            throw new EmailAlreadyExistsException();
        if (this.userRepository.existsByDocument(userData.document()))
            throw new DocumentAlreadyExistsException();

        // Verify if the document format is valid
        if (userData.userType() == UserType.MERCHANT && userData.document().length() != 14)
            throw new InvalidCNPJException();
        else if (userData.userType() == UserType.COMMON && userData.document().length() != 11)
            throw new InvalidCPFException();

        User user = new User(userData);
        saveUser(user);
        return user;
    }

    @Transactional
    public void updateUser(UpdatePasswordDTO passwordData, Long id) throws Exception {
        User updatedUser = this.userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        updatedUser.setPassword(passwordData.password());
        this.userRepository.save(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) throws Exception {
        // Poderia utilizar o método findUsuarioById, porém traria um retorno de um objeto Usuário desnecessário
        if (!this.userRepository.existsById(id))
            throw new UserNotFoundException();

        this.userRepository.deleteById(id);
    }
}
