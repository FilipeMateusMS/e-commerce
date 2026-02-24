package com.project.api.ecommerce.exceptions;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException( String message ){
        super( message );
    }
}

