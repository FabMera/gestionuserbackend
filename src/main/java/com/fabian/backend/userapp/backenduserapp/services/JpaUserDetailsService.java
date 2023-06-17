package com.fabian.backend.userapp.backenduserapp.services;

import com.fabian.backend.userapp.backenduserapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //Se hace referencia al paquete de la clase User de models.entities
        Optional<com.fabian.backend.userapp.backenduserapp.models.entities.User> o = userRepository.findByUsername(username);
        //Si no esta presente el usuario no existe en la base de datos y se lanza la excepcion.
        if (!o.isPresent()) {
            throw new UsernameNotFoundException(String.format("Username %s not found in the system..", username));
        }
        com.fabian.backend.userapp.backenduserapp.models.entities.User user = o.orElseThrow();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }
}
