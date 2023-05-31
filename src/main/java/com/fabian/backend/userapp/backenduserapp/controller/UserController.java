
//1.-Primero se debe crear una entidad (tabla en base de datos con sus respectivas anotaciones,incluye validaciones tipo @email por ej.)
//2.-Luego se debe crear una interface "EntidadRepository" que implemente de CrudRepository o JPARepository.
//3.-Despues crear un package service que contiene una interface SERVICE con la declaracion de metodos CRUD.
//4.-Seguido crear una segunda clase Service que HEREDA (extends) de la INTERFAZ SERVICE(3) creada anteriormente donde se IMPLEMENTARAN TODOS LOS METODOS DE LA INTERFAZ.
//5.-Crear nuestra clase Controller de nuestra entidad que utilizara un atributo de tipo private con el nombre de la clase SERVICE creada en el punto anterior (4).


package com.fabian.backend.userapp.backenduserapp.controller;

import com.fabian.backend.userapp.backenduserapp.models.entities.User;
import com.fabian.backend.userapp.backenduserapp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/users")
@CrossOrigin(originPatterns = "*")
public class UserController {
    
    @Autowired
    private UserService service;

    //Metodo que trae todos los usuarios
    @GetMapping
    public List<User> list() {
        return service.findAllUsers();
    }

    //Metodo para mostrar un usuario por id
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<User> userOptional = service.findUserById(id);

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    //Metodo para crear un usuario
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody @Valid User user ,BindingResult binding) {
        if (binding.hasErrors()) {
            return validation(binding); 
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveUser(user));
    }

    //Metodo para actualizar un usuario
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody @Valid User user,BindingResult binding, @PathVariable Long id) {


        Optional<User> op = service.updateUser(user, id);
        if (op.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(op.orElseThrow());
        }
        return ResponseEntity.notFound().build();

    }
    //Metodo para eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable Long id) {
        Optional<User> op = service.findUserById(id);
        User userDb = op.orElseThrow();
        if (op.isPresent()) {
            service.removeUser(id);
            System.out.println("Usuario eliminado con ID : " + id + " USERNAME: " + userDb.getUsername() );
            return ResponseEntity.noContent().build(); //204 no devuelve nada solo elimina.
        }
        return ResponseEntity.notFound().build();
    }

    //Metodo para validar errores
    private ResponseEntity<?> validation(BindingResult binding) {
        Map<String,String> errors = new HashMap<>();
        binding.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
