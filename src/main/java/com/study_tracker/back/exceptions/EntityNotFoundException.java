package com.study_tracker.back.exceptions;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(Long id, String entityName){
        super(String.format("El objeto no fue encontrado - id no encontrado: %d - Nombre de la entidad: %s",
                id, entityName));
    }
    public EntityNotFoundException(String entityName){
        super(String.format("El objeto no fue encontrado por su nombre - Nombre de la entidad: %s",
                entityName));
    }
}
