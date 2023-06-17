package com.fabian.backend.userapp.backenduserapp.auth;

import com.fabian.backend.userapp.backenduserapp.auth.filters.JwtAuthenticationFilter;
import com.fabian.backend.userapp.backenduserapp.auth.filters.JwtValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//SecurityFilterChain es para que se pueda configurar la seguridad de la aplicacion web con spring security .
//HttpSecurity es para que se pueda configurar la seguridad de la aplicacion web con spring security .
//Bean sirve para que se pueda inyectar en otras clases y no se tenga que instanciar con new
//Get es para que se pueda acceder a la ruta sin autenticacion y permitAll es para que se pueda acceder a la ruta sin autenticacion
// y anyRequest().authenticated() es para que se tenga que autenticar para acceder a cualquier otra ruta que no sea la de get de arriba
//csrf es para deshabilitar la proteccion csrf que es para evitar ataques de tipo cross-site request forgery
//sessionCreationPolicy es para que no se cree una sesion en el servidor y se maneje
//todo con tokens y asi no se tenga que estar guardando en el servidor.
//y se pueda escalar mejor la aplicacion y se pueda manejar mejor la seguridad de la aplicacion
//STATELESS es para que no se cree una sesion en el servidor.

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration autconfig;

    // NoOpPasswordEncoder esta deprecado pero lo usamos para que no se encripte la contraseña y se guarde en texto plano solo para usos de test y pruebas.
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return autconfig.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests().requestMatchers(HttpMethod.GET, "/users").permitAll()
                .anyRequest().authenticated().and()
                .addFilter(new JwtAuthenticationFilter(autconfig.getAuthenticationManager()))
                .addFilter(new JwtValidationFilter(autconfig.getAuthenticationManager()))
                .csrf(config -> config.disable())
                .sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).build();
    }
}
