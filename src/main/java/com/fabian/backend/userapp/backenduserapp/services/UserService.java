package com.fabian.backend.userapp.backenduserapp.services;

import java.util.List;
import java.util.Optional;

import com.fabian.backend.userapp.backenduserapp.models.entities.User;


public interface UserService {
    List<User> findAllUsers();

    Optional<User> findUserById(Long id);

    User saveUser(User user);

    Optional<User> updateUser(User user, Long id);

    void removeUser(Long id);

}
