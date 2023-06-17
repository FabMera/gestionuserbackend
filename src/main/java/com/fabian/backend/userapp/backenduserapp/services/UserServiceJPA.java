package com.fabian.backend.userapp.backenduserapp.services;

import java.util.List;
import java.util.Optional;

import com.fabian.backend.userapp.backenduserapp.models.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fabian.backend.userapp.backenduserapp.models.entities.User;
import com.fabian.backend.userapp.backenduserapp.repositories.UserRepository;

@Service
public class UserServiceJPA implements UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return (List<User>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        String passwordBCRYPT= passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordBCRYPT);
        return repository.save(user);
    }

    @Override
    @Transactional
    public Optional<User> updateUser(UserRequest user, Long id) {
        Optional<User> op = findUserById(id); // Optional<User> op = repository.findById(id
        User userOptional = null;
        if (op.isPresent()) {
            User userDb = op.orElseThrow();
            userDb.setUsername(user.getUsername());
            userDb.setEmail(user.getEmail());
            userOptional = this.saveUser(userDb);
        }
        return Optional.ofNullable(userOptional);
    }

    @Override
    @Transactional
    public void removeUser(Long id) {
        repository.deleteById(id);
    }

}
