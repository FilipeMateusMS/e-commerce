package com.project.api.ecommerce.exceptions;

public class BusinessErrorException extends RuntimeException {
    public BusinessErrorException(String message) {
        super(message);
    }
}