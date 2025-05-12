package com.study_tracker.back.exceptions;


public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException() {
        super("No hay un usuario autenticado. Por favor, inicia sesión e incluye un token válido.");
    }
    public UserNotAuthenticatedException(String message) {
        super(message);
    }
}
