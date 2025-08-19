package com.ecommerce.product.exceptions;

//Custom exceptions if category name is already exist , we need to throw an exception

public class APIExceptions extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public APIExceptions() {
    }

    public APIExceptions(String message) {
        super(message);
    }
}
