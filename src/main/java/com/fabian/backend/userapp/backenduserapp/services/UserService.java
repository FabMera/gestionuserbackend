package com.fabian.backend.userapp.backenduserapp.services;

import java.util.List;
import java.util.Optional;

import com.fabian.backend.userapp.backenduserapp.models.UserRequest;
import com.fabian.backend.userapp.backenduserapp.models.entities.User;


public interface UserService {
    List<User> findAllUsers();

    //Optional porque puede que no exista el usuario
    Optional<User> findUserById(Long id);

    User saveUser(User user);

    //Optional porque puede que no exista el usuario
    Optional<User> updateUser(UserRequest user, Long id);

    void removeUser(Long id);

}
