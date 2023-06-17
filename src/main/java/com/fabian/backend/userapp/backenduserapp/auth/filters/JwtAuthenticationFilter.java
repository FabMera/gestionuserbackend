package com.fabian.backend.userapp.backenduserapp.auth.filters;

import com.fabian.backend.userapp.backenduserapp.models.entities.User;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static com.fabian.backend.userapp.backenduserapp.auth.TokenJWTconfig.*;

//Esta clase se encarga de validar el usuario y contraseña con el AuthenticationManager y generar el token con JwtProvider y agregarlo al header de la respuesta
//attemptAuthentication: se ejecuta cuando se hace un POST a /login y se valida el usuario y contraseña con el AuthenticationManager
//successfulAuthentication: se ejecuta cuando el usuario y contraseña son validos y se genera el token con JwtProvider y se agrega al header de la respuesta
//UsernamePasswordAuthenticationFilter: es una clase de Spring Security que se encarga de validar el usuario y contraseña con el AuthenticationManager y generar el token con JwtProvider y agregarlo al header de la respuesta
//unsuccessfulAuthentication: se ejecuta cuando el usuario y contraseña no son validos y se agrega al header de la respuesta un mensaje de error de autenticacion
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        User user = null;
        String username = null;
        String password = null;
        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getPassword();
            logger.info("Username desde request InputStream (raw): " + username);
            logger.info("Password desde request InputStream (raw): " + password);
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authToken);
    }

    //Metodo cuando el usuario y contraseña son validos y se genera el token con JwtProvider y se agrega al header de la respuesta
    //getBytes(): convierte un String a un arreglo de bytes
    //Base64.getEncoder().encodeToString(): convierte un arreglo de bytes a un String
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername();
        String token = Jwts.builder()
                .setSubject(username) //payload solo username del usuario
                .signWith(SECRET_KEY) //Firma el token con la clave secreta.
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))//Agregamos la fecha de creacion y expiracion del token
                .compact();

        //Agregamos el token al header de la respuesta

        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);
        Map<String, Object> body = new HashMap<>();
        //Agregamos el token al body de la respuesta
        body.put("token", token);
        body.put("message", String.format("Hola %s, has iniciado sesion con exito", username));
        body.put("username", username);
        //Convertimos el body a JSON y lo agregamos al response de la peticion POST /login como respuesta en el body .
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(200);
        response.setContentType(CONTENT_TYPE);
    }

    //Metodo cuando el usuario y contraseña no son validos y se agrega al header de la respuesta un mensaje de error de autenticacion
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Error de autenticacion: username o password incorrecto");
        body.put("error", failed.getMessage());
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType(CONTENT_TYPE);
    }

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}
