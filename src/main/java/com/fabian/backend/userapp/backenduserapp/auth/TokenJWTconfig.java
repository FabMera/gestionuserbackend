package com.fabian.backend.userapp.backenduserapp.auth;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class TokenJWTconfig {

    //public final static String SECRET_KEY = "algun_token_con_clave_secreta";
    //Lo traemos de JWT librerias.
    public final static Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public final static String PREFIX_TOKEN = "Bearer";
    public final static String HEADER_AUTHORIZATION = "Authorization";
    public final static String CONTENT_TYPE = "application/json";

}