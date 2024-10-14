package com.kike.suscription.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s not found with the %s with value %s",resourceName,fieldName,fieldValue));
    }
}
