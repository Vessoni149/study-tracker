package com.study_tracker.back.exceptions;

public class SubjectsUserNotFoundException extends RuntimeException{
    public SubjectsUserNotFoundException(String message) {
        super(message);
    }
}
