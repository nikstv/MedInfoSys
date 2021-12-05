package com.stv.medinfosys.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ObjectAlreadyExistsException extends RuntimeException{
    public ObjectAlreadyExistsException(String message) {
        super(message);
    }
}
