package com.project.api.ecommerce.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException( String message ){
        super( message );
    }
}
