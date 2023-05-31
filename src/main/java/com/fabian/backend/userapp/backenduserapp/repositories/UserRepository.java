package com.fabian.backend.userapp.backenduserapp.repositories;

import com.fabian.backend.userapp.backenduserapp.models.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    
    
}
