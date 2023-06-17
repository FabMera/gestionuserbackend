package com.fabian.backend.userapp.backenduserapp.repositories;

import com.fabian.backend.userapp.backenduserapp.models.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    
    Optional<User> findByUsername(String username);

    //Otra alternativa:
    @Query("SELECT u FROM User u WHERE u.username = ?1") //JPQL 1 PRIMER ARGUMENTO
    Optional<User> gertUserByUsername(String username);
}
